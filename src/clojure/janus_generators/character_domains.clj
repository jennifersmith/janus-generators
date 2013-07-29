(ns janus-generators.character-domains
  (:require
   [clojure.core.logic.fd :as fd]))


(defn any-char-domain []
  (fd/interval 33 127))
;; TODO : Fix duplication. ACtually move all character stuff in here and test it 

;; something dodgy goes on if we try do (str \n)... argh
;; I dont fully understand. TODO: Read up what the chuff is going on with escaped chars
(def int-escape-string
  (zipmap (map int (keys char-escape-string))
          (vals char-escape-string)))

(def escape-string-int
  (apply zipmap ((juxt vals keys) int-escape-string)))

(defn character-to-int [[char-type value]]
  (case char-type
        :BASIC_BE_CHAR
        (int (first value))
        :ESCAPED_BE_CHAR
        (or (escape-string-int value)
            (int (last value)))))

(defn range-to-domain [range]
  (if (vector? range)
    (let [[from to] (map int range)]
      (fd/interval from to ))
    (int range)))

(defn- lowest-part [range]
  (if (vector? range)
    (first range)
    range))

(defn make-domain [range]
  (let [subterms (map range-to-domain (sort-by lowest-part (distinct range)))]
    (apply fd/multi-interval (fd/normalize-intervals subterms))))

(defmulti ranges-to-domain :type)

(defmethod ranges-to-domain :matching [{:keys [ range]}]
  (make-domain range))

;;dodgy cas it calls the other method
(defmethod ranges-to-domain :non-matching [{:keys [ range]}]
  (let [domain (make-domain range)]
    (fd/difference (any-char-domain) domain)))

(defn constrain-to-char []
  #(fd/in % (any-char-domain)))

(defn constrain-to-range [range-expression]
  #(fd/in % (ranges-to-domain range-expression)))

;; ===== shit layer ====

(defmulti parse-chunk first)

(defmethod parse-chunk :SINGLE_EXPRESSION [[_  character]]
  (let [character-value (character-to-int character)]
    character-value))

(defmethod parse-chunk :RANGE_EXPRESSION [[_  [_ from] _ [_ to]]]
  ;; constrains the result to be in the range from/to
  (vector (character-to-int from) (character-to-int to)))

(comment  (let [matching-domain (make-domains matching-list) ]
))

(defmethod parse-chunk :NON_MATCHING_LIST [[_ [_ & range-expressions]]]
  {:type :non-matching :range (map parse-chunk range-expressions)})

(defmethod parse-chunk :MATCHING_LIST [[_ [_ & range-expressions]]]
  {:type :matching :range (map parse-chunk range-expressions)})


;; TODO: Parse namespace with parsing stuff in
;; Takes in the [:BRACKET_EXPRESSION part
(defn quick-n-dirty-transform [tree]

  (parse-chunk tree))

(def make-character-constraint
  (comp
   constrain-to-range
   quick-n-dirty-transform))
