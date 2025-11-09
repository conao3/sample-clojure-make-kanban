(ns conao3.kanban.system.db
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [next.jdbc :as jdbc]))

(defrecord Db [db db-spec]
  component/Lifecycle
  (start [this]
    (log/info "Starting Db...")
    (let [ds (jdbc/get-datasource db-spec)]
      (log/info "Started Db")
      (assoc this :db ds)))

  (stop [this]
    (log/info "Stopping Db...")
    (when db
      (when (instance? java.lang.AutoCloseable db)
        (.close ^java.lang.AutoCloseable db)))
    (log/info "Stopped Db")
    (assoc this :db nil)))
