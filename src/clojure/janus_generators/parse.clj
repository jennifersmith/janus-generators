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
   "*" [0 :*]
   "?" [0 1]})

(defn transform-simple-re
  ([body]
      [:SIMPLE_RE body [1 1]])
  ([body dupl-symbol]
     [:SIMPLE_RE body (get-reps dupl-symbol)]))

(def code->charclass
  {"w" :alphanumeric})

(def transform-map {
                    :ANY_CHAR (constantly :any-char)
                    :ORD_CHAR last
                    :CHAR_CLASS code->charclass
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

(def transform-map {
                    :ANY_CHAR (constantly :any-char)
                    :ORD_CHAR last
                    :CHAR_CLASS code->charclass
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



(def transform-regex-tree (partial insta/transform transform-map))

(def parse-regex (comp transform-regex-tree parse str))
