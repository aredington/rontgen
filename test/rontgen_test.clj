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
    (is (= [:serialPersistentFields
            :ANNOTATION
            :reflectionFactory
            :allPermDomain
            :SYNTHETIC
            :useCaches
            :serialVersionUID
            :initted
            :ENUM] (keys result)))
    (is (= {:ANNOTATION 8192
            :allPermDomain nil
            :SYNTHETIC 4096
            :useCaches true
            :serialVersionUID 3206093459760846163
            :initted true
            :ENUM 16384}
           (select-keys result [:ANNOTATION
                                :allPermDomain
                                :SYNTHETIC
                                :useCaches
                                :serialVersionUID
                                :initted
                                :ENUM])))
    (is (thrown? clojure.lang.ExceptionInfo (bash String {:useCaches false})))))
