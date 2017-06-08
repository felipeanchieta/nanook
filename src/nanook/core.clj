(ns nanook.core
  "This namespace contains the core businesses of Nanook
   which basically consists of a facts atom list that
   will store the checking accounts facts"
  (:require [nanook.utils :refer :all]))

(def nanook-facts (atom {}))

(defn save-fact!
  "Atomically save a new fact to the nanook-facts map"
  [raw-fact]
  (let [final-fact (assoc raw-fact :uuid (generate-uuid))]
    (do
      (swap!
       nanook-facts
       update-in [(keyword (:account final-fact)) :facts] #(into [] (conj % final-fact)))
      final-fact)))

(defn retrieve-facts
  "Atomically retrieve a fact from the nanook-facts map"
  [acc-number]
  (:facts ((keyword acc-number) @nanook-facts)))
