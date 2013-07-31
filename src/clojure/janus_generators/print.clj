(ns janus-generators.print)

(defn print-leaf [indent-text leaf] 
  (println indent-text leaf))

(defn print-regex-tree
  ([tree] (print-regex-tree 0 tree))
  ([indent [current & children]]
     (let [indent-text (apply str (take indent (repeat " ")))
           non-vector-children (remove vector? children)
           vector-children (filter vector? children)]
       (println indent-text current)
       (if (seq non-vector-children)
         (println indent-text " " (vec non-vector-children)))
       (doseq [child vector-children]
         (print-regex-tree (inc indent) child)))))
