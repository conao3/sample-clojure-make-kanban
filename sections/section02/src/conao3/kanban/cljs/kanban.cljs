(ns ^:figwheel-hooks conao3.kanban.cljs.kanban
  (:require
   ["urql" :as urql]
   [reagent.dom.client :as reagent.dom.client]))

(enable-console-print!)

(defonce urql-client (urql.Client.
                      #js {:url "/api/graphql"
                           :exchanges #js [urql.cacheExchange urql.fetchExchange]
                           :preferGetMethod false}))

(defn TaskList []
  (let [[result] (urql.useQuery #js {:query "query { tasks { id taskId title status createdAt updatedAt } }"})
        tasks (some-> result .-data .-tasks (js->clj :keywordize-keys true))]
    [:div
     [:h2 "Tasks"]
     (cond
       (.-fetching result) [:p "fetching..."]
       (.-error result) [:p "error" (.-error result)]
       :else
       [:ul
        (if (empty? tasks)
          [:p "No tasks found"]
          (->> tasks
             (map (fn [task]
                    [:li {:key (:id task)} (:title task) " - " (:status task)]))))])]))

(defn App []
  [:> urql.Provider {:value urql-client}
   [:div
    [:h1 "Kanban Board"]
    [:f> TaskList]]])

(defonce root (-> js/document (.getElementById "app") reagent.dom.client/create-root))

(defn ^:after-load init []
  (reagent.dom.client/render root [App]))

(defonce initialized (do (init) true))
