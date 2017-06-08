(ns nanook.validation
  "This namespace contains the validation definitions
   that are used in the handler in order to filter out
   request with wrong bodies"
  (:require [clj-time.format :as f]
            [clova.core :as clova]))

(def credit-validation
  (clova/validation-set [:amount clova/required? clova/numeric? clova/positive?
                         :description clova/required? clova/stringy? [clova/longer? 5] [clova/shorter? 255]]))

(def debit-validation credit-validation)

(def statement-validation
  (clova/validation-set [:from clova/required? [clova/date? {:formatter "dd/MM/yyyy"}]
                         :to clova/required? [clova/date? {:formatter "dd/MM/yyyy"}]]))

(def debit-periods-validation
  (clova/validation-set [:from clova/required? clova/date?
                         :to clova/required? clova/date?]))
