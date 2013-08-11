(ns janus-generators.character-domains
  (:require
   [clojure.core.logic :as logic]
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

(defn alphanum-constraint []
  (fd/multi-interval (fd/interval 48 57)
                     (fd/interval (int \A) (int \Z))
                     95
                     (fd/interval (int \a) (int \z))))

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
   (= :alphanumeric expression) (alphanum-constraint)
   :else (ranges-to-domain expression)))

;; There is something weird in the order that FD is evaluated here. Does not
;; interleave nicely like conde etc.
;; Basically likely I am going to do a tree walk to implement this later so not 
;; too fussed about this blatant hack


(defn constrain-character [expression]
  (let [range
        ;; magic number alert
        (logic/run 128 [n] (fd/in n (to-domain expression)))]
    #(logic/membero % range)))
