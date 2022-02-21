(ns mncmd.stat
  (:require [mncmd.score :as score]
            [clojure.string :as str])
  (:gen-class))

(def ^:private indent-depth 24)

(defn- stat-row [attr-name attr-value]
  (let [whitespace (str/join (repeat (- indent-depth (count attr-name)) " "))]
    (if (str/blank? (str attr-value))
      ""
      (str attr-name ":" whitespace attr-value "\n"))))

(defn- basic-stat [score]
  (let [score-attr (score/attributes score)]
    (str (stat-row "Title" (:title score-attr))
         (stat-row "Movement-title" (:movement-title score-attr))
         (stat-row "Composer" (:composer score-attr))
         (stat-row "Arranger" (:arranger score-attr)))))


(defn- count-stat [score]
  (let [counts (score/counts score)]
    (str (stat-row "Part count" (:part-count counts))
         (stat-row "Measure count" (:measure-count counts))
         (stat-row "Note count" (:note-count counts)))))


(defn print-stat [args]
  (let [path (get (args :_arguments) 0)
        score (score/read-score path)]
    (do
      (print (stat-row "Score path" path))
      (print (basic-stat score))
      (when (:counts args)
        (print (count-stat score)))
      (println))))
