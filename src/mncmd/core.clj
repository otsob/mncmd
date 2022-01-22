(ns mncmd.core
  (:require [cli-matic.core :refer [run-cmd]]
            [mncmd.stat :as stat])
  (:gen-class))


(def app-config
  {:app         {:command     "mncmd"
                 :description "Command line tool for extracting information from music notation files "
                 :version     "0.1"}
   :commands    [{:command     "stat"
                  :description ["Shows file information for a music notation file"
                                ""
                                ""]
                  :opts        []
                  :runs        stat/print-stat}]})

(defn -main
  [& args]
  (run-cmd args app-config))
