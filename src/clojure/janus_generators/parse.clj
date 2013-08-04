(ns janus-generators.parse
  (:require
   [instaparse.core :as insta]))

(defn load-parser []
  (insta/parser
   (slurp "grammars/basic")
   :start :S))

;; oooooops
(def parse (load-parser))


(def escaped-string->char
  (zipmap
          (vals char-escape-string)
          (keys char-escape-string)))


(def get-reps
  {"+" [1 :*]
   "*" [0 :*]})

(defn transform-simple-re
  ([body]
      [:SIMPLE_RE body [1 1]])
  ([body dupl-symbol]
     [:SIMPLE_RE body (get-reps dupl-symbol)]))

(def transform-map {
                    :ANY_CHAR (constantly :any-char)
                    :ORD_CHAR last
                    :ESCAPED_SPEC_CHAR last
                    :ESCAPED_CHAR #(escaped-string->char % (last %))
                    :ESCAPED_BE_CHAR #(escaped-string->char % (last %))
                    :SINGLE_EXPRESSION identity
                    :BRACKET_EXPRESSION identity
                    :RANGE_EXPRESSIONS vector
                    :RANGE_EXPRESSION (fn [first _ last] [first last])
                    :BASIC_BE_CHAR last
                    :DUPL_SYMBOL identity
                    :MATCHING_LIST (partial vector :matching)
                    :NON_MATCHING_LIST (partial vector :non-matching)
                    :SIMPLE_RE transform-simple-re
                    :GROUP identity})

(comment

(defn character-to-int [[char-type value]]
  (case char-type
        :BASIC_BE_CHAR
        (int (first value))
        :ESCAPED_BE_CHAR
        (or (escape-string-int value)
            (int (last value)))))

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
    {:type :matching :range (map parse-chunk range-expressions)}))



(def transform-regex-tree (partial insta/transform transform-map))

(def parse-regex (comp transform-regex-tree parse str))
