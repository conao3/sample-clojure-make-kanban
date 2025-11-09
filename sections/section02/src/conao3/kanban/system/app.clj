(ns conao3.kanban.system.app
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [conao3.kanban.app :as c.app]))

(defrecord App [router app]
  component/Lifecycle
  (start [this]
    (log/info "Starting App...")
    (let [app (c.app/make-app (:router router))]
      (log/info "Started App")
      (assoc this :app app)))

  (stop [this]
    (log/info "Stopping App...")
    (log/info "Stopped App")
    (assoc this :app nil)))
