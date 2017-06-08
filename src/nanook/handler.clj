(ns nanook.handler
  "This namespace is responsible for handling the HTTP requests of
   Nanook, defining further actions regarding their URIs"
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clova.core :as clova]
            [nanook.facade :refer :all]
            [nanook.validation :refer :all]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [resource-response response status]]
            [ring.adapter.jetty :refer (run-jetty)])
  (:gen-class))

(defroutes nanook-routes
  (POST "/accounts/:acc-number{[0-9]{5}}/credit" request
    (let [validated (clova/validate credit-validation (:body request))]
      ;(println request)
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
    (let [validated (clova/validate debit-validation (:body request))]
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
     (get-balance (:acc-number (:params request)))))

  (POST "/accounts/:acc-number{[0-9]{5}}/statement" request
    (let [validated (clova/validate statement-validation (:body request))]
      (if (:valid? validated)
        (response
         (get-statement (:acc-number (:params request))
                        (:from (:body request))
                        (:to (:body request))))
        {:body "400 Bad Request" :status 400})))

  (GET "/accounts/:acc-number{[0-9]{5}}/debt-periods" request
    (response
     (get-debt-periods (:acc-number (:param request)))))

  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults nanook-routes api-defaults)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty app {:port 3000
                  :join? false}))
