(ns mncmd.score-test
  (:require [clojure.test :refer :all]
            [mncmd.score :as score]))

(def score (score/read-score "test/resources/twoPartsAndMeasures.musicxml"))

(deftest test-attributes
  (let [attributes (score/attributes score)]
    (testing "Test score attributes"
      (is (= "Multistaff test file" (:movement-title attributes)))
      (is (= "TestFile Composer" (:composer attributes))))))

(deftest test-counts
  (let [counts (score/counts score)]
    (testing "Test counts"
      (is (= 2 (:part-count counts)))
      (is (= 2 (:measure-count counts)))
      (is (= 5 (:note-count counts)))
      (is (= 1 (:rest-count counts))))))
