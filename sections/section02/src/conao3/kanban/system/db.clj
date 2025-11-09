(ns conao3.kanban.system.db
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [next.jdbc :as jdbc]))

(defrecord Db [datasource db-spec]
  component/Lifecycle
  (start [this]
    (log/info "Starting Db...")
    (let [ds (jdbc/get-datasource db-spec)]
      (log/info "Started Db")
      (assoc this :datasource ds)))

  (stop [this]
    (log/info "Stopping Db...")
    (when datasource
      (.close ^java.lang.AutoCloseable datasource))
    (log/info "Stopped Db")
    (assoc this :datasource nil)))
