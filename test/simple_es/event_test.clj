(ns simple-es.event-test
  (:require [clojure.test :refer :all]
            [simple-es.event :refer :all]
            [clojure.test.check.generators :as gen]))

(defn a-raw-event []
  (gen/generate (gen/hash-map :id gen/uuid :transaction_id gen/uuid :name gen/string-ascii :price gen/nat)))

(defn an-event-from [event]
  (create-from event (rand-nth [:happened-1 :happened-2 :happened-3])))

(testing "events replays"
  (deftest replays-a-list-of-events
    (let [predictable-events [{ :id "1" :name "one" :birth "1900-11-11" :last-name "two" :status "single" }
                              { :id "1" :birth "1966-11-11" }
                              { :id "1" :status "married" :last-name "three" }]]
      (is (= { :id "1" :name "one" :birth "1966-11-11" :last-name "three" :status "married" }
             (replay predictable-events))))))

(testing "event creation"
  (deftest creates-with-metadata
    (let [event (create-from { :f1 "v1" } :item-added)]
     (is (= :item-added (:fact event)))
     (is (instance? java.lang.Long (:timestamp event)))))

  (deftest stores-a-raw-event-and-returns-such
    (let [clean-store (clean)
          new-event (an-event-from (a-raw-event))]
      (is (= (store new-event) new-event))
      (is (= (all) [new-event])))))

(testing "events fetching"
  (deftest returns-events-for-given-id
    (let [clean-store (clean)
          events (take 5 (repeatedly (fn [] (store (an-event-from (a-raw-event))))))
          random-event (rand-nth events)]
      (is (= (given (:id random-event)) [random-event])))))
