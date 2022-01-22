(ns mncmd.stat
  (:require [mncmd.score :as score])
  (:gen-class))


(defn print-stat [args]
  (let [path (get (args :_arguments) 0)
        score (score/read-score path)
        score-attr (score/attributes score)]
    (println score-attr)))
