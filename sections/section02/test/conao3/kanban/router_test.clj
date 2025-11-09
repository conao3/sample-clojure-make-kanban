(ns conao3.kanban.router-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.handler :as c.handler]
   [conao3.kanban.router :as c.router]
   [reitit.core :as reitit]))

(t/deftest router-test
  (let [router (c.router/make-routes c.handler/handler nil nil)]
    (t/is (nil? (reitit/match-by-path router "/api/")))

    (t/is (= {:template "/api/health"
              :path "/api/health"
              :path-params {}}
             (-> (reitit/match-by-path router "/api/health")
                 (select-keys [:template :path :path-params]))))))
