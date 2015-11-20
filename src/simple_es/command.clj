(ns simple-es.command
  (:require [simple-es.event :as events]))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn execute [command handlers]
  (let [enhanced (assoc (second command) :id (uuid) :tid (uuid))]
    ((first handlers) enhanced)
    enhanced))
