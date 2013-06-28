(ns janus-generators.core-test
  (:use midje.sweet
        janus-generators.core))

(fact
 (first (generate-from-regex "A")) => "A")
