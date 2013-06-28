(ns janus-generators.core-test
  (:use midje.sweet
        janus-generators.core))

(fact
 (take 2 (generate-from-regex "A" 10)) => ["A"]
  (take 2 (generate-from-regex "." 10)) => ["a" "b"])
