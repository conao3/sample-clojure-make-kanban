(ns conao3.kanban.handler-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.handler :as c.handler]))

(t/deftest handler-test
  (t/is (= {:status 200 :headers {} :body "ok"}
           (c.handler/handler {:request-method :get :uri "/api/health"}))))

(t/deftest handler-redirect-test
  (t/is (= {:status 200 :headers {} :body "ok"}
           (c.handler/handler {:request-method :get :uri "/api/health"})))

  (t/is (= {:status 301 :headers {"Location" "/api/health"} :body ""}
           (c.handler/handler {:request-method :get :uri "/api/health/"}))))
