(ns janus-generators.character-domains-tests
  (:use midje.sweet
        janus-generators.core
        janus-generators.character-domains)
(:require
   [clojure.core.logic.fd :as fd]))

(defn characters-in [ranges]
  [:matching ranges])

(defn characters-not-in [ranges]
  [:non-matching ranges])

(defn make-intervals [bounds]
  (->>
   bounds
   (map int)
   (partition 2)
   (map #(apply fd/interval %))
   (fd/normalize-intervals)
   (apply fd/multi-interval)))

(tabular "Matching domain constraints correctly applied"
          (fact
            (ranges-to-domain (characters-in ?c))
            => ?r)
          ?c         ?r
          [\A \B]    (fd/interval (int \A) (int \B))
          [[\A \Z]]  (fd/interval (int \A) (int \Z))
          [\A \Z]    (fd/multi-interval (int \A) (int \Z))
          [[\N \Z] [\A \N]] (fd/interval (int \A) (int \Z))
          [[\O \Z] \N [\A \M]] (fd/interval (int \A) (int \Z)))

(tabular "Non-matching domain constraints correctly applied"
          (fact
            (ranges-to-domain (characters-not-in ?c))
            => (make-intervals ?r))
          ?c         ?r
          [\A \B]    [9 9 32 32 33 \@ \C 127]
          [[\A \Z]]  [9 9 32 32 33 \@ \[ 127]
          [\A \Z]  [9 9 32 32 33 \@ \B \Y \[ 127]
          [[\N \Z] [\A \N]] [9 9 32 32 33 \@ \[ 127]
          [[\O \Z] \N [\A \M]] [9 9 32 32 33 \@ \[ 127])

(tabular "Absolute character constraints"
         (fact
           (constant-character-constraint ?input) => ?result)
         ?input   ?result
         \A       (int \A)
         \newline (int \newline))

(fact "Any character constraints"
  (any-character-constraint) =>
  (fd/multi-interval 9 32 (fd/interval 33 127)))

(fact "Alphanum constraints"
  (alphanum-constraint) =>
  (fd/multi-interval (fd/interval 48 57)
                     (fd/interval (int \A) (int \Z))
                     95
                     (fd/interval (int \a) (int \z)) ))

;; TODO : dont test disbatch of the various representations of single char/range etc.\
;; Tested thru core tests right now which test the whole lot
