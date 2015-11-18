(ns simple-es.core-test
  (:require [clojure.test :refer :all]
            [simple-es.core :refer :all]
            [clojure.test.check.generators :as gen]))

(def events
  [ { :id "1" :name "one" :birth "1900-11-11" :last-name "two" :status "single" }
    { :id "1" :birth "1966-11-11" }
    { :id "1" :status "married" :last-name "three" }
    { :id "2" :status "single" } ])

(testing "events"
  (deftest reconstructs-by-id
    (is (= { :id "1" :name "one" :birth "1966-11-11" :last-name "three" :status "married" }
           (replay (events-for "1" events)))))

  (deftest stores-an-event
    (let [id "1"
          new-event { :id id :name "blah" }
          this-events (store-event new-event [])]
      (is (= new-event
             (replay (events-for id this-events))))))

  )
