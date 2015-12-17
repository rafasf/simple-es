(ns simple-es.inventory-test
  (:require [clojure.test :refer :all]
            [simple-es.inventory :refer :all]
            [clj-uuid :as uuid]
            [simple-es.store :as store]))

(testing "inventory"
  (deftest adds-an-item
    (let [command (add (struct item (uuid/v1) "a description" 3.12))
          handled (add-item-handler command)
          added (store/find-with-id (:id (second command)))]
    (is (= [handled] added)))))
