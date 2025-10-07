(ns conao3.kanban.system.handler
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [conao3.kanban.handler :as c.handler]))

(defrecord Handler [handler]
  component/Lifecycle
  (start [this]
    (log/info "Starting Handler...")
    (log/info "Started Handler")
    (assoc this :handler c.handler/handler))

  (stop [this]
    (log/info "Stopping Handler...")
    (log/info "Stopped Handler")
    (assoc this :handler nil)))
