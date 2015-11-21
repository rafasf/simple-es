(ns simple-es.event
  (:require [simple-es.event-store :as events]))

(defn reconstruct-from [events aggregate]
  (if (empty? events)
    aggregate
    (recur (rest events) (merge aggregate (first events)))))

(defn replay [events]
  (reconstruct-from (rest events) (first events)))
