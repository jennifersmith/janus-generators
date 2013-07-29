(ns janus-generators.core
  (:refer-clojure :exclude [==])
  (:import [clojure.core.logic.protocols])
  (:require [janus-generators.parse :refer :all]
            [clojure.core.logic :refer :all]
            [janus-generators.character-domains :refer :all]))


(defmulti convert-char class)
;;(defmethod convert-char String [v] (or (char-escape-string v) v))


(defmethod convert-char Number [v]
  (char v))

(defmethod convert-char :default [v] (str "<class:" (class v) ", " v " .. probably doesnt belong>") )

(defn convert-to-string [s]
  (apply str  (map convert-char (flatten s))))


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


(defn starro-new [main-part result]
  (conde
   ((== [] result))
   ((fresh [result-head result-rem]
           (== result (lcons result-head result-rem))
           (main-part result-head)
           (starro-new main-part result-rem)))))

(defn plusso-new [main-part result]
  (fresh [result-head result-rem]
         (== result (lcons result-head result-rem))
         (main-part result-head)
         (conde
          ((== [] result-rem))
          ((plusso-new main-part result-rem)))))


;;Constructs goals based on relationship... so rel is not unifiable but hopefully cleaner

(defmulti make-goals first)
(defmethod make-goals :default [regex] (println "TODO: Handle " regex) (fn [& x] (trace-s)))
(defmethod make-goals :S [[_ & body]]
  (let [sub-branches  (map make-goals (take-nth 2 body))] ;; removes |
    (fn [result]
      (anyg result sub-branches))))

;; a bit unneccessary - can we do something with inline in instaparse
(defmethod make-goals :ONE_CHAR_RE [[_ contents]]
  (let [contents-goal (make-goals contents)]
    #(contents-goal %)))

 (defmethod make-goals :SIMPLE_RE [[_ & goals]]
  (let [[main-goal dupl-goal] (map make-goals goals)]
   (fn [result]
     (if (nil? dupl-goal)
       (main-goal result)
       (dupl-goal main-goal result)))))


(defmethod make-goals :BRACKET_EXPRESSION [[_ inner-expression]]
  (make-character-constraint inner-expression))

;; TODO: Same ish as :S
(defmethod make-goals :RE_BRANCH
 [[_ & body]]
  (let [sub-goals (map make-goals body)]
    (fn [result]
      (eachg result sub-goals))))

;; todo :Better dispatch
(defmethod make-goals :ORD_CHAR [[_ the-char]]
  #(==  (int (last the-char)) %))

(defmethod make-goals :ESCAPED_CHAR [[_ the-char]]
  #(== (escape-string-int the-char) %))

(defmethod make-goals :ESCAPED_SPEC_CHAR [[_ the-char]]
  #(== % (int (last the-char))))

(defmethod make-goals :ANY_CHAR [_]
  (constrain-to-char))

(defmethod make-goals :DUPL_SYMBOL [[ _ symbol]]
  (case symbol
    "*" starro-new
    "+" plusso-new
    :else
    #(all
     (log "Do not recognise " symbol) (trace-s))))

;; todo : identicalish to S
(defmethod make-goals :GROUP [[ _ & body]]
  (let [sub-goals (map make-goals (rest (butlast body)))]
    (fn [result]
      (eachg result sub-goals))))


(defn run-goals-part-two [regex n]
  (let [regex-tree (parse regex)
        goals (make-goals regex-tree)]
    (run n [result] (goals result))))

(defn generate-from-regex
  [regex n]
  (map convert-to-string
       (run-goals-part-two regex n)))
