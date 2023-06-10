(ns mncmd.stat
  (:require [mncmd.score :as score]
            [mncmd.harmony :as harmony]
            [mncmd.visual :as vis]
            [clojure.string :as str])
  (:gen-class))

(def ^:private indent-depth 24)

(defn- stat-row [attr-name attr-value]
  (let [whitespace (str/join (repeat (- indent-depth (count attr-name)) " "))]
    (if (str/blank? (str attr-value))
      ""
      (if (str/blank? attr-name)
        (str "\n" attr-value "\n")
        (str attr-name ":" whitespace attr-value "\n")))))

(defn- key-sig->str [key-sig]
  (when key-sig
    (str (str "Flats: " (.getFlats key-sig)) (str " Sharps: " (.getSharps key-sig)))))

(defn- time-sig->str [time-sig]
  (when time-sig
    (str (.getBeatCount time-sig) "/" (.getDenominator (.getBeatDuration time-sig)))))

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
       (stat-row "Highest pitch" (::score/highest part))
       (stat-row "Key signature" (key-sig->str (::score/key-signature part)))
       (stat-row "Time signature" (time-sig->str (::score/time-signature part)))
       (stat-row "Chromagram" (:chroma part))
       (stat-row "" (:chroma-plot part))))

(defn- most-common [elems]
  (->> elems
       frequencies
       (sort-by val)
       reverse
       (take 1)
       (map first)
       first))

(defn- most-common-time-sig [time-sig-maps]
  (let [time-sigs (map ::score/time-signature time-sig-maps)]
    (most-common time-sigs)))

(defn time-signature-stat [score]
  (let [time-sigs (score/time-signatures score)]
    (str (stat-row "Time signature" (time-sig->str (most-common-time-sig time-sigs))))))

(defn- most-common-key-sig [key-sig-maps]
  (let [key-sigs (map ::score/key-signature key-sig-maps)]
    (most-common key-sigs)))

(defn- key-signature-stat [score]
  (let [key-sigs (score/key-signatures score)]
    (str (stat-row "Key signature" (key-sig->str (most-common-key-sig key-sigs))))))

(defn- part-attribute-maps [score args]
  (filter not-empty
          [(score/all-part-attributes score)
           (when (:counts args) (score/all-part-counts score))
           (when (:ambitus args) (score/part-ambitus score))
           (when (:key-signature args) (score/key-signatures score))
           (when (:time-signature args) (score/time-signatures score))
           (when (:chroma args) (map #(hash-map :chroma %) (harmony/part-chromagrams score)))
           (when (:chroma-plot args) (map #(hash-map :chroma-plot (vis/plot-chroma %)) (harmony/part-chromagrams score)))]))

(defn- all-part-stats [score args]
  (let [part-attr (map #(apply merge %)
                       (apply map vector (part-attribute-maps score args)))]
    (str/join "\n" (map part-stat part-attr))))

(defn- chromagram-stat [score]
  (let [chroma (harmony/score->chromagram score)]
    (stat-row "Chromagram" chroma)))

(defn- print-score-stat [path args]
  (if-let [score (score/read-score path (:skip-validation args))]
    (do
      (print (stat-row "Score path" path))
      (print (basic-stat score))
      (when (:counts args)
        (print (count-stat score)))
      (when (:ambitus args)
        (print (ambitus-stat score)))
      (when (:key-signature args)
        (print (key-signature-stat score)))
      (when (:time-signature args)
        (print (time-signature-stat score)))
      (when (:chroma args)
        (print (chromagram-stat score)))
      (when (:chroma-plot args)
        (print (vis/plot-chroma (harmony/score->chromagram score))))
      (when (:parts args)
        (println "\n=== Parts ===")
        (print (all-part-stats score args)))
      (println))
    (System/exit 1)))

(defn print-stats [args]
  (run! #(print-score-stat % args) (:_arguments args)))
