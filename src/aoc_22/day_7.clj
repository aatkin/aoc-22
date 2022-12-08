(ns aoc-22.day-7
  (:require [clojure.java.io]
            [clojure.string :as str]))

(def ^:private input (slurp "resources/input/day_7"))
(def ^:private mock-input (slurp "resources/mock_input/day_7"))

(defn parse-line [s]
  (->> [[:cd #"^\$ cd (.+)$"]
        [:file #"^(\d+) (.+)$"]]
       (some (fn [[type regex]]
               (when-let [args (seq (rest (re-matches regex s)))]
                 [type args])))))
(defn parse [dada] (->> (str/split-lines dada)
                        (keep parse-line)))

(defmulti command (fn [_ cmd] (first cmd)))
(defmethod command :default [state _] state) ;noop

(defmethod command :cd [state [_ args]]
  (case (first args)
    "/" (assoc state :path (list "/"))
    ".." (update state :path drop-last)
    (-> state
        (update :path concat args)
        (update :dirs conj (concat (:path state) args)))))

(defmethod command :file [state [_ args]]
  (let [size (Integer/parseInt (first args))]
    (loop [state state
           path (concat [:tree] (:path state))]
      (if (= :tree (last path))
        state
        (recur (update-in state
                          (concat path [:size])
                          (fnil (partial + size) 0))
               (drop-last path))))))

(defn map-dirs [states]
  (let [state (reduce command {:dirs [["/"]]} states)]
    (->> (:dirs state)
         (map (comp :size
                    (partial get-in (:tree state)))))))

(def part-1-solution
  (->> (parse input)
       (map-dirs)
       (filter #(< % 100000))
       (reduce + 0)))

(defn find-smallest-deletable [dirs]
  (let [dirs (sort dirs)
        unused-space (- 70000000 (last dirs))
        needed-space (- 30000000 unused-space)]
    (->> (drop-while #(< % needed-space) dirs)
         (first))))

(def part-2-solution
  (->> (parse input)
       (map-dirs)
       (find-smallest-deletable)))

