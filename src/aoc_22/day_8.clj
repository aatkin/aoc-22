(ns aoc-22.day-8
 (:require [clojure.string :as str]))

(def ^:private input (slurp "resources/input/day_8"))
(def ^:private mock-input (slurp "resources/mock_input/day_8"))

(defn to-int [s] (Integer/parseInt s))
(defn transpose [coll] (apply map vector coll))
(defn parse [dada]
  (let [from-left (->> (str/split-lines dada)
                       (map #(str/split % #"")))
        from-right (map reverse from-left)
        from-top (transpose from-left)
        from-bottom (map reverse from-top)]
    [from-left from-right from-top from-bottom]))

(defn find-visibles [row]
  (loop [row (map to-int row)
         visibles []]
    (if (<= (count row) 1)
      (concat [1] visibles)
      (let [right-most (last row)
            lefts (butlast row)]
        (recur lefts
               (if (< (apply max lefts) right-most)
                 (concat [1] visibles)
                 (concat [0] visibles)))))))

(defn flip-tables [[from-left from-right from-top from-bottom]]
  [from-left
   (map reverse from-right)
   (transpose from-top)
   (transpose (map reverse from-bottom))])

(defn count-visibles [[from-left from-right from-top from-bottom]]
  (map (fn [& xs] (some #{1} xs))
       (flatten from-left)
       (flatten from-right)
       (flatten from-top)
       (flatten from-bottom)))

(defn calculate-scenic [row]
  (let [[left right] (->> (rest row)
                          (split-with (fn [x]
                                        (< x (first row)))))]
    (+ (count left) (if (empty? right)
                      0 1))))

(defn count-scenics [[from-left from-right from-top from-bottom]]
  (map (fn [& xs] (apply * xs))
       (flatten from-left)
       (flatten from-right)
       (flatten from-top)
       (flatten from-bottom)))

(defn scenics [row]
  (loop [row (map to-int row)
         visibles []]
    (if (<= (count row) 1)
      (concat visibles [1])
      (let [rights (rest row)
            scenic (calculate-scenic row)]
        (recur rights
               (concat visibles (if (zero? scenic)
                                  [1] [scenic])))))))

(defn drop-edges [[from-left from-right from-top from-bottom]]
  (let [from-left (->> from-left
                       (map #(drop-last (rest %)))
                       (rest)
                       (drop-last))
        from-right (->> from-right
                        (map #(drop-last (rest %)))
                        (rest)
                        (drop-last))
        from-top (->> from-top
                      (map #(drop-last (rest %)))
                      (rest)
                      (drop-last))
        from-bottom (->> from-bottom
                         (map #(drop-last (rest %)))
                         (rest)
                         (drop-last))]
    [from-left from-right from-top from-bottom]))

(def part-1-solution
  (->> (parse input)
       (map (partial map find-visibles))
       (flip-tables)
       (count-visibles)
       (filter some?)
       (reduce + 0)))
(def part-2-solution
  (->> (parse input)
       (map (partial map scenics))
       (flip-tables)
       (drop-edges)
       (count-scenics)
       (apply max)))