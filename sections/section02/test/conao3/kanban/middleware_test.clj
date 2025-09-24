(ns conao3.kanban.middleware-test
  (:require
   [clojure.test :as t]
   [conao3.kanban.middleware :as c.middleware]
   [conao3.kanban.test-helper :as h]))

(t/deftest transform-keys-test
  (let [access-token (h/random-str)
        previous-password (h/random-str)
        proposed-password (h/random-str)
        user-name (h/random-str)
        called-value (atom nil)
        handler (fn [req]
                  (reset! called-value (:params req))
                  {:body @called-value})
        request-params {:accessToken access-token
                        :previousPassword previous-password
                        :proposedPassword proposed-password
                        :user-name user-name}
        handler-params {:access-token access-token
                        :previous-password previous-password
                        :proposed-password proposed-password
                        :user-name user-name}
        response-body {:accessToken access-token
                       :previousPassword previous-password
                       :proposedPassword proposed-password
                       :userName user-name}]
    (t/is (= {:body response-body}
             ((c.middleware/transform-keys handler)
              {:params request-params})))
    (t/is (= handler-params @called-value))))
