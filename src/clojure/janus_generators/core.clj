(ns janus-generators.core
  (:refer-clojure :exclude [==])
  (:require [instaparse.core :as insta]
            [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))

(defn load-parser []
  (insta/parser
   (slurp "grammars/basic")
   :start :S))

(defn parse [v]
  (let [parser (load-parser)]
    (parser v)))


(defn one-char-regexo [body result]
  (fresh [thechar]
         (firsto body thechar)
         (regex-chunk thechar result)))

(defn ord-charo [body result]
  (fresh [thechar]
         (firsto body thechar)
         (== thechar result)))

(defn any-charo [body result]
  (fd/in result (fd/interval (int \A) (int \z))))

;; OK So I want a conda right because once it has succeeded down one of the alternatives it probly doesnt want to succeed down another... probably. Unless i was generating regexes. Which is not really want I want to do. Though that would be awesome

(defn regex-chunk [regex result]
  (fresh [identifier body]
         (== (lcons identifier body) regex)
         (conda
          ((== identifier :ONE_CHAR_RE) (one-char-regexo body result)) 
          ((== identifier :ORD_CHAR)(ord-charo body result))
          ((== identifier :ANY_CHAR) (any-charo body result))
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

(defn convert-to-string [s]
  (apply str (map convert-char s)))

(defn generate-from-regex
  "Generates a lazy sequence of strings conforming to the regex. Or at least it will." 
  [regex n]
  (let [[_ & regex-tree] (parse regex)]
    (map #(convert-to-string %)
         (run n [result]
              (regex-unify regex-tree result)))))
