(ns nanook.handler
  "This namespace is responsible for handling the HTTP requests of
   Nanook, defining further actions regarding their URIs"
  (:use [compojure.core]
        [nanook.facade])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clova.core :as clova]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [resource-response response status]]
            [ring.adapter.jetty :refer (run-jetty)])
  (:gen-class))

(def credit-validation
  (clova/validation-set [:amount clova/required? clova/numeric? clova/positive?
                         :description clova/required? clova/stringy? [clova/longer? 5] [clova/shorter? 255]]))

(def debit-validation credit-validation)

(defroutes nanook-routes
  (POST "/accounts/:acc-number{[0-9]{5}}/credit" request
    (let [validated (clova/validate credit-validation (:body request))]
      (if (:valid? validated)
        (response
         (if (contains? (:body request) :timestamp)
           (credit-op (:acc-number (:params request))
                      (:amount (:body request))
                      (:description (:body request))
                      (:timestamp (:body request)))

           (credit-op (:acc-number (:params request))
                      (:amount (:body request))
                      (:description (:body request)))))
        {:body "400 Bad Request" :status 400})))

  (POST "/accounts/:acc-number{[0-9]{5}}/debit" request
    (let [validated (clova/validate credit-validation (:body request))]
      (if (:valid? validated)
        (response
         (if (contains? (:body request) :timestamp)
           (debit-op (:acc-number (:params request))
                     (:amount (:body request))
                     (:description (:body request))
                     (:timestamp (:body request)))

           (debit-op (:acc-number (:params request))
                     (:amount (:body request))
                     (:description (:body request)))))
        {:body "400 Bad Request" :status 400})))

  (GET "/accounts/:acc-number{[0-9]{5}}/balance" request
    (response
     (get-total-balance (:acc-number (:params request)))))

  (route/not-found "Not Found"))

(def app
  (-> (handler/api nanook-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty app {:port 3000
                  :join? false}))
