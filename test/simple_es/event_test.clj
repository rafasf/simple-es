(ns simple-es.event-test
  (:require [clojure.test :refer :all]
            [simple-es.event :refer :all]
            [clojure.test.check.generators :as gen]))

(def predictable-events
  [{ :id "1" :name "one" :birth "1900-11-11" :last-name "two" :status "single" }
   { :id "1" :birth "1966-11-11" }
   { :id "1" :status "married" :last-name "three" }])

(deftest replay
  (is (= { :id "1" :name "one" :birth "1966-11-11" :last-name "three" :status "married" }
         (replay predictable-events))))

(deftest event-states
  (is (= [{ :id "1" :name "one" :birth "1900-11-11" :last-name "two" :status "single" }
          { :id "1" :name "one" :birth "1966-11-11" :last-name "two" :status "single" }
          { :id "1" :name "one" :birth "1966-11-11" :last-name "three" :status "married" }]
         (states-of predictable-events))))

(deftest creation
  (let [event (create-from { :f1 "v1" } :item-added)]
    (is (= :item-added (:_fact event)))
    (is (instance? java.lang.Long (:_timestamp event)))))
