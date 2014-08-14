(ns rontgen-test
  (:require [clojure.test :refer :all]
            [rontgen :refer :all]))

(deftest string-abuse-test
  (let [string "foo"
        char-array (char-array 3 [\b \a \r])]
    (is (= [\f \o \o] (-> string peer :value seq)))
    (is (= "bar" (-> string (bash {:value char-array}))))
    (is (= "bar" string))))
