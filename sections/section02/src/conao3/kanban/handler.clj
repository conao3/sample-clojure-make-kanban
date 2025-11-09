(ns conao3.kanban.handler
  (:require
   [clojure.java.io :as io]
   [com.walmartlabs.lacinia :as lacinia]
   [com.walmartlabs.lacinia.parser.schema :as parser.schema]
   [com.walmartlabs.lacinia.schema :as schema]
   [conao3.kanban.resolver.task :as c.resolver.task]
   [ring.util.http-response :as res]))

(defmulti handler :name)

(defmethod handler (with-meta 'api.health
                     {:method :get :path "/api/health"})
  [_req]
  (res/ok "ok"))

(defn attach-resolvers-to-fields [schema resolvers]
  (reduce-kv
   (fn [acc resolver-key resolver-fn]
     (let [type-key (namespace resolver-key)
           field-key (name resolver-key)
           type-keyword (keyword type-key)
           field-keyword (keyword field-key)]
       (update-in acc [:objects type-keyword :fields field-keyword]
                  (fn [field]
                    (assoc field :resolve resolver-fn)))))
   schema
   resolvers))

(defn compile-schema []
  (-> (io/resource "schema.graphql")
      slurp
      parser.schema/parse-schema
      (attach-resolvers-to-fields c.resolver.task/resolvers)
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
