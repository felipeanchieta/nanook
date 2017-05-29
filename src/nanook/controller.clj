(ns nanook.controller
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [resource-response response status]]
            [ring.adapter.jetty :refer (run-jetty)])
  (:gen-class))

;; change this module to nanook.handler

(defn deposit [] nil)
(defn salary [] nil)
(defn purchase [] nil)
(defn withdrawal [] nil)
(defn create [] nil)
(defn delete [] nil)
(defn update-credit [] nil)

(defroutes credit-routes
  (POST "/accounts/:acc-number/deposit" []
        (response (deposit)))
  (POST "/accounts/:acc-number/salary" []
        (response (salary))))

(defroutes debit-routes
  (POST "/accounts/:acc-number/purchase" []
        (response (purchase)))
  (POST "/accounts/:acc-number/withdrawal" []
        (response (withdrawal))))

(defroutes operations-routes
  (POST "/accounts/create" []
        (response (create)))
  (DELETE "/accounts/delete/:account-number" []
          (response (delete)))
  (PUT "/accounts/:account-number/update-credit" []
       (response (update-credit))))

(defroutes nanook-routes
  ;;(credit-routes)
  ;;(debit-routes)
  ;;(operations-routes)
  (route/resources "/")
  (route/not-found "Not Found"))
  
(def app
  (-> (handler/api nanook-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty app {:port 3000 :join? false}))
