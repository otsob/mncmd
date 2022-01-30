(defproject mncmd "0.1.0-SNAPSHOT"
  :description "Command line tool for extracting information from music notation files"
  :url "https://github.com/otsob/mncmd"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.wmn4j/wmn4j "0.5.3"]
                 [cli-matic "0.4.2"]]
  :main mncmd.core
  :repl-options {:init-ns mncmd.core})
