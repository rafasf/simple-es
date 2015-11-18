(ns simple-es.core)

(defn reconstruct-from [events aggregate]
  (if (empty? events)
    aggregate
    (recur (rest events) (merge aggregate (first events)))))

(defn replay [events]
  (reconstruct-from (rest events) (first events)))

(defn events-for [aggregate-id events]
  (filter #(= aggregate-id (:id %)) events))

