(ns conao3.kanban.system-test
  (:require
   [clojure.test :as t]
   [com.stuartsierra.component :as component]
   [conao3.kanban.system :as c.system]))

(def system nil)

(defn system-fixture [f]
  (alter-var-root #'system (constantly (-> (c.system/new-system) (dissoc :server))))
  (alter-var-root #'system component/start-system)
  (f)
  (alter-var-root #'system component/stop-system)
  (alter-var-root #'system (constantly nil)))

(t/use-fixtures :once system-fixture)

(t/deftest system-test
  (t/is (= {:status 200 :headers {} :body "ok"}
           ((-> system :handler :handler)
            {:request-method :get :uri "/api/health"}))))
