(ns conao3.kanban.handler
  (:require
   [conao3.kanban.route :as c.route]
   [reitit.ring :as reitit.ring]))

(def handler
  (-> c.route/router
      (reitit.ring/ring-handler
       (reitit.ring/redirect-trailing-slash-handler {:method :strip}))))
