(ns simple-es.command
  (:require [clj-uuid :as uuid]
            [taoensso.timbre :as timbre]))

(defn has-handler-for [command-name handlers]
  (contains? handlers command-name))

(defn handle [payload command-name handle]
  (timbre/info "command=" command-name ", tid=" (:_transaction_id payload))
  (handle payload)
  [command-name payload])

(defn execute [command handlers]
  (let [[command-name payload] command]
    (if (has-handler-for command-name handlers)
      (handle payload command-name (command-name handlers))
      nil)))

(defn build-issuer-considering [handlers]
  (fn [command] (execute command handlers)))

(defn create [name body]
  (if (contains? body :id)
    [(keyword name) (assoc body :_transaction_id (uuid/v4))]
    [(keyword name) (assoc body :id (uuid/v1) :_transaction_id (uuid/v4))]))
