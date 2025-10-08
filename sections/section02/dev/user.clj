#_{:clj-kondo/ignore [:unused-namespace :unused-referred-var]}
(ns user
  (:require
   [clojure.java.classpath :as cp]
   [clojure.string :as str]
   [clojure.tools.namespace.repl :as clojure.tools.namespace.repl]
   [com.stuartsierra.component.user-helpers :refer [dev go]]))

(->> (cp/classpath-directories)
     (filter #(not (str/includes? % "resources")))
     (apply clojure.tools.namespace.repl/set-refresh-dirs))
