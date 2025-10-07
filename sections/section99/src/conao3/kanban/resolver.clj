(ns conao3.kanban.resolver
  (:require
   [ring.util.http-response :as res]))

(defmulti resolver :name)

(defmethod resolver (with-meta 'api.health
                      {:method :get :path "/api/health"})
  [_req]
  (res/ok "ok"))

(defmethod resolver (with-meta 'api.auth.signup
                      {:method :post :path "/api/auth/signup"})
  [_req]
  "req: :username str :password str"
  (res/ok "ok"))

(defmethod resolver (with-meta 'api.auth.initiate-auth
                      {:method :post :path "/api/auth/initiate-auth"})
  [_req]
  "req: :username str :password str"
  (res/ok
   {:access-token "asdfasdf"
    :id-token "asdf"
    :refresh-token "asdf"}))

(defmethod resolver (with-meta 'api.auth.refresh-token
                      {:method :post :path "/api/auth/refresh-token"})
  [_req]
  "req: :refresh-token str"
  (res/ok
   {:access-token "asdfasdf"
    :id-token "asdf"
    :refresh-token "asdf"}))

(defmethod resolver (with-meta 'api.auth.change-password
                      {:method :post :path "/api/auth/change-password"})
  [_req]
  "req: :access-token str :previous-password str :proposed-password str"
  (res/ok "ok"))

(defmethod resolver (with-meta 'api.auth.forgot-password
                      {:method :post :path "/api/auth/forgot-password"})
  [_req]
  "req: :user-name str"
  (res/ok "ok"))

(defmethod resolver (with-meta 'api.auth.delete-user
                      {:method :post :path "/api/auth/delete-user"})
  [_req]
  "req: :access-token str"
  (res/ok "ok"))

(defmethod resolver (with-meta 'api.user.get
                      {:name 'api.user :method :get :path "/api/user/:id"})
  [_req]
  "req: :id-token str"
  (res/ok {:name "asdf"}))

(defmethod resolver (with-meta 'api.user.update
                      {:name 'api.user :method :put :path "/api/user/:id"})
  [_req]
  "req: :id-token str :name str"
  (res/ok "ok"))
