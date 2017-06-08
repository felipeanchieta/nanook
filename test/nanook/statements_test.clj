(ns nanook.statements-test
  "This is the namespace that tests HTTP endpoints for statements requests"
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
  [amount acc-number timestamp handler]
  (let [response (handler (-> (mock/request
                               :post
                               (format "/accounts/%s/credit" acc-number)
                               (json/generate-string {:amount      amount
                                                      :description "Salary"
                                                      :timestamp timestamp}))
                              (mock/content-type "application/json")))]
    (is (= (:status response) 200))))

(defn- do-debit
  "Helper function to get money from an account throught a handler"
  [amount acc-number timestamp handler]
  (let [response (handler (-> (mock/request
                               :post
                               (format "/accounts/%s/debit" acc-number)
                               (json/generate-string {:amount      amount
                                                      :description "Purchase"
                                                      :timestamp timestamp}))
                              (mock/content-type "application/json")))]
    (is (= (:status response) 200))))

(deftest statement-test
  (testing
   "The statement of a typical client"
    (do
      (put-credit 1000.0 "85126" "2017-01-08T11:51:18+00:00" app)
      (put-credit 1245.0 "85126" "2017-02-08T11:51:18+00:00" app)
      (put-credit 1345.0 "85126" "2017-02-08T15:00:18+00:00" app)
      (do-debit    400.0 "85126" "2017-03-08T11:51:18+00:00" app)
      (let [response (app (-> (mock/request
                               :post
                               "/accounts/85126/statement"
                               (json/generate-string {:from "01/02/2017"
                                                      :to "01/04/2017"}))
                              (mock/content-type "application/json")))
            body      (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (:statement body) [{:date "08/02/2017" :description "Salary" :amount 1245.0}
                                  {:date "08/02/2017" :description "Salary" :amount 1345.0}
                                  {:date "08/03/2017" :description "Purchase" :amount 400.0}]))))))
