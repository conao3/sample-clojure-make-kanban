(ns conao3.kanban.handler
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [com.walmartlabs.lacinia :as lacinia]
   [com.walmartlabs.lacinia.schema :as schema]
   [com.walmartlabs.lacinia.util :as util]
   [conao3.kanban.resolver.task :as c.resolver.task]
   [ring.util.http-response :as res]))

(defmulti handler :name)

(defmethod handler (with-meta 'api.health
                     {:method :get :path "/api/health"})
  [_req]
  (res/ok "ok"))

(defn compile-schema []
  (-> (io/resource "graphql-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers c.resolver.task/resolvers)
      schema/compile))

(defmethod handler (with-meta 'api.graphql
                     {:method :post :path "/api/graphql"})
  [req]
  (let [schema (:schema req)
        db (:db req)
        body (-> req :body-params)
        query (:query body)
        variables (or (:variables body) {})
        operation-name (:operationName body)
        context {:db (:db db)}
        result (lacinia/execute schema query variables context {:operation-name operation-name})]
    (if (:errors result)
      (res/bad-request result)
      (res/ok result))))
