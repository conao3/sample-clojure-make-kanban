(ns conao3.kanban.resolver-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.handler :as c.handler]
   [ring.util.http-response :as res]))

(t/deftest health-test
  (t/is (= (res/ok "ok")
           (c.handler/handler {:name 'api.health}))))
