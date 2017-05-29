(ns airhead-cljs.components
  (:require [reagent.core :as r]
            [airhead-cljs.core :refer [app-state update-state!]]
            [airhead-cljs.requests :as req]))

(defn info-component []
  (fn []
    (let [cursor  (r/cursor app-state [:info])
          title   (@cursor :name)
          message (@cursor :greet_message)
          url     (@cursor :stream_url)]
      [:section#info
       [:h1 title]
       [:p message]
       [:audio {:src url
                :controls "controls"}]])))

(defn upload-component []
  [:section#upload
   [:h2 "Upload"]
   [:form {:id "upload-form"}
    [:input {:type "file" :name "track"}]
    [:input {:type "button" :value "Upload" :on-click req/upload!}]]])

(defn playlist-add-component [track]
  [:input.add
   {:type "button" :value "+"
    :on-click #(req/playlist-add! (:uuid track))}])

(defn playlist-remove-component [track]
  [:input.remove
   {:type "button" :value "-"
    :on-click #(req/playlist-remove! (:uuid track))}])

(defn track-component [track]
  [:span.track
   (if track
     (str " " (:artist track) " - " (:title track))
     "-")])

(defn now-playing-component []
  [:p
   [:span "Now playing:"]
   [track-component (@app-state :now-playing)]])

(defn next-component []
  [:section
   [:span "Next:"]
   [:ul
    (for [track (@app-state :playlist)]
      [:li
       [playlist-remove-component track]
       [track-component track]])]])

(defn playlist-component []
  [:section#playlist
   [:h2 "Playlist"]
   [now-playing-component]
   [next-component]])

(defn on-query-change [e]
  ;(get-library!)
  (update-state! :query (-> e .-target .-value)))

(defn search-component []
  [:section#search
   [:form
    [:label {:for "query"} "Search:"]
    [:input {:type "text"
             :id "query"
             :value (@app-state :query)
             :on-change on-query-change}]]])

(defn library-component []
  [:section#library
   [:h2 "Library"]
   [search-component]
   [:ul (for [track (@app-state :library)]
          [:li
           [playlist-add-component track]
           [track-component track]])]])

(defn page-component []
  [:main
   [info-component]
   [upload-component]
   [playlist-component]
   [library-component]])
