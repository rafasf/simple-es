(ns simple-es.store-test
  (:require [clojure.test :refer :all]
            [simple-es.store :refer :all]
            [clojure.test.check.generators :as gen]
            [clj-uuid :as uuid]))

(defn a-something []
  (gen/generate (gen/hash-map :id gen/uuid :name gen/string-ascii)))

(deftest storage
  (let [clean-store (clean)
        something {:id (uuid/v4) :description "blah"}]
    (save something)
    (is (= something (first (all))))))

(deftest finds-by-id
  (let [clean-store (clean)
        somethings (take 5 (repeatedly (fn [] (save (a-something)))))
        something-random (rand-nth somethings)]
    (is (= [something-random] (find-with-id (:id something-random))))))
