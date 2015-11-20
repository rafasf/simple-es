(ns simple-es.event-store-test
  (:require [clojure.test :refer :all]
            [simple-es.event-store :refer :all]
            [clojure.test.check.generators :as gen]))

(defn an-event []
  (gen/generate (gen/hash-map
    :id gen/uuid
    :name gen/string-ascii
    :price gen/nat)))

(testing "event storage persistence"
  (deftest stores-an-event-and-returns-such
    (let [clean-store (clean)
          new-event (an-event)]
      (is (= (store new-event) new-event))
      (is (= (all) [new-event])))))

(testing "events fetching"
  (deftest returns-events-for-given-id
    (let [clean-store (clean)
          events (take 5 (repeatedly (fn [] (store (an-event)))))
          an-event (rand-nth events)]
      (is (= (given (:id an-event))
             [an-event])))))
