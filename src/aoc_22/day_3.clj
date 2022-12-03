(ns aoc-22.day-3
  (:require [clojure.string :as str]
            [clojure.set]))

(def ^:private input (slurp "resources/input/day_3"))
(def ^:private mock-input (slurp "resources/mock_input/day_3"))

(defn parse [dada] (str/split-lines dada))
(def priority (-> ((juxt identity str/upper-case) "abcdefghijklmnopqrstuvwxyz")
                    str/join
                    (zipmap (map inc (range)))))
(defn common-item-priority [& rs]
  (->> (map set rs)
       (apply clojure.set/intersection)
       first
       (get priority)))

(def part-1-solution
  (->> (parse input)
       (map #(as-> (/ (count %) 2) n
               (list (subs % 0 n) (subs % n))))
       (map (partial apply common-item-priority))
       (reduce +)))
(def part-2-solution
  (->> (parse input)
       (partition 3)
       (map (partial apply common-item-priority))
       (reduce +)))