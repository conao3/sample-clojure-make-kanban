(ns conao3.kanban.resolver
  (:require
   [ring.util.http-response :as res]))

(defn health [_req]
  (res/ok "ok"))

(defn auth-signup
  "req: :username str :password str"
  [_req]
  (res/ok "ok"))

(defn auth-initiate-auth
  "req: :username str :password str"
  [_req]
  (res/ok
   {:access-token "asdfasdf"
    :id-token "asdf"
    :refresh-token "asdf"}))

(defn auth-refresh-token
  "req: :refresh-token str"
  [_req]
  (res/ok
   {:access-token "asdfasdf"
    :id-token "asdf"
    :refresh-token "asdf"}))

(defn auth-change-password
  "req: :access-token str :previous-password str :proposed-password str"
  [_req]
  (res/ok "ok"))

(defn auth-forgot-password
  "req: :user-name str"
  [_req]
  (res/ok "ok"))

(defn auth-delete-user
  "req: :access-token str"
  [_req]
  (res/ok "ok"))

(defn user-get
  "req: :id-token str"
  [_req]
  (res/ok {:name "asdf"}))

(defn user-update
  "req: :id-token str :name str"
  [_req]
  (res/ok "ok"))
