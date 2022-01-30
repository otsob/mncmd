(ns mncmd.stat
  (:require [mncmd.score :as score]
            [clojure.string :as str])
  (:gen-class))

(def ^:private indent-depth 24)

(defn- stat-row [attr-name attr-value]
  (let [whitespace (str/join (repeat (- indent-depth (count attr-name)) " "))]
    (str attr-name ":" whitespace attr-value "\n")))

(defn- basic-stat [score]
  (let [score-attr (score/attributes score)]
    (str (stat-row "Title" (:title score-attr))
         (stat-row "Movement-title" (:movement-title score-attr))
         (stat-row "Part count" (:part-count score-attr))
         (stat-row "Measure count" (:measure-count score-attr)))))

(defn print-stat [args]
  (let [path (get (args :_arguments) 0)
        score (score/read-score path)]
    (do
      (print (stat-row "Score path" path))
      (print (basic-stat score))
      (println))))
