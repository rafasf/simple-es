(ns simple-es.command
  (:require [clj-uuid :as uuid]
            [taoensso.timbre :as timbre]))

(defn execute [command handlers]
  (if (contains? handlers (first command))
    (let [command-name (first command)
          handle (command-name handlers)]
      (timbre/info "command(" (first command) ") tid(" (:transaction_id (second command)) ")")
      (handle command)
      command)
    nil))

(defn build-handler [handlers]
  (fn [command] (execute command handlers)))

(defn create [name body]
  [(keyword name) (assoc body :id (uuid/v1) :transaction_id (uuid/v4))])
