#!/usr/bin/env bb

(ns scaffold
  (:require [clojure.string]
            [clojure.tools.cli :refer [parse-opts]]
            [babashka.fs :as fs]))

(def day-n (-> (parse-opts *command-line-args* {})
               :arguments
               first))

(defn clj-template [n]
  (str "(ns aoc-22.day-" n
       "\n (:require [clojure.string :as str]))"
       "\n"
       "\n(def ^:private input (slurp \"resources/input/day_" n "\"))"
       "\n(def ^:private mock-input (slurp \"resources/mock_input/day_" n "\"))"
       "\n"
       "\n(defn parse [dada] (str/split-lines dada))"
       "\n"
       "\n(comment"
       "\n (parse mock-input))"
       "\n"
       "\n(def part-1-solution nil)"
       "\n(def part-2-solution nil)"))

(defn create-file [path]
  (when-not (fs/exists? (fs/file path))
    (fs/create-file path)))

(defn create-clj-template [day-n]
  (spit (fs/file "src" "aoc_22" (str "day_" day-n ".clj"))
        (clj-template day-n))
  (println "created" (str "src/aoc_22/day_" day-n ".clj")))

(if (nil? day-n)
  (println "provide day as integer: bb scaffold.clj 1")
  (do
    (fs/create-dirs "resources/mock_input")
    (fs/create-dirs "resources/input")
    (create-file (str "resources/mock_input/day_" day-n))
    (create-file (str "resources/input/day_" day-n))
    (create-clj-template day-n)))

