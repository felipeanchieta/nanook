(ns nanook.facade)
(use '(nanook.core))

(defn do-deposit
  "Deposit an amount of money into an account"
  [acc-number amount]
  (do-credit acc-number amount "Deposit" (now)))

(defn do-purchase
  "Deposit an amount of money into an account"
  [acc-number amount place]
  (do-debit acc-number amount (str "Purchase on " place) (now)))

(defn do-withdrawal
  "Deposit an amount of money into an account"
  [acc-number amount]
  (do-debit acc-number amount "Withdrawal" (now)))
