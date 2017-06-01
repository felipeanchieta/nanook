(ns nanook.core
  "This namespace contains the core businesses of Nanook
   which basically consists of a facts atom list that
   will store the checking accounts facts"
  (:use [nanook.utils]))

(def nanook-facts (atom {}))

(defn save-fact!
  "Atomically save a new fact to the nanook-facts map"
  [raw-fact]
  (let [final-fact (assoc raw-fact :uuid (generate-uuid))]
    (do
      (swap! nanook-facts merge {:account (:account final-fact) :fact final-fact})
      ;; TODO: change data model
      final-fact)))

(defn retrieve-fact
  "Atomically retrieve a fact from the nanook-facts map"
  [acc-number]
  ;; TODO: retrieve a fact by account
  (:fact @nanook-facts))

(defn retrieve-balance
  ;; TODO: remove and move to #3
  "Retrieve the balance between two dates, given an account"
  [acc-number start-timestamp end-timestamp]
  (assoc (retrieve-fact acc-number)
         :balance 0.0
         :start start-timestamp
         :end end-timestamp))

