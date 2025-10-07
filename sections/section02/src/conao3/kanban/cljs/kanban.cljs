(ns conao3.kanban.cljs.kanban
  (:require [reagent.dom.client :as reagent.dom.client]))

(enable-console-print!)

(defn App []
  [:div "hello world"])

(defn init []
  (let [root (-> js/document (.getElementById "app") reagent.dom.client/create-root)]
    (reagent.dom.client/render root [App])))

(defn ^:dev/after-load start []
  (init))
