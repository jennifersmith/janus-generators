(ns janus-generators.logic
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]))

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

;; we are recursing on reps (non-rel) meaning any long expansion of * could (possibly) blow the stack : not actually sure as have not made it do so
(defne repeato [reps main-goal result]
  ([[_ 1] main-goal [head . []]]
     (main-goal head))
  ;; anything with min 0 could be empty
  ([[0 _] _ result] (== result []))
  ;; anything with max * could be recursion
  ([[lower :*] main-goal [result-head . result-tail]]
     (main-goal result-head)
     (repeato [0 :*] main-goal result-tail)))

