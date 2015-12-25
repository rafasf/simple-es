(ns simple-es.main
  (:require [clj-uuid :as uuid]
            [simple-es.command :as commands]
            [simple-es.event :as events]
            [simple-es.inventory :as inventory]))

(def issue (commands/build-issuer-considering inventory/handlers))

(defn -main [& args]
  (let [{mug-id :id} (second (issue (inventory/add { :description "Mug" :price 3.12 })))
        {plate-id :id} (second (issue (inventory/add { :description "Plate" :price 14.99 })))]
    (issue (inventory/change-price plate-id 13.99))
    (issue (inventory/change-price plate-id 12.99))
    (issue (inventory/change-price plate-id 16.99))))
