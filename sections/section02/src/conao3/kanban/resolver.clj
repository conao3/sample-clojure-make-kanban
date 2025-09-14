(ns conao3.kanban.resolver
  (:require
   [clojure.data.json :as json]))

(defn health [_req]
  {:status 200
   :body "ok"})

(defn auth-signup
  "req: :username str :password str"
  [_req]
  {:status 200
   :body "ok"})

(defn auth-initiate-auth
  "req: :username str :password str"
  [_req]
  {:status 200
   :body (json/write-str
          {:access-token "asdfasdf"
           :id-token "asdf"
           :refresh-token "asdf"})})

(defn auth-refresh-token
  "req: :refresh-token str"
  [_req]
  {:status 200
   :body (json/write-str
          {:access-token "asdf"
           :id-token "asdf"
           :refresh-token "asdf"})})

(defn auth-change-password
  "req: :access-token str :previous-password str :proposed-password str"
  [_req]
  {:status 200
   :body "ok"})

(defn auth-forgot-password
  "req: :user-name str"
  [_req]
  {:status 200
   :body "ok"})

(defn auth-delete-user
  "req: :access-token str"
  [_req]
  {:status 200
   :body "ok"})

(defn user-get
  "req: :id-token str"
  [_req]
  {:status 200
   :body {:name "asdf"}})

(defn user-update
  "req: :id-token str :name str"
  [_req]
  {:status 200
   :body "ok"})
