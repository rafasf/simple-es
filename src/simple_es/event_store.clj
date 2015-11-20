(ns simple-es.event-store)

(def all-events (atom []))

(defn given [aggregate-id]
  (filter #(= aggregate-id (:id %)) @all-events))

(defn store [event]
  (swap! all-events conj event)
  event)

(defn all []
  @all-events)

(defn clean []
  (reset! all-events []))
