(ns simple-es.main
  (:require [clj-uuid :as uuid]
            [simple-es.command :as commands]
            [simple-es.event :as events]
            [simple-es.inventory :as inventory]))

(def handlers {
               :add-item inventory/add-item-handler
               :change-item-price inventory/change-price-handler })

(defn -main [& args]
  (let [{mug-id :id} (second (commands/execute (inventory/add { :description "Mug" :price 3.12 }) handlers))
        {plate-id :id} (second (commands/execute (inventory/add { :description "Plate" :price 14.99 }) handlers))]
    (commands/execute (inventory/change-price plate-id 13.99) handlers)
    (commands/execute (inventory/change-price plate-id 12.99) handlers)
    (commands/execute (inventory/change-price plate-id 16.99) handlers)
    (clojure.pprint/pprint (events/all))))
