(ns nanook.statements-test
  "This is the namespace that test HTTP endpoints for statements requests"
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

(deftest statement-test
  (testing
   "The statement of a typical client"
    (do
      (put-credit "12345" app) ;; add timestamp
      (put-credit "12345" app) ;; add timestamp
      (do-debit  "12345" app)
      (let [response (app (-> (mock/request
                               :post
                               "/accounts/12345/statement"
                               (json/generate-string {:from "10/10/1995"
                                                      :to "01/09/2017"}))
                              (mock/content-type "application/json")))
            body      (parse-body (:body response))]
        (is (= (:status response) 200))))))
; implement body
