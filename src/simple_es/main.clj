(ns simple-es.main
  (:require [clj-uuid :as uuid]
            [simple-es.command :as commands]
            [simple-es.event :as events]
            [simple-es.store :as store]
            [simple-es.inventory :as inventory]))

(def issue (commands/build-issuer-considering inventory/handlers))

(defn open-store []
  (let [{mug-id :id} (second (issue (inventory/add { :description "Mug" :price 3.12 })))
        {plate-id :id} (second (issue (inventory/add { :description "Plate" :price 14.99 })))]
    (issue (inventory/change-price plate-id 13.99))
    (issue (inventory/change-price plate-id 12.99))
    (issue (inventory/change-price plate-id 16.99))
    [mug-id plate-id]))

(defn show-item-history [item-id]
  (let [item-events (store/find-with-id item-id)]
    (events/states-of item-events)))

(defn -main [& args]
  (let [[mug-id plate-id] (open-store)
        item-history (show-item-history plate-id)]
    (clojure.pprint/pprint item-history)))
