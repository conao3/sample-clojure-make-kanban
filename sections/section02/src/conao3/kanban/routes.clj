(ns conao3.kanban.routes
  (:require
   [reitit.ring :as reitit.ring]
   [reitit.ring.coercion :as reitit.ring.coercion]
   [reitit.ring.middleware.muuntaja :as reitit.ring.middleware.muuntaja]
   [reitit.coercion.spec :as reitit.coercion.spec]
   [muuntaja.core :as muuntaja]
   [muuntaja.middleware :as muuntaja.middleware]
   [conao3.kanban.resolver :as c.resolver]
   [conao3.kanban.middleware :as c.middleware]))

(def router
  (reitit.ring/router
   [["/api"
     ["/health" {:get c.resolver/health}]
     ["/auth"
      ["/signup" {:post c.resolver/auth-signup}]
      ["/initiate-auth" {:post c.resolver/auth-initiate-auth}]
      ["/refresh-token" {:post c.resolver/auth-refresh-token}]
      ["/change-password" {:post c.resolver/auth-change-password}]
      ["/forgot-password" {:post c.resolver/auth-forgot-password}]
      ["/delete-user" {:post c.resolver/auth-delete-user}]]
     ["/user/:id" {:get c.resolver/user-get :put c.resolver/user-update}]]]
   {:data {:muuntaja muuntaja/instance
           :coercion reitit.coercion.spec/coercion
           :middleware [reitit.ring.middleware.muuntaja/format-middleware
                        muuntaja.middleware/wrap-params
                        c.middleware/transform-keys
                        reitit.ring.coercion/coerce-exceptions-middleware
                        reitit.ring.coercion/coerce-request-middleware
                        reitit.ring.coercion/coerce-response-middleware]}}))
