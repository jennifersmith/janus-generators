(ns janus-generators.parse-test
  (:use midje.sweet
        janus-generators.core
        janus-generators.parse))

(defn simple-re-wrapper [body]
  [:S [:RE_BRANCH [:SIMPLE_RE [:ONE_CHAR_RE body]]]])

(tabular "Collapses down the various sorts of one-char regex into characters"
         (fact (parse-regex ?input) => (simple-re-wrapper ?expected))
           ?input  ?expected
           "A"     \A
           #"[\]]" [:matching [\]]]
           #"[^ABC]" [:non-matching [\A \B \C]]
           #"[\n]" [:matching [\newline]]
           #"[\.]" [:matching  [\.]]
           #"[1A-Z]" [:matching [\1 [\A \Z]]]
           #"\n"   \newline
           #"\^"   \^
           #"."    :any-char)
