(defproject simple-es "0.1.0-SNAPSHOT"
  :description "Explore the parts of event source and commands"
  :url "https://github.com/rafasf/simple-es"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/test.check "0.9.0"]
                 [clj-time "0.11.0"]
                 [com.taoensso/timbre "4.1.4"]
                 [danlentz/clj-uuid "0.1.6"]]
  :main simple-es.main)
