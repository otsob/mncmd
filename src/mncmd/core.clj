(ns mncmd.core
  (:require [cli-matic.core :refer [run-cmd]]
            [mncmd.stat :as stat])
  (:gen-class))

(def app-config
  {:app         {:command     "mncmd"
                 :description "Command line tool for extracting information from music notation files "
                 :version     "0.1.1"}
   :commands    [{:command     "stat"
                  :description "Shows file information for the music notation files at given paths"
                  :opts        [{:as     "Prints out counts of notation elements."
                                 :option "counts"
                                 :type   :with-flag}
                                {:as     "Apply stat to all parts in a score separately."
                                 :option "parts"
                                 :type   :with-flag}
                                {:as     "Prints the ambitus of the score (and parts with --parts option)."
                                 :option "ambitus"
                                 :type   :with-flag}
                                {:as     "Prints the first key signature of the score (and parts with --parts option).
                                          In case of parts having different key signature, prints the most common."
                                 :option "key-signature"
                                 :type   :with-flag}
                                {:as     "Prints the first time signature of the score (and parts with --parts option).
                                          In case of parts having different time signatures, prints the most common."
                                 :option "time-signature"
                                 :type   :with-flag}
                                {:as      "Prints the chromagram (weighted by duration) of the score (and parts with --parts option)."
                                 :option  "chroma"
                                 :type    :with-flag}
                                {:as      "Prints a chromagram plot (weighted by duration) of the score (and parts with --parts option)."
                                 :option  "chroma-plot"
                                 :type    :with-flag}]
                  :runs        stat/print-stats}]})

(defn -main
  [& args]
  (run-cmd args app-config))

