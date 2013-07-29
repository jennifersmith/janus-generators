(ns janus-generators.output)

(defn convert-to-string [s]
  (apply str  (map char (flatten s))))
