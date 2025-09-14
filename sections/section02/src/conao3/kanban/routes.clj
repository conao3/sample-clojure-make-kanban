(ns conao3.kanban.routes
  (:require
   [reitit.ring :as reitit.ring]
   [conao3.kanban.resolver :as c.resolver]))

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
     ["/user/:id" {:get c.resolver/user-get :put c.resolver/user-update}]]]))
