(ns simple-es.inventory-test
  (:require [clojure.test :refer :all]
            [simple-es.inventory :refer :all]
            [clj-uuid :as uuid]
            [simple-es.event :as events]))

(testing "inventory"
  (deftest adds-an-item
    (let [command (add (struct item (uuid/v1) "a description" 3.12))
          handled (add-item-handler command)
          added (events/given (:id (second command)))]
    (is (= [handled] added)))))
