(ns janus-generators.walk-test
  (:use midje.sweet
        janus-generators.walk
        janus-generators.parse))

(fact "walking a tree of terminals ends up producing original string"
  (regex-tree (parse-regex "A"))
  => [\A])
