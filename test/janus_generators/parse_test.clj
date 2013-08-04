(ns janus-generators.parse-test
  (:use midje.sweet
        janus-generators.core
        janus-generators.parse))

(defn simple-re-wrapper [contents reps]
  [:S [:RE_BRANCH [:SIMPLE_RE contents reps]]])

(defn one-char-re-wrapper [body]
  (simple-re-wrapper [:ONE_CHAR_RE body] [1 1]))


(tabular "Collapses down the various sorts of one-char regex into characters"
         (fact (parse-regex ?input) => (one-char-re-wrapper ?expected))
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

(tabular "Correctly computes min max repetitions for supported dupe forms"
         (fact (parse-regex ?input) => 
           (simple-re-wrapper [:ONE_CHAR_RE ?character] ?expected-reps))
         ?input ?character ?expected-reps
         "A"    \A         [1 1]
         "A+"   \A         [1 :*]
         "A*"   \A         [0 :*])
