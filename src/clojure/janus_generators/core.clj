(ns janus-generators.core
  (:refer-clojure :exclude [==])
;;  (:import [clojure.core.logic.protocols])
  (:require [janus-generators.parse :refer :all]
            [clojure.core.logic :refer :all]
            [janus-generators.character-domains :refer :all]
            [janus-generators.output :refer :all]))

;; TODO: Are these somewhere else
;; unifies results to a sequence with the head being unified to the first goal
;; and so on. yes recursion
(defn eachg [result goals]
  (if (seq goals)
    (fresh [head tail]
           (== result (lcons head tail))
           ((first goals) head)
           (eachg tail (rest goals)))
    (== result [])))

;; TODO: Better way than recursion. Although it's not going to go crazy I think it might be slow
(defn anyg [result goals]
  (if (seq goals)
    (conde
     (((first goals) result))
     ((anyg result (rest goals)))
     )
    fail))


(defn starro [main-part result]
  (conde
   ((== [] result))
   ((fresh [result-head result-rem]
           (== result (lcons result-head result-rem))
           (main-part result-head)
           (starro main-part result-rem)))))

(defn plusso [main-part result]
  (fresh [result-head result-rem]
         (== result (lcons result-head result-rem))
         (main-part result-head)
         (conde
          ((== [] result-rem))
          ((plusso main-part result-rem)))))

;; TODO: DOdgy territory
;; (def foo (parse-regex "A*"))
;; (def bar ((comp second second) foo))

(defn make-reps-goal [reps]
   (case reps
    [0 :*] starro
    [1 :*] plusso
    [1 1] #(%1 %2) ;; applies main goal to result TEMP!
))

;;Constructs goals based on relationship... so rel is not unifiable but hopefully cleaner

(defmulti make-goals first)

(defmethod make-goals :S [[_ & body]]
  (let [sub-branches  (map make-goals (take-nth 2 body))] ;; removes |
    (fn [result]
      (anyg result sub-branches))))

(defmethod make-goals :ONE_CHAR_RE [[_ ranges]]
  (constrain-character ranges))

 (defmethod make-goals :SIMPLE_RE [[_ body reps]]
    (let [main-goal (make-goals body)
          reps-goal (make-reps-goal reps)]
      (fn [result]
        (reps-goal main-goal result))))


;; TODO: Same ish as :S
(defmethod make-goals :RE_BRANCH
 [[_ & body]]
  (let [sub-goals (map make-goals body)]
    (fn [result]
      (eachg result sub-goals))))

(defmethod make-goals :DUPL_SYMBOL [[ _ symbol]]
)

;; todo : identicalish to S
(defmethod make-goals :GROUP [[ _ & body]]
  (let [sub-goals (map make-goals (rest (butlast body)))]
    (fn [result]
      (eachg result sub-goals))))


(defn run-goals-part-two [regex n]
  (let [regex-tree (parse-regex regex)
        goals (make-goals regex-tree)]
    (run n [result] (goals result))))

(defn generate-from-regex
  [regex n]
  (map convert-to-string
       (run-goals-part-two regex n)))
