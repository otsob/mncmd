(ns mncmd.score-test
  (:require [clojure.test :refer :all]
            [mncmd.score :as score]))

(def score (score/read-score "test/resources/twoPartsAndMeasures.musicxml"))

(deftest test-attributes
  (let [attributes (score/score-attributes score)]
    (testing "Test score attributes"
      (is (= "Multistaff test file" (:movement-title attributes)))
      (is (= "TestFile Composer" (:composer attributes))))))

(deftest test-counts
  (let [counts (score/score-counts score)]
    (testing "Test counts"
      (is (= 2 (:part-count counts)))
      (is (= 2 (:measure-count counts)))
      (is (= 5 (:note-count counts)))
      (is (= 1 (:rest-count counts))))))

(deftest test-parts
  (let [parts (score/all-part-attributes score)]
    (testing "Test parts"
      (is (= 2 (count parts)))
      (is (= "Part1" (:name (nth parts 0))))
      (is (= "Part2" (:name (nth parts 1)))))))

(deftest test-part-counts
  (let [part-counts (score/all-part-counts score)
        part-one-counts (nth part-counts 0)
        part-two-counts (nth part-counts 1)]
    (testing "Test part counts"
      (is (= 3 (:note-count part-one-counts)))
      (is (= 1 (:rest-count part-one-counts)))
      (is (= 2 (:note-count part-two-counts)))
      (is (= 0 (:rest-count part-two-counts))))))
