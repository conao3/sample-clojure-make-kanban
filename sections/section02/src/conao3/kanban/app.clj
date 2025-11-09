(ns conao3.kanban.app
  (:require
   [reitit.ring :as reitit.ring]))

(defn make-app [router]
  (reitit.ring/ring-handler
   router
   (reitit.ring/routes
    (reitit.ring/create-resource-handler {:path "/"})
    (reitit.ring/redirect-trailing-slash-handler {:method :strip}))))
