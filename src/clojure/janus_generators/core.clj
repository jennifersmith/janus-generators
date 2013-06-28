(ns janus-generators.core
  (:refer-clojure :exclude [==])
  (:require [instaparse.core :as insta]
            [clojure.core.logic :refer :all]
            [clojure.core.logic.dcg :refer [-->e -->]]))

(defn load-parser []
  (insta/parser
   (slurp "grammars/basic")
   :start :S))

(defn parse [v]
  (let [parser (load-parser)]
    (parser v)))


(defn single-charo [body result]
  (fresh [thechar]
         (firsto body thechar)
         (== result thechar)))
;; OK So I want a conda right because once it has succeeded down one of the alternatives it probly doesnt want to succeed down another... probably. Unless i was generating regexes. Which is not really want I want to do. Though that would be awesome

(defn regex-chunk [regex result]
  (fresh [identifier body]
         (== (lcons identifier body) regex)
         (conda
          ((== identifier :SINGLE_CHAR) (single-charo body result)))
         ))
(defn regex-unify [regex result]
  (fresh [head tail resulthead resulttail]
    (== regex (lcons head tail))
    (== result (lcons resulthead resulttail))
    (regex-chunk head resulthead)
    (conde
     ((== [] tail) (== [] resulttail) )
     (
      (regex-unify tail resulttail)))
    ))

(defn generate-from-regex
  "Generates a lazy sequence of strings conforming to the regex. Or at least it will." 
  [regex]
  (let [[_ & regex-tree] (parse regex)]
    (run 1 [result]
         (regex-unify regex-tree result))))
