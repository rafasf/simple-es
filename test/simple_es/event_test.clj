(ns simple-es.event-test
  (:require [clojure.test :refer :all]
            [simple-es.event :refer :all]
            [clojure.test.check.generators :as gen]))

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
     (is (instance? java.lang.Long (:timestamp event))))))
