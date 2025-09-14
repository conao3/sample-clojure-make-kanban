(ns conao3.kanban.middleware
  (:require
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]))

(defn transform-keys [handler]
  (fn [req]
    (let [res (handler (cond-> req
                         (map? (:params req))
                         (update :params (partial cske/transform-keys csk/->kebab-case))))]
      (cond-> res
        (map? (:body res))
        (update :body (partial cske/transform-keys csk/->camelCase))))))
