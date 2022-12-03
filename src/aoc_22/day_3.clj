(ns aoc-22.day-3
  (:require [clojure.string :as str]
            [clojure.set]))

(def ^:private input (slurp "resources/input/day_3"))
(def ^:private mock-input (slurp "resources/mock_input/day_3"))

(defn parse [dada] (str/split-lines dada))
(defn priority [c] (cond-> (- (int c) 96) (< (int c) 96) (+ 58)))
(defn common-item [& rs] (first (apply clojure.set/intersection (map set rs))))

(def part-1-solution
  (->> (parse input)
       (map #(as-> (/ (count %) 2) n
               (list (subs % 0 n) (subs % n))))
       (map (comp priority (partial apply common-item)))
       (reduce +)))
(def part-2-solution
  (->> (parse input)
       (partition 3)
       (map (comp priority (partial apply common-item)))
       (reduce +)))