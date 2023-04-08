(ns mncmd.core
  (:require [cli-matic.core :refer [run-cmd]]
            [mncmd.stat :as stat]
            [mncmd.library :as lib])
  (:gen-class))

(def app-config
  {:app         {:command     "mncmd"
                 :description "Command line tool for extracting information from music notation files "
                 :version     "0.2.0"}
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
                  :runs        stat/print-stats}
                 {:command     "lib"
                  :description "Manage a library of MusicXML scores"
                  :subcommands [{:command       "create"
                                 :description   "Creates a library in the given path."
                                 :runs lib/create-library}
                                {:command       "add"
                                 :description   "Add files to library. Library path is first argument and the rest are the file paths."
                                 :runs lib/add-to-library}
                                {:command       "remove"
                                 :description   "Remove files from a library. Library path is first argument and the rest are the file paths."
                                 :runs lib/add-to-library}]}]})

(defn -main
  [& args]
  (run-cmd args app-config))

