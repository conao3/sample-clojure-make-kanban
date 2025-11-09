(ns conao3.kanban.system
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [com.stuartsierra.component :as component]
   [conao3.kanban.system.db :as c.system.db]
   [conao3.kanban.system.handler :as c.system.handler]
   [conao3.kanban.system.server :as c.system.server]))

(defn new-system [profile]
  (let [config (aero/read-config (io/resource "config.edn") {:profile profile})]
    (component/system-map
     :db (c.system.db/map->Db {:db-spec (:db config)})
     :handler (c.system.handler/map->Handler {})
     :server (component/using
              (c.system.server/map->Server {:port (-> config :server :port)})
              [:handler]))))
