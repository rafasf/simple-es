(ns simple-es.command-test
  (:require [clojure.test :refer :all]
            [simple-es.command :as command]))

(def fake-store (atom []))
(def handlers
  { :create-item (fn [command] (swap! fake-store conj command))
    :other (fn [command] (swap! fake-store conj "fail")) })

(testing "command execution and effects"
  (deftest executes-command
    (let [command (command/create :create-item { :name "cake" :price 3.69 })]
      (command/execute command handlers)
      (is (= command (first @fake-store)))))

  (deftest ignores-command-if-handler-does-not-exist
    (let [command (command/create "some-command" {})
          ignored (command/execute command handlers)]
      (is (= nil ignored)))))

(testing "command creation"
  (deftest create-with-additional-info
    (let [command (command/create "do-something" { :field_1 "value 1" :field_2 "value 2" })]
      (is (= :do-something (first command)))
      (is (instance? java.util.UUID (:id (second command))))
      (is (instance? java.util.UUID (:transaction_id (second command)))))))

(testing "handler"
  (deftest creates-handler
    (let [handle (command/build-handler handlers)
          handled (handle (command/create :other { :description "blah" }))]
      (is (= :other (first handled))))))
