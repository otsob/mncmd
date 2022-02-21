(ns mncmd.score
  (:import [java.nio.file Paths]
           [java.net URI]
           [java.util Optional]
           [org.wmn4j.notation Score Note Chord Rest]
           [org.wmn4j.io.musicxml MusicXmlReader])
  (:gen-class))

;; TODO: Ensure that the paths are handled correctly (problems with whitespaces etc.
(defn read-score [path]
  (let [path-obj (java.nio.file.Paths/get (new java.net.URI (str "file://" path)))
        reader (org.wmn4j.io.musicxml.MusicXmlReader/readerFor path-obj)]
    (.readScore reader)))

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

