(ns simple-es.command-test
  (:require [clojure.test :refer :all]
            [simple-es.command :as command]))

(def fake-store (atom []))
(def handlers
  { :create-item (fn [command] (swap! fake-store conj command))
    :other (fn [command] (swap! fake-store conj { :id "meh" :f_2 "fail" })) })

(testing "execution"
  (deftest executes-command
    (let [command (command/create :create-item { :name "cake" :price 3.69 })
          payload (second command)]
      (command/execute command handlers)
      (is (= [payload]
             (filter #(= (:id payload) (:id %)) @fake-store)))))

  (deftest ignores-command-if-handler-does-not-exist
    (let [command (command/create "some-command" {})
          ignored (command/execute command handlers)]
      (is (= nil ignored)))))

(testing "command creation"
  (deftest create-with-additional-info
    (let [[command payload] (command/create "do-something" { :field_1 "value 1" :field_2 "value 2" })]
      (is (= :do-something command))
      (is (instance? java.util.UUID (:id payload)))
      (is (instance? java.util.UUID (:transaction_id payload))))))

(testing "issuer"
  (deftest builds-issuer-considering-many-handlers
    (let [issuer (command/build-issuer-considering handlers)
          [command-name payload] (issuer (command/create :other { :description "blah" }))]
      (is (= :other command-name)))))
