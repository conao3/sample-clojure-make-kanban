(ns conao3.kanban.resolver-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.resolver :as c.resolver]
   [ring.util.http-response :as res]))

(t/deftest health-test
  (t/is (= (res/ok "ok")
           (c.resolver/resolver {:name 'api.health}))))

(t/deftest auth-signup-test
  (t/is (= (res/ok "ok")
           (c.resolver/resolver {:name 'api.auth.signup}))))

(t/deftest auth-initiate-auth-test
  (t/is (= (res/ok
            {:access-token "asdfasdf"
             :id-token "asdf"
             :refresh-token "asdf"})
           (c.resolver/resolver {:name 'api.auth.initiate-auth}))))

(t/deftest auth-refresh-token-test
  (t/is (= (res/ok
            {:access-token "asdfasdf"
             :id-token "asdf"
             :refresh-token "asdf"})
           (c.resolver/resolver {:name 'api.auth.refresh-token}))))

(t/deftest auth-change-password-test
  (t/is (= (res/ok "ok")
           (c.resolver/resolver {:name 'api.auth.change-password}))))

(t/deftest auth-forgot-password-test
  (t/is (= (res/ok "ok")
           (c.resolver/resolver {:name 'api.auth.forgot-password}))))

(t/deftest auth-delete-user-test
  (t/is (= (res/ok "ok")
           (c.resolver/resolver {:name 'api.auth.delete-user}))))

(t/deftest user-get-test
  (t/is (= (res/ok {:name "asdf"})
           (c.resolver/resolver {:name 'api.user.get}))))

(t/deftest user-update-test
  (t/is (= (res/ok "ok")
           (c.resolver/resolver {:name 'api.user.update}))))
