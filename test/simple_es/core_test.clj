(ns simple-es.core-test
  (:require [clojure.test :refer :all]
            [simple-es.core :refer :all]))

(with-test
  (def events
    [ { :name "one" :birth "1900-11-11" :last-name "two" :status "single" }
      { :birth "1966-11-11" }
      { :status "married" :last-name "three" } ])

  (is (= { :name "one" :birth "1966-11-11" :last-name "three" :status "married" }
         (replay events))))
