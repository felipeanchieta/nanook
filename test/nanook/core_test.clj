(ns nanook.core-test
  (:require [cheshire.core :as json] 
            [clojure.test :refer :all]
            [nanook.handler :refer :all]
            [ring.mock.request :as mock]))

(defn- parse-body
   "Parses a request body into a map"
   [body]
   (json/parse-string body true))


(deftest credit-tests
  (testing 
    "Positive case: Credit on a valid account with valid amount"
    (let [response (app (-> (mock/request 
                              :post
                              "/accounts/12345/credit"
                              (json/generate-string {:amount 1000.0
                                                     :description "Salary"}))
                     (mock/content-type "application/json")))
          body (parse-body (:body response))]
      (is (= (:status response) 200))
      (is (= (float (:amount body)) 1000.0))
      (is (= (:description body) "Salary"))))

  (testing 
    "Postive case: Credit on a valid account with valid amount (w/ timestamp)"
    (let [response (app (-> (mock/request 
                              :post
                              "/accounts/12345/credit"
                              (json/generate-string {:amount 1000.0
                                                     :description "Salary"
                                                     :timestamp "2017-05-31T01:22:18.313-03:00"}))
                     (mock/content-type "application/json")))
          body (parse-body (:body response))]
      (is (= (:status response) 200))
      (is (= (float (:amount body)) 1000.0))
      (is (= (:timestamp body) "2017-05-31T01:22:18.313-03:00"))
      (is (= (:description body) "Salary"))))

  (testing 
    "Negative case: Credit on a invalid account (lower bound) with valid amount"
    (let [response (app (-> (mock/request 
                              :post
                              "/accounts/1234/credit"
                              (json/generate-string {:amount 1000.0
                                                     :description "Salary"}))
                     (mock/content-type "application/json")))]
      (is (= (:status response) 404))))

  (testing 
    "Negative case: Credit on a invalid account (upper bound) with valid amount"
    (let [response (app (-> (mock/request 
                              :post
                              "/accounts/123456/credit"
                              (json/generate-string {:amount 1000.0
                                                     :description "Salary"}))
                     (mock/content-type "application/json")))]
      (is (= (:status response) 404)))))


;;(deftest debit-tests
;;  (testing 
;;    "Positive case: Debit"
;;    (let [response (app (-> (mock/request 
;;                              :post
;;                              "/accounts/12345/debit"
;;                              (json/generate-string {:amount 1000.0
;;                                                     :description "Salary"}))
;;                     (mock/content-type "application/json")))
;;          body (parse-body (:body response))]
;;      (is (= (:status response) 200))
;;      (is (= (float (:amount body)) 1000.0))
;;      (is (= (:description body) "Salary"))))
;;
;;  (testing 
;;    "Postive case: Credit on a valid account with valid amount (w/ timestamp)"
;;    (let [response (app (-> (mock/request 
;;                              :post
;;                              "/accounts/12345/debit"
;;                              (json/generate-string {:amount 1000.0
;;                                                     :description "Salary"
;;                                                     :timestamp "2017-05-31T01:22:18.313-03:00"}))
;;                     (mock/content-type "application/json")))
;;          body (parse-body (:body response))]
;;      (is (= (:status response) 200))
;;      (is (= (float (:amount body)) 1000.0))
;;      (is (= (:timestamp body) "2017-05-31T01:22:18.313-03:00"))
;;      (is (= (:description body) "Salary"))))
;;
;;  (testing 
;;    "Negative case: Credit on a invalid account (lower bound) with valid amount"
;;    (let [response (app (-> (mock/request 
;;                              :post
;;                              "/accounts/1234/debit"
;;                              (json/generate-string {:amount 1000.0
;;                                                     :description "Salary"}))
;;                     (mock/content-type "application/json")))]
;;      (is (= (:status response) 404)))) ;; FIXME: should be another value
;;
;;  (testing 
;;    "Negative case: Credit on a invalid account (upper bound) with valid amount"
;;    (let [response (app (-> (mock/request 
;;                              :post
;;                              "/accounts/123456/debit"
;;                              (json/generate-string {:amount 1000.0
;;                                                     :description "Salary"}))
;;                     (mock/content-type "application/json")))]
;;      (is (= (:status response) 404))))) ;; FIXME: should be another value
