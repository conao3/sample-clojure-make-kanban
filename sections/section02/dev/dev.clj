(ns dev
  (:require
   [clojure.main :as clojure.main]
   #_{:clj-kondo/ignore [:unused-referred-var]}
   [com.stuartsierra.component.repl :as component.repl :refer [system start stop reset initializer]]
   [conao3.kanban.system :as c.system]))

(apply require clojure.main/repl-requires)

(component.repl/set-init (fn [_] (c.system/new-system)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def go reset)
