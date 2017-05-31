(ns nanook.facade
  "The responsibility of this façade namespace is to create a layer of abstraction
   between clients, like a handler of REST methods, and a layer of core businesses
   that must be protected."
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
  "Puts money into an given account with an amount and a description"
  ([acc-number amount description] (operate :credit acc-number amount description (get-current-timestamp)))
  ([acc-number amount description timestamp] (operate :credit acc-number amount description timestamp)))

(defn debit-op
  "Gets money into an given account with an amount and a description"
  ([acc-number amount description] (operate :debit acc-number amount description (get-current-timestamp)))
  ([acc-number amount description timestamp] (operate :debit acc-number amount description timestamp)))
