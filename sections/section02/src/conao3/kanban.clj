(ns conao3.kanban
  (:gen-class)
  (:require
   [clojure.tools.logging :as log]
   [com.stuartsierra.component :as component]
   [conao3.kanban.system :as c.system]))

(defn add [a b]
  (+ a b))

(defn -main [& args]
  (println "Hello World," args)
  (let [system (-> (c.system/new-system)
                   component/start-system)]
    (-> (Runtime/getRuntime)
        (.addShutdownHook (Thread. (fn []
                                     (log/info "Shutdown signal received")
                                     (component/stop-system system)
                                     (log/info "Shutdown hook completed")))))
    (log/info "Server started")
    @(promise)))
