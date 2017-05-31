(ns nanook.utils
  "This namespace defines functions that are considered
   useful to other namespaces in Nanook, gathering utilities
   together in one file"
  (:require [clj-time.local :as l]))


(def account-pattern #"[0-9]{5}")


(defn get-current-timestamp
  "Returns the current time from the system"
  []
  (str (l/local-now)))


(defn generate-uuid
  "Returns a random UUID as a string"
  []
  (str (java.util.UUID/randomUUID)))


(defn valid-account-number?
  "Validate an account number that should be a number
   consisting of 5 integers, 0-9, no longer, no shorter."
   [acc-number]
   (= (re-matches account-pattern acc-number) acc-number))


(defn valid-amount?
  "Validate a given amount of money, which should be
   a positive Double"
   [amount]
   (and (= java.lang.Double (type amount))
        (>= amount 0.0)))

;; TODO: valid-timestamp?


;;; TODO: move to future branch
;;; is this here?
(defn compute-debit-periods
  "Compute periods of debit"
  [acc-number])

