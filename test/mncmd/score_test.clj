(ns mncmd.score-test
  (:require [clojure.test :refer [deftest is testing]]
            [mncmd.score :as score])
  (:import [org.wmn4j.notation Pitch]))

(def score (score/read-score "test/resources/multistaff_test_file.musicxml" false))

(deftest test-attributes
  (let [attributes (score/score-attributes score)]
    (testing "Test score attributes"
      (is (= "Multistaff test file" (::score/movement-title attributes)))
      (is (= "TestFile Composer" (::score/composer attributes))))))

(deftest test-counts
  (let [counts (score/score-counts score)]
    (testing "Test counts"
      (is (= 3 (::score/part-count counts)))
      (is (= 2 (::score/measure-count counts)))
      (is (= 11 (::score/note-count counts)))
      (is (= 5 (::score/rest-count counts))))))

(deftest test-parts
  (let [parts (score/all-part-attributes score)]
    (testing "Test parts"
      (is (= 3 (count parts)))
      (is (= "Part1" (::score/part-name (nth parts 0))))
      (is (= "Part2" (::score/part-name (nth parts 1))))
      (is (= "Piano" (::score/part-name (nth parts 2))))
      (is (= "Pno." (::score/abbr-name (nth parts 2)))))))

(deftest test-part-counts
  (let [part-counts (score/all-part-counts score)
        part-one-counts (nth part-counts 0)
        part-two-counts (nth part-counts 1)
        part-three-counts (nth part-counts 2)]
    (testing "Test part counts"
      (is (= 5 (::score/note-count part-one-counts)))
      (is (= 1 (::score/rest-count part-one-counts)))
      (is (= 2 (::score/note-count part-two-counts)))
      (is (= 0 (::score/rest-count part-two-counts)))
      (is (= 4 (::score/note-count part-three-counts)))
      (is (= 4 (::score/rest-count part-three-counts))))))

(deftest test-score-ambitus
  (let [score-ambitus (score/score-ambitus score)]
    (testing "Test ambitus of score"
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/A org.wmn4j.notation.Pitch$Accidental/NATURAL 2)
             (::score/lowest score-ambitus)))
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/D org.wmn4j.notation.Pitch$Accidental/NATURAL 5)
             (::score/highest score-ambitus))))))

(deftest test-part-ambitus
  (let [part-ambitus (score/part-ambitus score)]
    (testing "Test ambitus of parts"
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/G org.wmn4j.notation.Pitch$Accidental/NATURAL 4)
             (::score/lowest (nth part-ambitus 0))))
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/D org.wmn4j.notation.Pitch$Accidental/NATURAL 5)
             (::score/highest (nth part-ambitus 0))))
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/G org.wmn4j.notation.Pitch$Accidental/NATURAL 3)
             (::score/lowest (nth part-ambitus 1))))
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/G org.wmn4j.notation.Pitch$Accidental/NATURAL 3)
             (::score/highest (nth part-ambitus 1))))
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/A org.wmn4j.notation.Pitch$Accidental/NATURAL 2)
             (::score/lowest (nth part-ambitus 2))))
      (is (= (Pitch/of org.wmn4j.notation.Pitch$Base/F org.wmn4j.notation.Pitch$Accidental/SHARP 4)
             (::score/highest (nth part-ambitus 2)))))))
