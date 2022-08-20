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
  (let [score-attr (score/score-attributes score)]
    (str (stat-row "Title" (::score/title score-attr))
         (stat-row "Movement-title" (::score/movement-title score-attr))
         (stat-row "Composer" (::score/composer score-attr))
         (stat-row "Arranger" (::score/arranger score-attr)))))

(defn- count-stat [score]
  (let [counts (score/score-counts score)]
    (str (stat-row "Part count" (::score/part-count counts))
         (stat-row "Measure count" (str (::score/measure-count counts)
                                        (if (::score/has-pickup counts)
                                          " (+ pickup)"
                                          "")))
         (stat-row "Note count" (::score/note-count counts))
         (stat-row "Rest count" (::score/rest-count counts)))))

(defn- ambitus-stat [score]
  (let [ambitus (score/score-ambitus score)]
    (str (stat-row "Lowest pitch" (::score/lowest ambitus))
         (stat-row "Highest pitch" (::score/highest ambitus)))))

(defn- part-stat [part]
  (str (stat-row "Name" (::score/part-name part))
       (stat-row "Abbreviated name" (::score/abbr-name part))
       (stat-row "Note count" (::score/note-count part))
       (stat-row "Rest count" (::score/rest-count part))
       (stat-row "Lowest pitch" (::score/lowest part))
       (stat-row "Highest pitch" (::score/highest part))))

(defn- part-attribute-maps [score args]
  (filter not-empty
          [(score/all-part-attributes score)
           (when (:counts args) (score/all-part-counts score))
           (when (:ambitus args) (score/part-ambitus score))]))

(defn- all-part-stats [score args]
  (let [part-attr (map #(apply merge %)
                       (apply map vector (part-attribute-maps score args)))]
    (str/join "\n" (map part-stat part-attr))))

(defn- print-score-stat [path args]
  (let [score (score/read-score path)]
    (print (stat-row "Score path" path))
    (print (basic-stat score))
    (when (:counts args)
      (print (count-stat score)))
    (when (:ambitus args)
      (print (ambitus-stat score)))
    (when (:parts args)
      (println "\n=== Parts ===")
      (print (all-part-stats score args)))
    (println)))

(defn print-stats [args]
  (run! #(print-score-stat % args) (:_arguments args)))
