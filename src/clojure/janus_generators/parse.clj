(ns janus-generators.parse
  (:require
   [instaparse.core :as insta]))

(defn load-parser []
  (insta/parser
   (slurp "grammars/basic")
   :start :S))

;; oooooops
(def parse (load-parser))
