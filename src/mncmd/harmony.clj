(ns mncmd.harmony
  (:import [org.wmn4j.analysis.harmony ChromagramBuilder])
  (:require [mncmd.score :as score])
  (:gen-class))

(defn- seq->chromagram [dur-seq]
  (let [builder (new ChromagramBuilder)
        add-to-builder (fn [builder durational]
                         (cond
                           (.isNote durational) (.add builder (.toNote durational))
                           (.isChord durational) (.add builder (.toChord durational))
                           :else builder))]
    (reduce add-to-builder
            builder
            dur-seq)
    (.build builder)))

(defn score->chromagram [score]
  (seq->chromagram (score/score->dur-seq score)))

(defn part-chromagrams [score]
  (map seq->chromagram (map score/part->dur-seq (score/parts score))))

