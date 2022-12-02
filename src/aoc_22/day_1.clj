(ns aoc-22.day-1
  (:require [clojure.string]
            [clojure.java.io]))

(def ^:private input (slurp "resources/input/day_1"))
(def ^:private mock-input (slurp "resources/mock_input/day_1"))

(defn- parse [input]
  (->> (clojure.string/split-lines input)
       (map #(when-not (= "" %)
               (Integer/parseInt %)))
       (partition-by nil?)
       (keep (partial reduce +))))

(comment
  (apply max (parse mock-input)))

(def part-1-solution
  (apply max (parse input)))

(comment
  (->> (parse mock-input)
       sort
       (take-last 3)
       (reduce +)))

(def part-2-solution
  (->> (parse input)
       sort
       (take-last 3)
       (reduce +)))

;; alternative solution for part-1 and part-2
;; reads input file line-by-line, uses 3-size list and maintains
;; current sum in single atom

(comment
  (def ^:private input "resources/input/day_1")
  (def ^:private mock-input "resources/mock_input/day_1")
  (with-open [rdr (clojure.java.io/reader input)]
    (let [max-threes (atom (list))
          current (atom 0)]
      (doall
       (doseq [line (line-seq rdr) ; lazy seq
               :let [sum-now? (= "" line)]]
         (if sum-now?
           (do
             (cond
               (< (count @max-threes) 3) (swap! max-threes (comp sort conj) @current)
               (< (first @max-threes) @current) (reset! max-threes (sort (conj (rest @max-threes) @current))))
             (reset! current 0))
           (swap! current + (Integer/parseInt line)))))
      (println "max" (last @max-threes)
               "max three" @max-threes))))
