(ns conao3.kanban.routes
  (:require
   [conao3.kanban.resolver :as c.resolver]))

(def routes
  [["/api"
    ["/health" c.resolver/health]
    ["/auth"
     ["/signup" {:get c.resolver/auth-signup}]
     ["/initiate-auth" {:get c.resolver/auth-initiate-auth}]
     ["/refresh-token" {:get c.resolver/auth-refresh-token}]
     ["/change-password" {:get c.resolver/auth-change-password}]
     ["/forgot-password" {:get c.resolver/auth-forgot-password}]
     ["/delete-user" {:get c.resolver/auth-delete-user}]]
    ["/user/:id" {:get c.resolver/user-get :put c.resolver/user-update}]]])
