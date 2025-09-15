(ns conao3.kanban
  (:require
   [clojure.tools.logging :as log]
   [conao3.kanban.system :as c.system]
   [com.stuartsierra.component :as component])
  (:gen-class))

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
