(ns conao3.kanban.handler
  (:require
   [reitit.ring :as reitit.ring]
   [conao3.kanban.routes :as c.routes]))

(def handler
  (-> c.routes/routes
      reitit.ring/router
      reitit.ring/ring-handler))
