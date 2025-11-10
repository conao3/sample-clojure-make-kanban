(ns conao3.kanban.util
  (:require
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]))

(defn camelCasePreserveUnderbarPrefix [x]
  (-> (str (re-find #"_*" (name x)) (csk/->camelCase (name x))) keyword))

(defn walk-update-keys [m f]
  (cske/transform-keys f m))
