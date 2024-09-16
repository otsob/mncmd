(ns mncmd.visual
  (:import [org.wmn4j.notation PitchClass])
  (:require [pp-grid.api :as g])
  (:gen-class))

(def ^:private pitch-classes (PitchClass/values))

(def ^:private pc-labels
  {PitchClass/C            "C"
   PitchClass/CSHARP_DFLAT "C#/Db"
   PitchClass/D            "D"
   PitchClass/DSHARP_EFLAT "D#/Eb"
   PitchClass/E            "E"
   PitchClass/F            "F"
   PitchClass/FSHARP_GFLAT "F#/Gb"
   PitchClass/G            "G"
   PitchClass/GSHARP_AFLAT "G#/Ab"
   PitchClass/A            "A"
   PitchClass/ASHARP_BFLAT "A#/Bb"
   PitchClass/B            "B"})

(defn plot-chroma [chromagram]
  (let [values (map #(.getValue chromagram %) pitch-classes)
        labels (map #(pc-labels %) pitch-classes)]
    (g/chart-bar values :horizontal false :labels labels)))
