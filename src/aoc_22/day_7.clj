(ns aoc-22.day-7
  (:require [clojure.java.io]
            [clojure.string :as str]))

(def ^:private input (slurp "resources/input/day_7"))
(def ^:private mock-input (slurp "resources/mock_input/day_7"))

(defn parse-int [s] (Integer/parseInt s))

(defn parse [dada]
  (for [row (str/split-lines dada)]
    (->> [[:cd #"^\$ cd (.+)$"]
          [:ls #"^\$ ls$"]
          [:dir #"^dir (.+)$"]
          [:file #"^(\d+) (.+)$"]]
         (some (fn [[type regex]]
                 (when-let [result (re-matches regex row)]
                   [type (rest result)]))))))

(defmulti command (fn [_ cmd] (first cmd)))
(defmethod command :ls [state _] state) ;noop

(defmethod command :cd [state [_ [path]]]
  (case path
    "/" (assoc state :path (list "/"))
    ".." (update state :path drop-last)
    (update state :path concat [path])))

(defmethod command :dir [state [_ args]]
  (-> state
      (assoc-in (concat [:tree] (:path state) args)
                {:files [] :size 0})
      (update :dirs conj (concat (:path state) args))))

(defn update-dir-sizes [state path size]
  (loop [state state
         path path]
    (if (= :tree (last path))
      state
      (recur (update-in state (concat path [:size]) + size)
             (drop-last path)))))

(defmethod command :file [state [_ [size name]]]
  (let [path (concat [:tree] (:path state))
        size (parse-int size)]
    (-> state
        (update-in (concat path [:files])
                   concat
                   [size name])
        (update-dir-sizes path size))))

(defn map-dirs [state]
  (->> (:dirs state)
       (map (comp :size #(get-in (:tree state) %)))))

(def part-1-solution
  (->> (parse input)
       (reduce command {:tree
                        {"/" {:files [] :size 0}}
                        :path (list)
                        :dirs (list (list "/"))})
       (map-dirs)
       (filter #(< % 100000))
       (reduce + 0)))

(defn smallest-deletable [dirs]
  (let [dirs (sort dirs)
        unused-space (- 70000000 (last dirs))
        needed-space (- 30000000 unused-space)]
    (->> (split-with #(< % needed-space) dirs)
         (second)
         (first))))

(def part-2-solution
  (->> (parse input)
       (reduce command {:tree
                        {"/" {:files [] :size 0}}
                        :path (list)
                        :dirs (list (list "/"))})
       (map-dirs)
       (smallest-deletable)))
