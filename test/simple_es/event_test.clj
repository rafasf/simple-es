(ns simple-es.event-test
  (:require [clojure.test :refer :all]
            [simple-es.event :refer :all]))

(def events
  [ { :id "1" :name "one" :birth "1900-11-11" :last-name "two" :status "single" }
    { :id "1" :birth "1966-11-11" }
    { :id "1" :status "married" :last-name "three" }
    { :id "2" :status "single" } ])

(defn events-for [id events]
  (filter #(= id (:id %)) events))

(testing "events"
  (deftest reconstructs-by-id
    (is (= { :id "1" :name "one" :birth "1966-11-11" :last-name "three" :status "married" }
           (replay (events-for "1" events))))))
