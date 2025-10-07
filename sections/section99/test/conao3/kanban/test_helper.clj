(ns conao3.kanban.test-helper
  (:require
   [clojure.string :as str]))

(defn random-str
  ([] (format "%s-%s" (random-str 7) (random-str 7)))
  ([n] (->> (repeatedly #(rand-nth (concat (range (int \a) (int \z))
                                           (range (int \A) (int \Z)))))
            (map char)
            (take n)
            str/join)))

(defn random-bool []
  (rand-nth [true false]))
