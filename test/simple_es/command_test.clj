(ns simple-es.command-test
  (:require [clojure.test :refer :all]
            [simple-es.event :as events]
            [simple-es.event-store :as es]
            [simple-es.command :as commands]
            [clojure.test.check.generators :as gen]))

(testing "command execution and effects"
  (deftest executes-command
    (let [handlers [(fn [c] (es/store (assoc c :fact "item-created")))]
          executed (commands/execute [:create-item { :name "cake" :price 3.69 }] handlers)
          id (:id executed)
          tid (:tid executed)]
      (is (= (events/replay (es/given (:id executed)))
             { :id id :tid tid :name "cake" :price 3.69 :fact "item-created"}))))

  )

