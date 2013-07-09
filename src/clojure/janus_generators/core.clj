(ns janus-generators.core
  (:refer-clojure :exclude [==])
  (:import [clojure.core.logic.protocols])
  (:require [instaparse.core :as insta]
            [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))

(defn load-parser []
  (insta/parser
   (slurp "grammars/basic")
   :start :S))

;; oooooops
(def parse (load-parser))

(declare regex-chunk)

;; it's starting to sound like an italian meal in here
(defn secondo [xs x]
  (fresh [head tail]
         (== xs (lcons head tail))
         (firsto tail x)))

(defn singlo [xs x]
  (firsto xs x)
  (== xs (lcons x [])))

(defn pairo [xy x y]
  (fresh [tail]
         (== xy (lcons x tail))
         (singlo tail y)))

(defn ord-charo [body result]
  (fresh [thechar]
         (firsto body thechar)
         (== thechar result)))

(defn any-char-domain []
  (fd/interval 33 127))

(defn any-charo [result]
  (fd/in result (any-char-domain))) ;; visible char range


;; OK So I want a conda right because once it has succeeded down one of the alternatives it probly doesnt want to succeed down another... probably. Unless i was generating regexes. Which is not really want I want to do. Though that would be awesome

(defn one-char-regexo [body result]
  (fresh [identifier rem]
         (== body (lcons identifier rem))
         (conda
          ((== identifier :ORD_CHAR) (ord-charo rem result))
          ((== identifier :ANY_CHAR) (any-charo result))
          ((log "one-char-regexo: I don't have a clue what " identifier " is!") (trace-s)))))

(defn plusso [main-part result]
  (fresh [result-head result-rem]
         (== result (lcons result-head result-rem))
         (one-char-regexo main-part result-head)
         (conde
          ((== [] result-rem))
          ((plusso main-part result-rem)))))

(defn starro [main-part result]
  (conde
   ((== [] result))
   ((fresh [result-head result-rem]
           (== result (lcons result-head result-rem))
           (one-char-regexo main-part result-head)
           (starro main-part result-rem)))))

(defn duplicateo [dupl-symbol result main-part]
  (fresh [duplicate-char]
         (secondo dupl-symbol duplicate-char)
         (conda
          ((== duplicate-char "*") (starro main-part result))
          ((== duplicate-char "+") (plusso main-part result))
          ((log "duplicateo: I don't have a clue what " duplicate-char " is!") (trace-s)))))

(defn simple-regexo [body result]
  (fresh [identifier rem]
         (== body (lcons identifier rem))
         (fresh [main-part dupl-symbol main-result]
                (conda
                 ((singlo body main-part) 
                  (fresh [main-part-wtf]
                         (secondo main-part main-part-wtf)
                         (one-char-regexo main-part-wtf result)
                         ))
                 (
                  (pairo body main-part dupl-symbol
                         )
                  (fresh [main-part-wtf ]
                         (secondo main-part main-part-wtf)
                         (one-char-regexo main-part-wtf main-result)
                         (duplicateo dupl-symbol result main-part-wtf)))) ;; hahah i am just taking piss now
)))

(defn regex-chunk [regex result]
  (fresh [identifier body chunk]
         (== (lcons identifier body) regex)
         (conda
          ((== identifier :SIMPLE_RE) (simple-regexo body result)) 
          ((log "I don't have a clue what " identifier " is!") (trace-s)))))

(defn regex-unify [regex result]
  (fresh [head tail resulthead resulttail]
         (== regex (lcons head tail))
         (== result (lcons resulthead resulttail))
         (regex-chunk head resulthead)
         (conde
          ((== [] tail) (== [] resulttail))
          ((regex-unify tail resulttail)))))


(defmulti convert-char class)
(defmethod convert-char String [v] v)
(defmethod convert-char Number [v] (char v))
(defmethod convert-char :default [v] (str "<class:" v ", " v " .. probably doesnt belong>") )

(defn convert-to-string [s]
  (apply str  (map convert-char (flatten s))))

(defn run-goals [regex n]
  (let [[_ & regex-tree] (parse regex)]
    (run n [result]
         (regex-unify regex-tree result))))

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

;;Constructs goals based on relationship... so rel is not unifiable but hopefully cleaner

(defmulti make-goals first)
(defmethod make-goals :default [regex] (println "TODO: Handle " regex) (fn [x] (trace-s)))
(defmethod make-goals :S [[_ & body]]
  (let [sub-goals (map make-goals body)]
    (fn [result]
      (eachg result sub-goals))))
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

(defmethod make-goals :ORD_CHAR [[_ the-char]]
  #(== the-char %))

(defmethod make-goals :ANY_CHAR [_]
  #(fd/in % (any-char-domain)))

(defn run-goals-part-two [regex n]
  (let [regex-tree (parse regex)
        goals (make-goals regex-tree)]
    (run n [result] (goals result))))

(defn generate-from-regex
  [regex n]
  (map convert-to-string
       (run-goals-part-two regex n)))
