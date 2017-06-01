(ns nanook.facade
  "The responsibility of this façade namespace is to create a layer of abstraction
   between clients, like a handler of REST methods, and a layer of core businesses
   that must be protected."
  (:require [clj-time.format :as f])
  (:use [nanook.core]
        [nanook.utils]))

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

(defn get-total-balance
  "Gets the total balance of an account from its
   very first day"
  [acc-number]
  (let [balance (:amount (retrieve-fact acc-number))]
    (if-not (nil? balance)
      {:balance balance}
      {:balance 0})))

(defn get-balance
  ;; TODO: remove and move to #3
  "Computes the current balance of a given account"
  [acc-number start-date end-date]
  (let [start-timestamp (str (f/parse (f/formatters :date) start-date))
        final-timestamp (str (f/parse (f/formatters :date) end-date))]
    (retrieve-balance acc-number start-timestamp final-timestamp)))
