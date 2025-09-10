(ns conao3.kanban-test
  (:require
   [conao3.kanban :as c.kanban]
   [clojure.test :as t]))

(t/deftest add-test
  (t/is (= 3 (c.kanban/add 1 2))))
