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

;; Eachg is now a macro! Basically creates fresh things for each part 
;; Sure there is something easier
(defmacro eachg2 [result goal-fns]
  (let [goals (map (fn [goal] (vector `(~goal ~result ))) goal-fns)]
    `(conde
      ~@goals)))

;; TODO: Better way than recursion. Although it's not going to go crazy I think it might be slow
(defn anyg [result goals]
  (if (seq goals)
    (conde
     (((first goals) result))
     ((anyg result (rest goals)))
     )
    fail))

(def max-bound 100000)

(defne bounded-collectiono [from to result]
  ([lower upper []]
     (fd/in lower 0)
     (fd/in upper (fd/interval max-bound)))
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


(defna collectiono [from to result]
  ([from :* result] (unbounded-collectiono from result))
  ([from to result] (bounded-collectiono from to result)))

(defne listo [ main-goal result]
  ([_ []])
  ([main-goal result]
     (fresh [h r]
            (conso h r result)
            (main-goal h)
            (listo main-goal r))))

(defn repeato [reps main-goal result]
  (fresh [top bottom]
         (== [top bottom] reps)
         (collectiono top bottom result)
         (listo main-goal result)))
