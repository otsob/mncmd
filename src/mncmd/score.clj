(ns mncmd.score
  (:import [java.nio.file Paths]
           [org.wmn4j.notation Note Chord Rest]
           [org.wmn4j.io.musicxml MusicXmlReader])
  (:gen-class))

(defn read-score [path]
  ;; The into-array trick is used to just force the String param overload of Paths.get
  (let [path-obj (Paths/get path (into-array [""]))
        abs-path (.toAbsolutePath path-obj)]
    (with-open [reader (MusicXmlReader/readerFor abs-path)]
      (.readScore reader))))

(defn- attribute-value [score attribute]
  (let [attr-opt (.getAttribute score attribute)]
    (if (.isPresent attr-opt)
      (.get attr-opt)
      "")))

(defn score-attributes [score]
  (hash-map ::title (attribute-value score org.wmn4j.notation.Score$Attribute/TITLE)
            ::movement-title (attribute-value score org.wmn4j.notation.Score$Attribute/MOVEMENT_TITLE)
            ::composer (attribute-value score org.wmn4j.notation.Score$Attribute/COMPOSER)
            ::arranger (attribute-value score org.wmn4j.notation.Score$Attribute/ARRANGER)))

(defn score->seq [score]
  (iterator-seq (.partwiseIterator score)))

(defn- chords [dur-seq]
  (filter #(instance? Chord %) dur-seq))

(defn- notes [dur-seq]
  (filter #(instance? Note %) dur-seq))

(defn- rests [dur-seq]
  (filter #(instance? Rest %) dur-seq))

(defn- note-count [dur-seq]
  (+ (count (notes dur-seq))
     (apply + (map #(.getNoteCount %) (chords dur-seq)))))

(defn score-counts [score]
  (hash-map
   ::part-count (.getPartCount score)
   ::measure-count (.getFullMeasureCount score)
   ::has-pickup (.hasPickupMeasure score)
   ::note-count (note-count (score->seq score))
   ::rest-count (count (rests (score->seq score)))))

(defn- part->dur-seq [part]
  (let [measure-seq (iterator-seq (.iterator part))]
    (mapcat #(iterator-seq (.iterator %)) measure-seq)))

(defn part-counts [part]
  (let [dur-seq (part->dur-seq part)]
    (hash-map
     ::note-count (note-count dur-seq)
     ::rest-count (count (rests dur-seq)))))

(defn part-attributes [part]
  {::part-name (.orElse (.getName part) nil)
   ::abbr-name (.orElse (.getAttribute part org.wmn4j.notation.Part$Attribute/ABBREVIATED_NAME) nil)})

(defn parts [score]
  (iterator-seq (.iterator score)))

(defn all-part-attributes [score]
  (map part-attributes (parts score)))

(defn all-part-counts [score]
  (map part-counts (parts score)))
