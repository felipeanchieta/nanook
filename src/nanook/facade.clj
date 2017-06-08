(ns nanook.facade
  "The responsibility of this façade namespace is to create a layer of abstraction
   between clients, like a handler of REST methods, and a layer of core businesses
   that must be protected."
  (:require [clj-time.format :as f]
            [nanook.core :refer :all]
            [nanook.utils :refer :all]))

(defn- operate
  "Do a banking operation like :credit or :debit"
  [operation acc-number amount description timestamp]
  (save-fact! {:operation operation
               :account acc-number
               :amount amount
               :timestamp timestamp
               :description description}))

(defn credit-op
  "Puts money into an given account with an amount and a description
   The timestamp attribute is optative"
  ([acc-number amount description]
   (operate :credit acc-number amount description (get-current-timestamp)))
  ([acc-number amount description timestamp]
   (operate :credit acc-number amount description timestamp)))

(defn debit-op
  "Gets money into an given account with an amount and a description
   The timestamp attribute is optative"
  ([acc-number amount description]
   (operate :debit acc-number amount description (get-current-timestamp)))
  ([acc-number amount description timestamp]
   (operate :debit acc-number amount description timestamp)))

(defn get-balance
  "Gets the total balance of an account from its facts register"
  [acc-number]
  (let [facts   (retrieve-facts acc-number)
        amounts (for [fact facts
                      :let [amount (:amount fact)]]
                  (case (:operation fact)
                    :credit amount
                    :debit  (- amount)
                    0.0))]
    {:balance (apply + amounts)}))

(defn get-statement
  "Gets the bank statement of a given account within a given period of time"
  [acc-number initial-date final-date]
  0)
;  (let [facts (retrieve-facts acc-number)]
;    (for [fact facts
;          :when CONDICAO]
;      {:balance 0.0})))
