(ns nanook.core-test
  (:require [clojure.test :refer :all]
            [nanook.core :refer :all]))

(deftest money-deposit-test
  (testing "The money deposit into an account"
    (is (= true (do-deposit "1234-5" 500.0 "Nubank" "13/04")))))
