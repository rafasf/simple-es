(ns simple-es.inventory
  (:require [simple-es.command :as command]
            [simple-es.store :as store]
            [simple-es.event :as events]
            [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [taoensso.timbre :as timbre]))

(defstruct item :id :description :price :last_change)

(defn add [item]
  (command/create :add-item item))

(defn change-price [item-id new-price]
  (command/create :change-item-price {:item-id item-id :price new-price}))

(defn add-item-handler [command]
  (store/save (events/create-from (second command) :item-added)))

(defn change-price-handler [command]
  (let [action (second command)
        current-item (events/replay (store/find-with-id (:item-id action)))
        change-type (if (> (:price current-item) (:price action)) :item-price-decreased :item-price-increased)]
    (store/save (events/create-from action change-type))))

(def handlers {
               :add-item add-item-handler
               :change-item-price change-price-handler })
