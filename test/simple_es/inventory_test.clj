(ns simple-es.inventory-test
  (:require [clojure.test :refer :all]
            [simple-es.inventory :refer :all]
            [clj-uuid :as uuid]
            [clojure.test.check.generators :as gen]
            [simple-es.store :as store]))

(defn an-item []
  (gen/generate (gen/hash-map :id gen/uuid :description gen/string-ascii :price gen/nat)))

(testing "new item"
  (deftest adds-an-item
    (let [added-item (add-item-handler (second (add (an-item))))
          item (first (store/find-with-id (:id added-item)))]
      (is (= added-item item))
      (is (= :inventory (:type item))))))

(testing "item price changes"
  (deftest marks-as-price-increase-if-new-value-is-higher
    (let [added-item (add-item-handler (second (add (an-item))))
          changed-item (change-price-handler (second (change-price (:id added-item) (+ (:price added-item) 1))))
          item (first (store/find-with-id (:id changed-item)))]
      (is (= :item-price-increased (:fact item)))
      (is (= :inventory (:type item)))))

  (deftest marks-as-price-decreased-if-new-value-is-lower
    (let [added-item (add-item-handler (second (add (an-item))))
          changed-item (change-price-handler (second (change-price (:id added-item) (- (:price added-item) 1))))
          item (first (store/find-with-id (:id changed-item)))]
      (is (= :item-price-decreased (:fact item)))
      (is (= :inventory (:type item))))))
