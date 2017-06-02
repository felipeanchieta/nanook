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
  (handler (-> (mock/request
                :post
                (format "/accounts/%s/credit" acc-number)
                (json/generate-string {:amount      1000.0
                                       :description "Salary"})))))
(defn- get-credit ; todo: do-debit
  "Helper function to get money from an account throught a handler"
  [acc-number handler]
  (handler (-> (mock/request
                :post
                (format "/accounts/%s/debit" acc-number)
                (json/generate-string {:amount      1000.0
                                       :description "Salary"})))))

(deftest balance-tests
  (testing
   "The balance of a client that has only incomes"
    (do
      (dotimes [_ 3] (put-credit "12345" app))
      (let [response (app (mock/request
                           :get
                           "/accounts/12345/balance"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (float (:balance body)) 3000.0)))))

  (testing
   "The balance of a client that has only debts like mine"
    (do
      (dotimes [_ 3] (get-credit "12345" app))
      (let [response (app (mock/request
                           :get
                           "/accounts/12345/balance"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (float (:balance body)) 3000.0)))))

  (testing
   "The balance of a client that is doing well, with some credits
       and some debits"
    (do
      (dotimes [_ 3] (put-credit "12345" app))
      (dotimes [_ 2] (get-credit "12345" app))
      (let [response (app (mock/request
                           :get
                           "/accounts/12345/balance"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (float (:balance body)) 3000.0))))))
