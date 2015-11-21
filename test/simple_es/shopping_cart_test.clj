(ns simple-es.shopping-cart-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [taoensso.timbre :as timbre]
            [simple-es.event :refer :all]
            [simple-es.command :as command]
            [simple-es.event-store :as events]
            [clojure.test.check.generators :as gen]))

;; Thinking out loud with code...
;; Need a problem to solve :P
(defn create-item []
  (gen/generate
    (gen/hash-map
      :id gen/uuid
      :description gen/string-ascii
      :price gen/nat)))

(defn add-to-cart [item]
  (command/create :add-item-to-cart item))

(defn add-item-to-cart-handler [command]
  (let [event (events/store
                (assoc (second command)
                       :fact :item-added-to-cart
                       :timestamp (tc/to-long (time/now))))]
    (timbre/info "event(" (:fact event) ") tid(" (:transaction_id event) ")")
    ))


(testing "shopping hour"
  (deftest add-things-to-cart
    (let [things (take (gen/generate gen/nat) (repeatedly create-item))
          items-to-add (map #(add-to-cart %) things)
          added-items (map #(add-item-to-cart-handler %) items-to-add)]
      (apply println added-items))
  ))

