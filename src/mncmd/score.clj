(ns mncmd.score
  (:import [java.nio.file Paths]
           [java.net URI]
           [java.util Optional]
           [org.wmn4j.notation Score Note Chord Rest]
           [org.wmn4j.io.musicxml MusicXmlReader])
  (:gen-class))

(defn read-score [path]
  ;; The into-array trick is used to just force the String param overload of Paths.get
  (let [path-obj (java.nio.file.Paths/get path (into-array [""]))
        abs-path (.toAbsolutePath path-obj)]
    (with-open [reader (org.wmn4j.io.musicxml.MusicXmlReader/readerFor abs-path)]
        (.readScore reader))))

(defn- attribute-value [score attribute]
  (let [attr-opt (.getAttribute score attribute)]
    (if (.isPresent attr-opt)
      (.get attr-opt)
      "")))

(defn- measure-count [score]
  (let [m (.getFullMeasureCount score)]
    (if (.hasPickupMeasure score)
      (str m " (+ pickup)")
      (str m))))

(defn attributes [score]
  (hash-map :title (attribute-value score org.wmn4j.notation.Score$Attribute/TITLE)
            :movement-title (attribute-value score org.wmn4j.notation.Score$Attribute/MOVEMENT_TITLE)
            :composer (attribute-value score org.wmn4j.notation.Score$Attribute/COMPOSER)
            :arranger (attribute-value score org.wmn4j.notation.Score$Attribute/ARRANGER)))

(defn score->seq [score]
  (iterator-seq (.partwiseIterator score)))

(defn- chords [dur-seq]
  (filter #(instance? org.wmn4j.notation.Chord %) dur-seq))

(defn- notes [dur-seq]
  (filter #(instance? org.wmn4j.notation.Note %) dur-seq))

(defn- note-count [score]
  (let [dur-seq (score->seq score)]
    (+ (count (notes dur-seq))
       (apply + (map #(.getNoteCount %) (chords dur-seq))))))

(defn counts [score]
  (hash-map
   :part-count (.getPartCount score)
   :measure-count (measure-count score)
   :note-count (note-count score)))

