(ns aoc-22.day-2
  (:require [clojure.string :as str]))

(def ^:private input (slurp "resources/input/day_2"))
(def ^:private mock-input (slurp "resources/mock_input/day_2"))

(defn parse [dada]
  (->> (str/split-lines dada)
       (map #(str/split % #" "))))

;; {S -> [win(S) draw(S) lose(S)]}
(def outcomes {"A" ["Y" "X" "Z"]
               "B" ["Z" "Y" "X"]
               "C" ["X" "Z" "Y"]})

(defn shape-score [s]
  (case s
    ("A" "X") 1 ; rock
    ("B" "Y") 2 ; paper
    ("C" "Z") 3)) ; scissors

(def part-1-solution
  (->> (parse input)
       (map (fn [[s1 s2]]
              (let [[win? draw? _] (map #{s2} (get outcomes s1))]
                (cond-> (shape-score s2)
                  win? (+ 6)
                  draw? (+ 3)))))
       (reduce +)))
(def part-2-solution
  (->> (parse input)
       (map (fn [[s1 s2]]
              (let [[win? draw? lose?] (get outcomes s1)]
                (condp = s2
                  "Z" (+ 6 (shape-score win?))
                  "Y" (+ 3 (shape-score draw?))
                  (shape-score lose?)))))
       (reduce +)))
