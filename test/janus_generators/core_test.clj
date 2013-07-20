(ns janus-generators.core-test
  (:use midje.sweet
        janus-generators.core))


(defn matching-regex? [r]
  (chatty-checker [actual] (re-matches r actual)))

(defn all-match-regex? [r]
  (has every? (matching-regex? r)))

;; better way? 
(defmacro regex-generates-fact [regex] 
  (let [fact-name (str "Generates strings from regex " regex) 
        regex-str (str regex)]
    `(fact fact-name  
           (generate-from-regex ~regex-str 10) => (all-match-regex? ~regex))))

(tabular "Regex generation"
         (fact 
          (generate-from-regex (str ?r) 10) => (all-match-regex? ?r))
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
         #"[^A-Z1-1]")
