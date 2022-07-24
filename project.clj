(defproject mncmd "0.1.0"
  :description "Command line tool for extracting information from music notation files"
  :url "https://github.com/otsob/mncmd"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.wmn4j/wmn4j "0.6.0"]
                 [org.slf4j/slf4j-jdk14 "1.7.36"]
                 [cli-matic "0.4.2"]]
  :main mncmd.core
  :repl-options {:init-ns mncmd.core}
  :dev {:resource-paths ["test/resources"]}
  :plugins [[com.github.clj-kondo/lein-clj-kondo "0.1.3"]])
