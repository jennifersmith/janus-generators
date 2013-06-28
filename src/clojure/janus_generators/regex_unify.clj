(ns janus.regex-unify
    (:refer-clojure :exclude [==])
    (:use clojure.core.logic)
    (:require [clojure.core.logic :as logic]))
(def ^:dynamic *debug* false)

(defmacro log-with-more [& s]
`(fn [a#]
   (if *debug*
     (println ~@s {:substitutions a#}))
     a#))

(defmacro log-state [message & interesting-state]
  `(fn [subs#]
     (if *debug*
       (let [state# (:s subs#)]
         (println [~message
                   [~@interesting-state]
                   (select-keys state# [~@interesting-state] )
                   state#]))) subs# ))

(defn make-char-set [start end]
  (vec (map char (range (int start) (inc (int end))))))

(defn charo [q chr matched]
  (logic/conde [(logic/== q chr)
                (logic/== matched true)]
               [(logic/!= q chr)
                (logic/== matched false)]))

(defn containso [q sequence found]
  (logic/conde [(logic/== true (empty? sequence))
                (logic/== false found)]
               [(logic/!= nil (first sequence))
                (logic/== q (first sequence))
                (logic/== true found)]
               [(if (empty? sequence)
                  logic/fail
                  (containso q (rest sequence) found))]))

(defn matching-stringo [q chr-set length]
  (logic/== q (apply str (take length (repeatedly #(nth chr-set (rand-int (count chr-set))))))))


(defn repeato [min max char-set q matched]
  (letfn [(next [length]
            (logic/conde [(logic/== true matched)
                          (matching-stringo q char-set length)]
                         [(logic/== false matched)
                          (logic/== (apply str (repeat length "b")) q)]
                         [(logic/!= length max)
                          (next (inc length))]))]
    (next min)))


;; A+

(defn charactero [character-domain match]
  (conde
   ((emptyo character-domain) fail)
   ((fresh [c,r]
           (conso c r character-domain)
           (conde
            ((== c match))
            ((!= r '()) (charactero r match)))))))

(defn character-classo [regex-params match]
  (fresh [character-domain, root]
         (log-state "character-classo" match)
         (== regex-params (partial-map {:domain character-domain}))
         (fresh [h r]
                (conso h r match)
                (emptyo r)
                (charactero character-domain h)
                         (log-state "character-classo-two" match h r))))

(declare regex-matcho)

(defn pluso [inner match]
(logic/all
 (logic/fresh [h]
        (logic/firsto match h)
        (regex-matcho inner h))
   (logic/fresh [r]
          (logic/resto match r)
          (logic/conde
           ((logic/== r '()))
           ((pluso inner r))))))

(defn quantificationo [regex-params match]
                 (fresh [ children, child]
                        (== regex-params
                            (partial-map
                             {:children children}))
                        (firsto children child)
                        (resto children [])
                        (pluso child match)))

(defn optionalo [regex-params match]
                   (fresh [ children, child]
                        (== regex-params
                            (partial-map
                             {:children children}))
                        (firsto children child)
                        (resto children [])
                        (conde
                         ((regex-matcho child match))
                         ((== match '())))))

(defn sequenco-foo [children match]
   (conde
          ((emptyo children) (== match '()))
          ( (fresh [child remaining-children head remaining-match]
                   (conso head remaining-match match)
                   (conso child remaining-children children)
                   (regex-matcho child head )
                   (sequenco-foo remaining-children remaining-match)
                   ))))

(defn sequenco [regex-params match]
  (fresh [children]
         (log-state "sequenco" match)
         (== regex-params {:children children})
         (sequenco-foo children match)
         (log-state "sequenco2" match)))

(defn optiono2 [children match]
  (conde
   ((emptyo children) fail)
   ((fresh [child remaining-children]
           (conso child remaining-children children)
           (conde
            ((regex-matcho child match))
            ((optiono2 remaining-children match)))))))

(defn optiono [regex-params match]
  (fresh [children]
         (== regex-params {:children children})
         (log-state "hai" children)
         (optiono2 children match)))


(defn regex-matcho [regex match]
  (fresh [pm operator params]
         (log-with-more "regex-matcho"  )
         (== pm
             (partial-map
              {:operator operator
               :params params}))
         (== regex pm)
         (matche [operator]
                 [[:at-least-one] (quantificationo params match)]
                 [[:character-class] (character-classo params match)]
                 [[:sequence] (sequenco params match) (log-state "end" match) ]
                 [[:option] (optiono params match)]
                 [[:optional] (optionalo params match)])))
