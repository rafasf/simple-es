(ns simple-es.event
  (:require [taoensso.timbre :as timbre]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]))

(defn reconstruct-from [events aggregate]
  (if (empty? events)
    aggregate
    (recur (rest events) (merge aggregate (first events)))))

(defn replay [events]
  (reconstruct-from (rest events) (first events)))

(defn create-from [action fact]
  (assoc action
         :fact fact
         :timestamp (tc/to-long (t/now))))

(defn states-of [events]
  (letfn [(all-states-of [events progression]
            (if (empty? events)
              progression
              (recur (rest events) (conj progression (merge (last progression) (first events))))))]
    (all-states-of events [])))
