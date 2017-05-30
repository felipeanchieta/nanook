(ns nanook.handler
  "This namespace is responsible for handling the HTTP requests of
   Nanook, defining further actions regarding their URIs"
  (:use [compojure.core]
        [nanook.facade])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [resource-response response status]]
            [ring.adapter.jetty :refer (run-jetty)])
  (:gen-class))

(defroutes nanook-routes
  (POST "/accounts/:acc-number{[0-9]{5}}/deposit" request
    (response
     (do-deposit
      (:acc-number (:params request))
      (:body request))))

  (POST "/accounts/:acc-number{[0-9]{5}}/salary" request
    (response
      (pay-salary
        (:acc-number request)
        (:body request))))

  (POST "/accounts/:acc-number{[0-9]{5}}/purchase" request
    (response
      (make-purchase
        (:acc-number request)
        (:body request))))

  (POST "/accounts/:acc-number{[0-9]{5}}/withdrawal" request
    (response
      (do-withdrawal
        (:acc-number request)
        (:body request))))

  (POST "/accounts/create" request
    (response
      (create-account
        (:body request))))

  (PUT "/accounts/delete/:account-number{[0-9]{5}}" request
    (response
      (delete-account
        (:acc-number request)
        (:body request))))

  (PUT "/accounts/:account-number{[0-9]{5}}/update-credit" request
    (response
      (update-credit
        0
        request)))

  (route/not-found "Not Found"))

(def app
  (-> (handler/api nanook-routes)
      (middleware/wrap-json-body {:keywords? true}) 
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty app {:port 3000
                  :join? false}))
