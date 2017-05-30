(ns nanook.core
  "This namespace contains the core businesses of Nanook
   which basically consists of a *facts* atom list that
   will store the checking accounts facts"
  (:use [nanook.utils]))

(def facts (atom ()))

(defn save-fact!
  "Atomically save a new fact to the facts list"
  [raw-fact]
  (let [final-fact (assoc raw-fact :timestamp (get-current-timestamp)
                                   :uuid (generate-uuid))]
    (swap! facts conj final-fact)
    final-fact))

(defn retrieve-fact
  "TODO: Atomically retrieve a fact from the facts list"
  [searched-fact]
  (searched-fact (deref facts)))
