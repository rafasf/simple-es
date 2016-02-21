(ns simple-es.command-test
  (:require [clojure.test :refer :all]
            [simple-es.command :as command]))

(def fake-store (atom []))
(def handlers
  { :create-item (fn [command] (swap! fake-store conj command))
    :other (fn [command] (swap! fake-store conj { :id "meh" :f_2 "fail" })) })

(deftest execution
  (testing "executes a command"
    (let [command (command/create :create-item { :name "cake" :price 3.69 })
          payload (second command)]
      (command/execute command handlers)
      (is (= [payload]
             (filter #(= (:id payload) (:id %)) @fake-store)))))

  (testing "ignores command if no handler"
    (let [command (command/create "some-command" {})
          ignored (command/execute command handlers)]
      (is (= nil ignored)))))

(deftest creation
  (testing "create with id and transaction id if not provided"
    (let [[command payload] (command/create "do-something" { :field_1 "value 1" :field_2 "value 2" })]
      (is (= :do-something command))
      (is (instance? java.util.UUID (:id payload)))
      (is (instance? java.util.UUID (:_transaction_id payload)))))

  (testing "create with provided id"
    (let [[command payload] (command/create "do-something" {:id "provided" :field1 "value 1"})]
      (is (= "provided" (:id payload))))))

(deftest issuer
  (testing "create command issuer with given handlers"
    (let [issuer (command/build-issuer-considering handlers)
          [command-name payload] (issuer (command/create :other { :description "blah" }))]
      (is (= :other command-name)))))
