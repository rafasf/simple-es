(ns simple-es.core)

(defn has-keys [required-keys actual-keys]
  (let [actual-required-keys (filter #(.contains required-keys %) actual-keys)]
    (= required-keys actual-required-keys)))

(defn reconstruct-from [events aggregate]
  (if (empty? events)
    aggregate
    (recur (rest events) (merge aggregate (first events)))))

(defn replay [events]
  (reconstruct-from (rest events) (first events)))

(defn events-for [aggregate-id events]
  (filter #(= aggregate-id (:id %)) events))

(defn store-event [event events]
  (conj events event))
