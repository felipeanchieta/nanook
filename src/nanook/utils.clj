(ns nanook.utils
  "This namespace defines functions that are considered
   useful to other namespaces in Nanook, gathering utilities
   together in one file"
  (:require [clj-time.local :as l]))

(defn get-current-timestamp
  "Returns the current time from the system"
  []
  (str (l/local-now)))

(defn generate-uuid
  "Returns a random UUID as a string"
  []
  (str (java.util.UUID/randomUUID)))
