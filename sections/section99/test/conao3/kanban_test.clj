(ns conao3.kanban-test
  (:require
   [clojure.test :as t]
   [conao3.kanban :as c.kanban]))

(t/deftest add-test
  (t/is (= 3 (c.kanban/add 1 2))))
