(ns conao3.kanban.system.router
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [conao3.kanban.router :as c.router]))

(defrecord Router [handler db router]
  component/Lifecycle
  (start [this]
    (log/info "Starting Router...")
    (let [router (c.router/make-routes (:handler handler) (:schema handler) db)]
      (log/info "Started Router")
      (assoc this :router router)))

  (stop [this]
    (log/info "Stopping Router...")
    (log/info "Stopped Router")
    (assoc this :router nil)))
