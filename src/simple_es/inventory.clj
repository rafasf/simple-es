(ns simple-es.inventory
  (:require [simple-es.command :as command]
            [simple-es.store :as store]
            [simple-es.event :as events]
            [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [taoensso.timbre :as timbre]))

(defn with-type [item]
  (assoc item :_type :inventory))

(defn add [item]
  (command/create :add-item item))

(defn change-price [id new-price]
  (command/create :change-item-price {:id id :price new-price}))

(defn add-item-handler [command]
  (store/save (with-type (events/create-from command :item-added))))

(defn change-price-handler [command]
  (let [current-item (events/replay (store/find-with-id (:id command)))
        change-type (if (> (:price current-item) (:price command)) :item-price-decreased :item-price-increased)]
    (store/save (with-type (events/create-from command change-type)))))

(def handlers {
               :add-item add-item-handler
               :change-item-price change-price-handler })
