(ns simple-es.event
  (:require [taoensso.timbre :as timbre]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]))

;; fake store
(def all-events (atom []))

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

(defn given [aggregate-id]
  (filter #(= aggregate-id (:id %)) @all-events))

(defn store [event]
  (swap! all-events conj event)
  (timbre/info "event(" (:fact event) ") tid(" (:transaction_id event) ")")
  event)

(defn all []
  @all-events)

(defn clean []
  (reset! all-events []))
