(ns conao3.kanban.util
  (:require
   [camel-snake-kebab.extras :as cske]))

(defn walk-update-keys [m f]
  (cske/transform-keys f m))
