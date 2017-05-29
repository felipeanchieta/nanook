(ns nanook.core)

(defn do-credit
  "This operation inserts a given amount of money into an account
   with a description"
  [acc-number amount description date]
  (println " Acc #: "  acc-number
           " Amount: " amount
           " Descr.: " description
           " Date: "   date))

(defn do-debit
  "This operation takes away a given amount of money of an account
   with a description"
  [acc-number amount description date]
  (println " Acc #: "  acc-number
           " Amount: " (- amount)
           " Descr.: " description
           " Date: "   date))


