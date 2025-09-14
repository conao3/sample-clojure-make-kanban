(ns conao3.kanban
  (:require
   [clojure.tools.logging :as log]
   [org.httpkit.server :as httpkit.server]
   [conao3.kanban.handler :as c.handler])
  (:gen-class))

(defn add [a b]
  (+ a b))

(defn run-server []
  (let [ret (httpkit.server/run-server c.handler/handler)
        server (-> ret meta :server)]
    (log/info "Started Server at port:" (httpkit.server/server-port server))
    {:server server
     :server-stop-fn ret}))

(defn -main [& args]
  (println "Hello World," args)
  (run-server))
