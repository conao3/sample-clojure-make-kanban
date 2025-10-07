(ns conao3.kanban.handler
  (:require
   [conao3.kanban.router :as c.router]
   [reitit.ring :as reitit.ring]))

(def handler
  (-> c.router/router
      (reitit.ring/ring-handler
       (reitit.ring/redirect-trailing-slash-handler {:method :strip}))))
