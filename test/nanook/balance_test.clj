(ns nanook.balance-test
  "This is the namespace that ests HTTP endpoints for
   balance operations, so the user can retrieve an
   account's balance at any time"
  (:require [cheshire.core :as json]
            [clojure.test :refer :all]
            [nanook.handler :refer :all]
            [ring.mock.request :as mock]))

(defn- parse-body
  "Parses a request body into a map"
  [body]
  (json/parse-string body true))

(defn- put-credit
  "Helper function to put some credit in an account throught a handler"
  [acc-number handler]
  (let [response (handler (-> (mock/request
                               :post
                               (format "/accounts/%s/credit" acc-number)
                               (json/generate-string {:amount      1000.0
                                                      :description "Salary"}))
                              (mock/content-type "application/json")))]
    (is (= (:status response) 200))))

(defn- do-debit
  "Helper function to get money from an account throught a handler"
  [acc-number handler]
  (let [response (handler (-> (mock/request
                               :post
                               (format "/accounts/%s/debit" acc-number)
                               (json/generate-string {:amount      1000.0
                                                      :description "Nubank Bill"}))
                              (mock/content-type "application/json")))]
    (is (= (:status response) 200))))

(deftest balance-tests
  (testing
   "The balance of a client that has only incomes"
    (do
      ;;; Puts 3000.0
      (dotimes [_ 3] (put-credit "12345" app))
      (let [response (app (mock/request
                           :get
                           "/accounts/12345/balance"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (float (:balance body)) 3000.0)))))

  (testing
   "The balance of a client that has only debts like me"
    (do
      ;;; Gets 3000.0 out of 0.0
      (dotimes [_ 3] (do-debit "23456" app))
      (let [response (app (mock/request
                           :get
                           "/accounts/23456/balance"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (float (:balance body)) -3000.0)))))

  (testing
   "The balance of a client that is doing well, with some credits
       and some debits"
    (do
      ;;; Puts 3000.0 and gets 2000.0
      (dotimes [_ 3] (put-credit "34567" app))
      (dotimes [_ 2] (do-debit "34567" app))
      (let [response (app (mock/request
                           :get
                           "/accounts/34567/balance"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (float (:balance body)) 1000.0))))))
