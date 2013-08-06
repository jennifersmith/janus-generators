(ns janus-generators.core-test
  (:use midje.sweet
        janus-generators.core))

;; really do I have to do this?

(def not-empty? (complement empty?))

(defn matching-regex? [r]
  (chatty-checker [actual] (re-matches r actual)))

(defn all-match-regex? [r]
  (every-checker
   not-empty?
   (has every? (matching-regex? r))))


(tabular "Regex generation"
         (fact
          (re-generate (str ?r) 10) => (all-match-regex? ?r))
         ?r
         #"A"
         #"."
         #"AB"
         #"ABCDefg"
         #"A*"
         #"A+"
         #"(Abc)D+"
         #"A|B|C"
         #"[A-Z]"
         #"[^A-Z]"
         #"[A-Z1-5]"
         #"[^A-Z1-1]"
         #"[ADEFG2]"
         #"Ab?C"
         #"\w")

(tabular "More complicated stuff"
         (fact
          (re-generate (str ?r) 10) => (all-match-regex? ?r))
         ?r
         #"ABC(?<foo>D)"
         #"[\]]"
         #"[\n]"
         #"[\.]"
         #"\n"
         #"\^")

(comment
  (tabular "More Complicated Regex generation"
           (fact
             (re-generate (str ?r) 10) => (all-match-regex? ?r))
           ?r
           #"\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}" ;; email
           ))
