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

(defmethod handler (with-meta 'api.auth.signup
                     {:method :post :path "/api/auth/signup"})
  [_req]
  "req: :username str :password str"
  (res/ok "ok"))

(defmethod handler (with-meta 'api.auth.initiate-auth
                     {:method :post :path "/api/auth/initiate-auth"})
  [_req]
  "req: :username str :password str"
  (res/ok
   {:access-token "asdfasdf"
    :id-token "asdf"
    :refresh-token "asdf"}))

(defmethod handler (with-meta 'api.auth.refresh-token
                     {:method :post :path "/api/auth/refresh-token"})
  [_req]
  "req: :refresh-token str"
  (res/ok
   {:access-token "asdfasdf"
    :id-token "asdf"
    :refresh-token "asdf"}))

(defmethod handler (with-meta 'api.auth.change-password
                     {:method :post :path "/api/auth/change-password"})
  [_req]
  "req: :access-token str :previous-password str :proposed-password str"
  (res/ok "ok"))

(defmethod handler (with-meta 'api.auth.forgot-password
                     {:method :post :path "/api/auth/forgot-password"})
  [_req]
  "req: :user-name str"
  (res/ok "ok"))

(defmethod handler (with-meta 'api.auth.delete-user
                     {:method :post :path "/api/auth/delete-user"})
  [_req]
  "req: :access-token str"
  (res/ok "ok"))

(defmethod handler (with-meta 'api.user.get
                     {:name 'api.user :method :get :path "/api/user/:id"})
  [_req]
  "req: :id-token str"
  (res/ok {:name "asdf"}))

(defmethod handler (with-meta 'api.user.update
                     {:name 'api.user :method :put :path "/api/user/:id"})
  [_req]
  "req: :id-token str :name str"
  (res/ok "ok"))

(defn compile-schema []
  (-> (io/resource "graphql-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers c.resolver.task/resolvers)
      schema/compile))

(defn graphql-handler [req]
  (let [schema (:schema req)
        db (:db req)
        body (-> req :body-params)
        query (:query body)
        variables (or (:variables body) {})
        operation-name (:operationName body)
        context {:db db}
        result (lacinia/execute schema query variables context {:operation-name operation-name})]
    (if (:errors result)
      (res/bad-request result)
      (res/ok result))))

(defmethod handler (with-meta 'api.graphql
                     {:method :post :path "/api/graphql"})
  [req]
  (graphql-handler req))
