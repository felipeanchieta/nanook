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
  (POST "/accounts/:acc-number{[0-9]{5}}/credit" request
    (response
      (if (contains? (:body request) :timestamp)
        (credit-op (:acc-number (:params request))
                   (:amount (:body request))
                   (:description (:body request))
                   (:timestamp (:body request)))

        (credit-op (:acc-number (:params request))
                   (:amount (:body request))
                   (:description (:body request))))))

  (POST "/accounts/:acc-number{[0-9]{5}}/debit" request
    (response
      (if (contains? (:body request) :timestamp)
        (debit-op (:acc-number (:params request))
                  (:amount (:body request))
                  (:description (:body request))
                  (:timestamp (:body request)))

        (debit-op (:acc-number (:params request))
                  (:amount (:body request))
                  (:description (:body request))))))

  ;;; TODO: move to future branch
  (GET "/accounts/:acc-number{[0-9]{5}}/balance" request
    (response
      nil))

  (GET "/accounts/:acc-number{[0-9]{5}}/statement" request
    (response
      nil))

  (GET "/accounts/:acc-number{[0-9]{5}}/debit-periods" request
    (response
      nil))

  (route/not-found "Not Found"))

(defn wrap-invalid-requests [handler]
  (fn [request]
    (if (coll? (:body request))
      (let [body (:body request)]
        (if (and (not (contains? body :amount))
                 (not (contains? body :description)))
          {:status 400
           :body body})))))

(def app
  (-> (handler/api nanook-routes)
      ;;(wrap-invalid-requests)
      (middleware/wrap-json-body {:keywords? true}) 
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty app {:port 3000
                  :join? false}))
