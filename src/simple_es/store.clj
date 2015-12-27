(ns simple-es.store
  (:require [taoensso.timbre :as timbre]))

(def all-things (atom []))

(defn save [a-thing]
  (swap! all-things conj a-thing)
  (timbre/info "fact(" (:fact a-thing) ") tid(" (:transaction_id a-thing) ")")
  a-thing)

(defn find-with-id [id]
  (filter #(= id (:id %)) @all-things))

(defn find-with-item-id [id]
  (filter #(= id (:item-id %)) @all-things))

(defn all [] @all-things)

(defn clean []
  (reset! all-things []))

