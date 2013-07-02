(ns janus-generators.core-test
  (:use midje.sweet
        janus-generators.core))


(defn matching-regex? [r]
  (chatty-checker [actual] (re-matches r actual)))

(defn all-match-regex? [r]
  (has every? (matching-regex? r)))

(fact
 (take 2 (generate-from-regex "A" 10)) => (all-match-regex? #"A")
  (take 2 (generate-from-regex "." 10)) => (all-match-regex? #"."))
