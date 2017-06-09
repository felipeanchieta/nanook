(ns nanook.facade
  "The responsibility of this façade namespace is to create a layer of abstraction
   between clients, like a handler of REST methods, and a layer of core businesses
   that must be protected."
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]
            [nanook.core :refer :all]
            [nanook.utils :refer :all]))

(def human-friendly-format (f/formatter "dd/MM/yyyy"))

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
  (let [initial-timestamp (f/parse human-friendly-format initial-date)
        final-timestamp (f/parse human-friendly-format final-date)
        facts (retrieve-facts acc-number)]
    {:statement (into [] (for [fact facts
                               :let [fact-timestamp (l/to-local-date-time (:timestamp fact))]
                               :when (t/within?
                                      (t/interval initial-timestamp final-timestamp)
                                      fact-timestamp)]
                           {:date (f/unparse human-friendly-format fact-timestamp)
                            :description (:description fact)
                            :amount (:amount fact)}))}))

(defn- get-debt-periods-loop
  "Private loop for get-debt-periods"
  [facts]
  {:periods
   (into [] (loop [[fact & other-facts] facts
                   balance 0.0
                   in-debt? false
                   debt-periods '()
                   tmp-timestamp nil]
              (if (nil? fact)
                (if-not (nil? tmp-timestamp)
                  (conj debt-periods {:amount balance
                                      :start tmp-timestamp
                                      :end (get-current-timestamp)})
                  debt-periods)
                (let [new-balance (case (:operation fact)
                                    :credit (+ balance (:amount fact))
                                    :debit (- balance (:amount fact))
                                    balance)
                      new-in-debt? (< new-balance 0.0)
                      new-debt-periods (if (and (not new-in-debt?)
                                                (not (nil? tmp-timestamp)))
                                         (conj debt-periods {:amount balance
                                                             :start tmp-timestamp
                                                             :end (:timestamp fact)})
                                         debt-periods)
                      new-tmp-timestamp (if (and new-in-debt?
                                                 (nil? tmp-timestamp))
                                          (:timestamp fact)
                                          nil)]
                  (recur
                   other-facts
                   new-balance
                   new-in-debt?
                   new-debt-periods
                   new-tmp-timestamp)))))})

(defn get-debt-periods
  "Gets the periods of debt from a given account, so that Nanook can charge
   interest on that"
  [acc-number]
  (get-debt-periods-loop (retrieve-facts acc-number)))
