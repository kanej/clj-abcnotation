(defproject clj-abcnotation "0.1.0-SNAPSHOT"
  :description "ABC Notation library"
  :url "http://github.com/kanej/clj-abcnotation"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [instaparse "1.1.0"]
                 [overtone "0.9.1"]
                 [leipzig "0.8.1"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]]}})
