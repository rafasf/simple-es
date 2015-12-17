(ns simple-es.main
  (:require [clj-uuid :as uuid]
            [simple-es.command :as commands]
            [simple-es.event :as events]
            [simple-es.store :as store]
            [simple-es.inventory :as inventory]))

(def handle (commands/build-handler inventory/handlers))

(defn -main [& args]
  (let [{mug-id :id} (second (handle (inventory/add { :description "Mug" :price 3.12 })))
        {plate-id :id} (second (handle (inventory/add { :description "Plate" :price 14.99 })))]
    (handle (inventory/change-price plate-id 13.99))
    (handle (inventory/change-price plate-id 12.99))
    (handle (inventory/change-price plate-id 16.99))
    (clojure.pprint/pprint (store/all))))
