(ns aoc-22.day-6
 (:require [clojure.string :as str]))

(def ^:private input (slurp "resources/input/day_6"))
(def ^:private mock-input (slurp "resources/mock_input/day_6"))

(defn find-first [pred coll] (some #(when (pred %) %) coll))
(defn get-start-of-marker [n dada]
  (->> (partition n 1 dada)
       (find-first (comp (partial = n) count set))
       (str/join)
       (str/index-of dada)
       (+ n)))

(def part-1-solution (get-start-of-marker 4 input))
(def part-2-solution (get-start-of-marker 14 input))