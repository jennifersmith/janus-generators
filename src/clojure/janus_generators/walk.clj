(ns janus-generators.walk
  (:require [clojure.zip :as zip]
            [janus-generators.parse :refer :all]))

(defmulti get-children first)

(defmethod get-children :S [[_ & branches]]
  branches)

(defmethod get-children :RE_BRANCH [[_ & body]]
  body)

(defmethod get-children :SIMPLE_RE [[_ body [min max]]]
  (let [children (repeat body)
        [head tail] (split-at min children)]
    (concat
     head
     (take max tail))))

(defmethod get-children :ONE_CHAR_RE [[_ character]]
  [character])

(defn hiccup-zipper [hiccup]
  (zip/zipper
   vector?
   rest
   (comp vec cons)
   hiccup))



(defn regex-tree [parsed-regex]
  (tree-seq vector? get-children parsed-regex))
