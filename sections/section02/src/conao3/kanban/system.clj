(ns conao3.kanban.system 
  (:require
   [conao3.kanban.system.handler :as c.system.handler]
   [conao3.kanban.system.server :as c.system.server]
   [com.stuartsierra.component :as component]))

(defn new-system []
  (component/system-map
   :handler (c.system.handler/map->Handler {})
   :server (component/using
            (c.system.server/map->Server {})
            [:handler])))
