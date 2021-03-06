(ns mncmd.core
  (:require [cli-matic.core :refer [run-cmd]]
            [mncmd.stat :as stat])
  (:gen-class))

(def app-config
  {:app         {:command     "mncmd"
                 :description "Command line tool for extracting information from music notation files "
                 :version     "0.1"}
   :commands    [{:command     "stat"
                  :description "Shows file information for a music notation file"
                  :opts        [{:as     "Prints out counts of notation elements."
                                 :option "counts"
                                 :type   :with-flag}
                                {:as     "Apply stat to all parts in a score separately."
                                 :option "parts"
                                 :type   :with-flag}]
                  :runs        stat/print-stat}]})

(defn -main
  [& args]
  (run-cmd args app-config))

