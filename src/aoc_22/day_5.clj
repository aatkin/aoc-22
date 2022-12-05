(ns aoc-22.day-5
 (:require [clojure.string :as str]
           [clojure.java.io]))

(def input (slurp "resources/input/day_5"))
(def mock-input (slurp "resources/mock_input/day_5"))

(def to-int #(Integer/parseInt %))
(def parse-move (comp (partial map to-int)
                      (partial re-seq #"\d+")))

(defn parse-config [dada]
  (->> (for [n (range 1 (count (first dada)) 4)]
         (map #(.charAt % n) (drop-last dada)))
       (map (partial filter (complement #{\space})))
       (map-indexed (fn [idx stack] [(inc idx) stack]))
       (into {})))

(defn parse [dada]
  (let [[config moves] (->> (str/split-lines dada)
                            (split-with not-empty))]
    [(parse-config config) (rest moves)]))

(def ^:dynamic *reverse?* true)
(defn execute [state instructions]
  (let [[move from to] (parse-move instructions)
        crates (cond-> (take move (get state from))
                 *reverse?* (reverse))]
    (-> state
        (update to (partial concat crates))
        (update from (partial drop move)))))

(def part-1-solution
  (->> (parse input)
       (apply (partial reduce execute))
       (sort)
       (map (comp first second))
       (str/join)))
(def part-2-solution
  (with-redefs [*reverse?* false]
    (->> (parse input)
         (apply (partial reduce execute))
         (sort)
         (map (comp first second))
         (str/join))))