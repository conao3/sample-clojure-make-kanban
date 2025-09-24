(ns conao3.kanban.handler
  (:require
   [conao3.kanban.routes :as c.routes]
   [reitit.ring :as reitit.ring]))

(def handler
  (-> c.routes/router
      (reitit.ring/ring-handler
       (reitit.ring/redirect-trailing-slash-handler {:method :strip}))))
