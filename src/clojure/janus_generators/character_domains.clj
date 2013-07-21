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

;; Constructs domains based on regex tree
(defmulti make-domains first)

  ;; constrains the result to be in the range from/to the single expr... has to stay as an interval rather than = 'cos they are combined into multiinterval later with other potentially ranged expressions like [A-Z5] is range A-Z or a 5

(defmethod make-domains :SINGLE_EXPRESSION [[_  character]]
  (let [character-value (character-to-int character)]
    (fd/interval character-value character-value)))

(defmethod make-domains :RANGE_EXPRESSION [[_  [_ from] _ [_ to]]]
  ;; constrains the result to be in the range from/to
  (fd/interval (character-to-int from) (character-to-int to)))

(defmethod make-domains :NON_MATCHING_LIST [[_ _ matching-list]]
  (let [matching-domain (make-domains matching-list) ]
    (fd/difference (any-char-domain) matching-domain)))

;; Style fix: [AAAAB] is valid but I want to treat as [A]
(defmethod make-domains :MATCHING_LIST [[_ & expressions :as tmp]]
  (let [subterms  (map make-domains (distinct expressions)) ] 
;; we sort because when fd/difference runs, order is important
    (apply fd/multi-interval subterms)))

;; TODO: This is just for BE
(defn make-character-constraint [expression]
  (let [domain (make-domains expression)]
    (fn [result]
      (fd/in result domain))))

;; TODO: This is just for a single cahracter out of a BE

(defn constrain-to-char []
  #(fd/in % (any-char-domain)))
