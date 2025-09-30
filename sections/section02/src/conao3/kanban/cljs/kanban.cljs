(ns conao3.kanban.cljs.kanban
  (:require
   [reagent.core :as reagent]
   [reagent.dom :as reagent.dom]))

(defn- app []
  (let [counter (reagent/atom 1)]
    (fn []
      [:div
       "hello"
       [:div @counter]
       [:button {:on-click (fn [] (swap! counter inc))} "inc"]])))

(defn- ^:dev/after-load render
  []
  (reagent.dom/render [app] (.getElementById js/document "app")))

(defn- ^:export init []
  (render))
