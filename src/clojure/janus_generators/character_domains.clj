(ns janus-generators.character-domains
  (:require
   [clojure.core.logic.fd :as fd]))

(defn range-to-domain [range]
  (if (vector? range)
    (let [[from to] (map int range)]
      (fd/interval from to ))
    (int range)))

(defn- lowest-part [range]
  (if (vector? range)
    (first range)
    range))


(defn any-character-constraint []
  (fd/multi-interval 9 32 (fd/interval 33 127)))

(defn make-domain [range]
  (let [subterms (map range-to-domain (sort-by lowest-part (distinct range)))]
    (apply fd/multi-interval (fd/normalize-intervals subterms))))

(defmulti ranges-to-domain first)

(defmethod ranges-to-domain :matching [[_ range]]
  (make-domain range))

;;dodgy cas it calls the other method
(defmethod ranges-to-domain :non-matching [[_ range]]
  (let [domain (make-domain range)]
    (fd/difference (any-character-constraint) domain)))


(defn constant-character-constraint [constant]
  (int constant))

(defn to-domain [expression]
  (cond
   (char? expression)  (constant-character-constraint expression)
   (= :any-char expression) (any-character-constraint)
   :else (ranges-to-domain expression)))

(defn constrain-character [expression]
  #(fd/in % (to-domain expression)))