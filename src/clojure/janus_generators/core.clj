(ns janus-generators.core
  (:require [instaparse.core :as insta]))

(defn load-parser []
  (insta/parser
   (slurp "grammars/basic")))

(defn parse [v]
  (let [parser (load-parser)]
    (parser v)))
