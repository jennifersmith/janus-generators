(ns janus-generators.character-domains-tests
  (:use midje.sweet
        janus-generators.core
        janus-generators.character-domains)
(:require
   [clojure.core.logic.fd :as fd]))

(defn characters-in [ranges]
  {:type :matching :range ranges})

(defn characters-not-in [ranges]
  {:type :non-matching :range ranges})

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
          [\A \B]    [33 \@ \C 127]
          [[\A \Z]]  [33 \@ \[ 127]
          [\A \Z]  [33 \@ \B \Y \[ 127]
          [[\N \Z] [\A \N]] [33 \@ \[ 127]
          [[\O \Z] \N [\A \M]] [33 \@ \[ 127]
          )
