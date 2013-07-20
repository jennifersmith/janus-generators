(ns janus-generators.core
  (:refer-clojure :exclude [==])
  (:import [clojure.core.logic.protocols])
  (:require [instaparse.core :as insta]
            [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))

;; something dodgy goes on if we try do (str \n)... argh
;; I dont fully understand. TODO: Read up what the chuff is going on with escaped chars

(def int-escape-string
  (zipmap (map int (keys char-escape-string))
          (vals char-escape-string)))

(def escape-string-int
  (apply zipmap ((juxt vals keys) int-escape-string)))

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


(defn duplicateo [& foo])

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
;;(defmethod convert-char String [v] (or (char-escape-string v) v))


(defmethod convert-char Number [v]
  (char v))

(defmethod convert-char :default [v] (str "<class:" (class v) ", " v " .. probably doesnt belong>") )

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

;; Constructs domains based on regex tree
(defmulti make-domains first)

  ;; constrains the result to be in the range from/to the single expr... has to stay as an interval rather than = 'cos they are combined into multiinterval later with other potentially ranged expressions like [A-Z5] is range A-Z or a 5

(defn character-to-int [[char-type value]]
  (case char-type
        :BASIC_BE_CHAR
        (int (first value))
        :ESCAPED_BE_CHAR
        (or (escape-string-int value)
            (int (last value)))))

(defmethod make-domains :SINGLE_EXPRESSION [[_  character]]
  (let [character-value (character-to-int character)]
    (fd/interval character-value character-value)))

(defmethod make-domains :RANGE_EXPRESSION [[_  [_ from] _ [_ to]]]
  ;; constrains the result to be in the range from/to
  (fd/interval (character-to-int from) (character-to-int to)))

(defmethod make-domains :NON_MATCHING_LIST [[_ _ matching-list]]
  (let [matching-domain (make-domains matching-list) ]
    (fd/difference (any-char-domain) matching-domain)))

;; Style fix: [AAAAB] is valid but I want to treat as [A]
(defmethod make-domains :MATCHING_LIST [[_ & expressions :as tmp]]
  (let [subterms (map make-domains (distinct expressions)) ]
    (apply fd/multi-interval subterms)))

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
  (let [domain (make-domains inner-expression)]
    (fn [result]
      (fd/in result domain))))

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
  #(fd/in % (any-char-domain)))

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
