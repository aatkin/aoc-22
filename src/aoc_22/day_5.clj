(ns aoc-22.day-5
 (:require [clojure.string :as str]
           [clojure.java.io]))

(def input (slurp "resources/input/day_5"))
(def mock-input (slurp "resources/mock_input/day_5"))

(def to-int #(Integer/parseInt %))
(def move-regex (re-pattern "move (\\d+) from (\\d+) to (\\d+)"))

(defn parse-config [dada]
  (->> (for [n (range 1 (count (first dada)) 4)]
         (map #(.charAt % n) (drop-last dada)))
       (map (partial filter (complement #{\space})))
       (map-indexed (fn [idx stack] [(inc idx) stack]))
       (into {})))
(defn parse-move [s]
  (->> (re-matches move-regex s)
       (rest)
       (map to-int)))
(defn parse [dada]
  (let [raw-dada (str/split-lines dada)
        config (->> (take-while not-empty raw-dada)
                    (parse-config))
        moves (->> (drop-while not-empty raw-dada)
                   (rest)
                   (map parse-move))]
    [config moves]))

(def part-1-solution
  (->> (parse input)
       (apply (partial reduce
                       (fn [m [move from to]]
                         (let [crates (-> (take move (get m from))
                                          (reverse))]
                           (-> m
                               (update to (partial concat crates))
                               (update from (partial drop move)))))))
       
       (sort)
       (map (comp first second))
       (str/join)))
(def part-2-solution
  (->> (parse input)
       (apply (partial reduce
                       (fn [m [move from to]]
                         (let [crates (take move (get m from))]
                           (-> m
                               (update to (partial concat crates))
                               (update from (partial drop move)))))))

       (sort)
       (map (comp first second))
       (str/join)))