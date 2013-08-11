(ns janus-generators.logic
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all])
  (:require [clojure.core.logic.fd :as fd]))

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

(def max 100000)

(defne bounded-collectiono [from to result]
  ([lower upper []]
     (fd/in lower 0)
     (fd/in upper (fd/interval max)))
  ([lower upper [?h . ?r]]
     (fd/>= upper lower)
     (fd/> upper 0)
     (fresh [next-upper next-lower]
            (fd/- upper 1 next-upper)
            (conde
             ((fd/in lower 0) (fd/in next-lower 0))
             ((fd/> lower 0) (fd/- lower 1 next-lower)))
            (bounded-collectiono next-lower next-upper ?r))))

(defne unbounded-collectiono [from result]
  ([lower []]
     (fd/in lower 0))
  ([lower [?h . ?r]]
     (fresh [next-lower]
            (conde
             ((fd/in lower 0) (fd/in next-lower 0))
             ((fd/> lower 0) (fd/- lower 1 next-lower)))
            (unbounded-collectiono next-lower ?r))))


(defne repeato-new [from to result]
  ([from :* result] (unbounded-collectiono from result))
  ([from to result] (bounded-collectiono from to result)))

(defne repeato [reps main-goal result]
  ([_ :*])
  ([[_ 1] main-goal [head . []]]
     (main-goal head))
  ;; anything with min 0 could be empty
  ([[0 _] _ result] (== result []))
  ;; anything with max * could be recursion
  ([[lower :*] main-goal [result-head . result-tail]]
     (main-goal result-head)
     (repeato [0 :*] main-goal result-tail)))



(defn goal-fn1 [n]
  (fd/in n (fd/interval 1 10)))

(defn goal-fn2 [n]
  (conde
   ((== n 1))
   ((== n 2))
   ((== n 3))
   ((== n 4))
   ((== n 5))
   ((== n 6))
   ((== n 7))
   ((== n 8))
   ((== n 9))
   ((== n 10))))



(defn listo [of l]
  (conde [(emptyo l)]
             [(fresh [a d]
                 (conso a d l)
                 (of a)
                 (listo of d))]))


(comment

(defn slices [lol]
  (let [[heads tails] ( (juxt (partial map first) (partial map rest)) lol)]
    (if (some seq tails)
      (cons heads (slices tails))
      [heads])
        ))
  (run 100 [n] (goal-fn1 n))


o  (run 100 [n]
       (goal-fn2 n))

(run 100 [n]
      (repeato [0 100] goal-fn2 n))

(run 100 [n]
     (bounded-collectiono 0 1 n))
(run 5 [n]
     (listo goal-fn2 n))

(defn variability [results]
  (map println (map frequencies (slices results)))))
