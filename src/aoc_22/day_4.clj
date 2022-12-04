(ns aoc-22.day-4
 (:require [clojure.string :as str]))

(def ^:private input (slurp "resources/input/day_4"))
(def ^:private mock-input (slurp "resources/mock_input/day_4"))

(def split-pair #(str/split % #"-"))
(def split-range #(str/split % #","))
(def to-int #(Integer/parseInt %))

(defn parse [dada] (->> (str/split-lines dada)
                        (map (comp (partial map to-int)
                                   (partial mapcat split-pair)
                                   split-range))))
(defn fully-in-range? [s1 e1 s2 e2] (or (and (>= s1 s2) (<= e1 e2))
                                        (and (>= s2 s1) (<= e2 e1))))
(defn in-range? [s1 e1 s2 e2] (or (and (>= e1 s2) (<= s1 s2))
                                  (and (>= e2 s1) (<= s2 s1))))

(def part-1-solution
  (->> (parse input)
       (filter (partial apply fully-in-range?))
       count))
(def part-2-solution
  (->> (parse input)
       (filter (partial apply in-range?))
       count))