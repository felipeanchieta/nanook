(ns nanook.facade
  "The responsibility of this façade namespace is to create a layer of abstraction
   between clients, like a handler of REST methods, and a layer of core businesses
   that must be protected."
  (:use nanook.core))

;; Validators
;; ----------
;; These functions aim to validate the input args, in order
;; to protect the next layer of any kind of input errors

;; TODO: validate money!

(defn -valid-account-number?
  "Private function to validate an account number
   An account number should be a number consisting
   of 5 integers, 0-9; no longer, no shorter."
   [acc-number]
   (let [acc-pattern #"[0-9]{5}"]
       (= (re-seq acc-pattern acc-number) acc-number)))


(defn -valid-deposit?
  "Private function to validate the do-deposit args"
  [acc-number data]
  (and
    (-valid-account-number? acc-number)
    (contains? data :amount)))


(defn -valid-purchase?
  "Private function to validate the do-deposit args"
  [acc-number data]
  (and
    (contains? data :amount)
    (contains? data :store)
    (contains? data :location)))


(defn -valid-salary?
  "Private function to validate the pay-salary args"
  [acc-number data]
  (and
    (contains? {} nil)))


(defn -valid-withdrawal?
  "Private function to validate the do-withdrawal function"
  [acc-number data]
  (and
    (contains? {} nil)))


;; Public methods
;; --------------

(defn do-deposit
  "Deposit an amount of money into an account"
  [acc-number data]
  (if (-valid-deposit? acc-number data)
    (save-fact! {:operation :deposit
                :description {:amount (:amount data)}})))


(defn make-purchase
  "Make a purchase withing an account"
  [acc-number data]
  (if (-valid-purchase? acc-number data)
    (save-fact! {:operation :purchase
                :description {:amount (:amount data)
                              :store (:store data)
                              :location (:location data)}})))


(defn pay-salary 
  "Pays the salary of a given employee, identified by his/her account"
  [acc-number data]
  data)


(defn do-withdrawal
  "Withdrawal an amount of money into an account"
  [acc-number data]
  (save-fact! {:operation :purchase
              :description {:amount (:amount data)
                            :atm (:atm data)
                            :location (:location data)}}))


(defn create-account
  "Create a new account"
  [acc-number data]
  (save-fact! {:operation :account-creation
              :description {:amount (:amount data)
                            :atm (:atm data)
                            :location (:location data)}}))


(defn delete-account
  "Delete an existing account"
  [acc-number data]
  data)


(defn update-credit
  "Update an existing account"
  [acc-number data]
  data)
