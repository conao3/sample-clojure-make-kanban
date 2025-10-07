(ns conao3.kanban.router-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.router :as c.router]
   [reitit.core :as reitit]))

(t/deftest router-test
  (t/is (nil? (reitit/match-by-path c.router/router "/api/")))

  (t/is (= {:template "/api/health"
            :path "/api/health"
            :path-params {}}
           (-> (reitit/match-by-path c.router/router "/api/health")
               (select-keys [:template :path :path-params])))))
