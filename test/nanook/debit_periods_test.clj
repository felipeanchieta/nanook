(ns nanook.debit-periods-test
  "This is the namespace that tests HTTP endpoints for debit periods request"
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

(deftest debit-periods-test
  (testing
   "Just one period of debit"
    (do
      (put-credit 3000.0 "59373" "2017-01-08T11:51:18+00:00" app)
      (put-credit 3000.0 "59373" "2017-02-08T11:51:18+00:00" app)
      (do-debit   7000.0 "59373" "2017-02-09T11:51:18+00:00" app)
      (put-credit 2000.0 "59373" "2017-02-15T11:51:18+00:00" app)
      (let [response (app (mock/request
                           :get
                           "/accounts/59373/debit-periods"))
            body     (parse-body (:body response))]
        (is (= (:status response) 200))
        (is (= (:periods body) [{:amount 1000.0 :start "09/02/2017" :end "15/02/2017"}]))))))
