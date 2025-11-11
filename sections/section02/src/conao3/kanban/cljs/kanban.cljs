(ns ^:figwheel-hooks conao3.kanban.cljs.kanban
  (:require
   ["@apollo/client" :as apollo :refer [ApolloClient]]
   ["@apollo/client/react" :as apollo.react]
   ["react" :as react]
   [reagent.dom.client :as reagent.dom.client]))

(enable-console-print!)

(defonce apollo-client (ApolloClient.
                        #js {:link (apollo/HttpLink. #js {:uri "/api/graphql"})
                             :cache (apollo/InMemoryCache.)
                             :connectToDevTools goog.DEBUG}))

(defn TaskAdder []
  (let [[title set-title] (react/useState "")
        [create-task result] (apollo.react/useMutation
                              (apollo/gql "
mutation createTask ($title: String!) {
  createTask(title: $title status: \"TODO\") {
    id taskId title status createdAt updatedAt
  }
}")
                              #js {:refetchQueries #js ["tasks"]})]
    [:div
     [:h2 "Add Task"]
     [:input {:value title :on-change #(set-title (-> % .-target .-value))}]
     [:button {:type "button"
               :on-click (fn []
                           (create-task #js {:variables #js {:title title}})
                           (set-title ""))
               :disabled (-> result .-loading)}
      "Add"]
     (when-let [error (-> result .-error)]
       [:div {:style {:color "red"}}
        "Error: " (str error)])]))

(defn TaskList []
  (let [result (apollo.react/useQuery (apollo/gql "
query tasks {
  tasks {
    id taskId title status createdAt updatedAt
  }
}"))
        [update-task] (apollo.react/useMutation
                       (apollo/gql "
mutation updateTask($taskId: String! $status: String!) {
  updateTask(taskId: $taskId status: $status) {
    id status
  }
}"))
        [delete-task] (apollo.react/useMutation
                       (apollo/gql "
mutation deleteTask($taskId: String!) {
  deleteTask(taskId: $taskId) {
    id
  }
}")
                       #js {:refetchQueries #js ["tasks"]})
        tasks (some-> result .-data .-tasks (js->clj :keywordize-keys true))]
    [:div
     [:h2 "Tasks"]
     [:div {:style {:display "flex"
                    :max-width "1000px"}}
      [:div {:style {:flex 1}}
       [:h3 "Todo"]
       [:ul
        (->> tasks (filter #(= "TODO" (:status %)))
             (map (fn [task]
                    [:li {:key (:id task)}
                     (:title task)
                     [:button {:type "button"
                               :on-click #(update-task
                                           #js {:variables
                                                #js {:taskId (:taskId task) :status "DOING"}})}
                      "Next"]])))]
       (when (.-loading result) [:div "fetching..."])
       (when (.-error result) [:div "error" (-> result .-error .-message)])]
      [:div {:style {:flex 1}}
       [:h3 "Doing"]
       [:ul
        (->> tasks (filter #(= "DOING" (:status %)))
             (map (fn [task]
                    [:li {:key (:id task)}
                     (:title task)
                     [:button {:type "button"
                               :on-click #(update-task
                                           #js {:variables
                                                #js {:taskId (:taskId task) :status "DONE"}})}
                      "Next"]])))]]
      [:div {:style {:flex 1}}
       [:h3 "Done"]
       [:ul
        (->> tasks (filter #(= "DONE" (:status %)))
             (map (fn [task]
                    [:li {:key (:id task)}
                     (:title task)
                     [:button {:type "button"
                               :on-click #(delete-task #js {:variables #js {:taskId (:taskId task)}})}
                      "Delete"]])))]]]]))

(defn App []
  [:> apollo.react/ApolloProvider {:client apollo-client}
   [:div
    [:h1 "Kanban Board"]
    [:f> TaskAdder]
    [:f> TaskList]]])

(defonce root (-> js/document (.getElementById "app") reagent.dom.client/create-root))

(defn ^:after-load init []
  (reagent.dom.client/render root [App]))

(defonce initialized (do (init) true))
