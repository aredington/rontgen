(ns rontgen-test
  (:require [clojure.test :refer :all]
            [rontgen :refer :all]))

(deftest string-abuse-test
  (let [string "foo"
        char-array (char-array 3 [\b \a \r])]
    (is (= [\f \o \o] (-> string peer :value seq)))
    (is (= "bar" (-> string (bash {:value char-array}))))
    (is (= "bar" string))))

(deftest class-support
  (let [result (peer String)]
    (is (= [:serialVersionUID
            :serialPersistentFields
            :CASE_INSENSITIVE_ORDER
            :HASHING_SEED] (keys result)))
    (is (= {:serialVersionUID -6849794470754667710}
           (select-keys result [:serialVersionUID])))
    (is (thrown? clojure.lang.ExceptionInfo (bash String {:useCaches false})))))

(deftest dangerous-class-support
  (let [result (peer clojure.lang.RT)]
    (is (= :file (:FILE_KEY result)))
    (bash clojure.lang.RT {:FILE_KEY :directory})
    (is (= :directory (:FILE_KEY (peer clojure.lang.RT))))
    (bash clojure.lang.RT {:FILE_KEY :file})))
