(ns conao3.kanban.handler-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.handler :as c.handler]
   [conao3.kanban.router :as c.router]
   [reitit.ring :as reitit.ring]))

(t/deftest handler-test
  (let [router (c.router/make-routes c.handler/handler nil nil)
        handler (reitit.ring/ring-handler
                 router
                 (reitit.ring/redirect-trailing-slash-handler {:method :strip}))]
    (t/is (= {:status 200 :headers {} :body "ok"}
             (handler {:request-method :get :uri "/api/health"})))))

(t/deftest handler-redirect-test
  (let [router (c.router/make-routes c.handler/handler nil nil)
        handler (reitit.ring/ring-handler
                 router
                 (reitit.ring/redirect-trailing-slash-handler {:method :strip}))]
    (t/is (= {:status 200 :headers {} :body "ok"}
             (handler {:request-method :get :uri "/api/health"})))

    (t/is (= {:status 301 :headers {"Location" "/api/health"} :body ""}
             (handler {:request-method :get :uri "/api/health/"})))))
