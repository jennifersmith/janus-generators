(ns janus-generators.core
  (:import [janus_generators.grammars HelloParser
            HelloLexer
            HelloBaseListener ]
           [org.antlr.v4.runtime.tree RuleNode TerminalNode]
           [org.antlr.v4.runtime
            ANTLRInputStream
            CommonTokenStream
            TokenStream]))

(defn get-tree-children [node]
  (for [i (range (.getChildCount node))]
    (.getChild node i)))

;; TODO: Could I use some sort of zipper for this?
(defmulti tree-to-map class)

(defmethod tree-to-map RuleNode [tree]
  {:rule-index (.getRuleIndex tree)
   :children  (map tree-to-map (get-tree-children tree))})

(defmethod tree-to-map TerminalNode [node]
  (let [symbol (.getSymbol node)]
    (when symbol
      (.getText symbol))))

(defn lex [mystr]
  (let [
        input (new ANTLRInputStream (char-array mystr) (count mystr))
        lexer (new HelloLexer input)]
    (new CommonTokenStream lexer)))

(defn parse-stuffs [mystr]
  (let [
        tokens (lex mystr)
        parser (new HelloParser tokens)
        tree (.r parser)]
    (tree-to-map tree)))


