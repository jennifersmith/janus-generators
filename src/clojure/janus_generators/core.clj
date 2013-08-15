(ns janus-generators.core
  (:refer-clojure :exclude [==])
  (:require [janus-generators.parse :refer :all]
            [clojure.core.logic :refer :all]
            [janus-generators.character-domains :refer :all]
            [janus-generators.output :refer :all]
            [janus-generators.logic :refer :all]))

;;Constructs goals based on relationship... so rel is not unifiable but hopefully cleaner

(defmulti make-goals first)

(defmethod make-goals :S [[_ & body]]
  (let [sub-branches  (map make-goals body)]
    (fn [result]
      (anyg result sub-branches))))

;; TODO: Same ish as :S
(defmethod make-goals :RE_BRANCH [[_ & body]]
  (let [sub-goals (map make-goals body)]
    (fn [result]
      (eachg result sub-goals))))

(defmethod make-goals :ONE_CHAR_RE [[_ ranges]]
  (constrain-character ranges))

 (defmethod make-goals :SIMPLE_RE [[_ body reps]]
    (let [main-goal (make-goals body)]
      (fn [result]
        (repeato reps main-goal result))))

(defn run-goals-part-two [regex n]
  (let [regex-tree (parse-regex regex)
        goals (make-goals regex-tree)]
    (run n [result] (goals result))))

(defn re-generate
  [regex n]
  (map convert-to-string
       (run-goals-part-two regex n)))

(comment

  (re-generate #"\w\w\w: \w\w\w\w\w [0-9][0-9] °C" 20)
)
