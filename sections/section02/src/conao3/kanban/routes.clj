(ns conao3.kanban.routes
  (:require
   [conao3.kanban.middleware :as c.middleware]
   [conao3.kanban.resolver :as c.resolver]
   [muuntaja.core :as muuntaja]
   [muuntaja.middleware :as muuntaja.middleware]
   [reitit.coercion.spec :as reitit.coercion.spec]
   [reitit.dev.pretty :as reitit.dev.pretty]
   [reitit.ring :as reitit.ring]
   [reitit.ring.coercion :as reitit.ring.coercion]
   [reitit.ring.middleware.muuntaja :as reitit.ring.middleware.muuntaja]
   [reitit.spec :as reitit.spec]))

(def routes
  (->> c.resolver/resolver
       methods
       (group-by (comp :path meta key))
       (map (fn [[path vals]]
              (let [common-val (->> vals
                                    (reduce
                                     (fn [acc elm]
                                       (cond-> acc (:name elm) (assoc :name (:name elm))))
                                     {}))]
                [path (->> vals
                           (mapv (fn [[key val]]
                                   (let [m (meta key)]
                                     (conj (dissoc m :method :path)
                                           {(:method m) val :name (-> key name keyword)}))))
                           (#(conj % common-val))
                           (into {}))])))))

(def router
  (-> routes
      (reitit.ring/router
       {:validate reitit.spec/validate
        :exception reitit.dev.pretty/exception
        :data {:muuntaja muuntaja/instance
               :coercion reitit.coercion.spec/coercion
               :middleware [reitit.ring.middleware.muuntaja/format-middleware
                            muuntaja.middleware/wrap-params
                            c.middleware/transform-keys
                            reitit.ring.coercion/coerce-exceptions-middleware
                            reitit.ring.coercion/coerce-request-middleware
                            reitit.ring.coercion/coerce-response-middleware]}})))
