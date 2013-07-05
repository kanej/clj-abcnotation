(ns clj-abcnotation.core
  (:require [instaparse.core :as insta]))

(def grammar
  "S = MOVEMENT (<WHITESPACE?> MOVEMENT)* 
   MOVEMENT = <'|:'> <WHITESPACE?> BARS <WHITESPACE?> <':|'>
   <BARS> = BAR (<WHITESPACE?>  <'|'> <WHITESPACE?> BAR  )*
   BAR = NOTE (<WHITESPACE?> NOTE)*
   NOTE = LETTER DIGIT?
   <LETTER> = #'[a-gA-G]' 
   <DIGIT> = #'[0-9]' 
   <WHITESPACE> = #'\\s+'")

(def letter->note 
  { "A" :A4 "B" :b4 "C" :c4 "D" :d4 "E" :e4 "F" :f#4 "G" :g4 
    "a" :A5 "b" :B5 "c" :C5 "d" :D5 "e" :E5 "f" :F#5 "g" :G5 })

(defn note->tuple 
  ([letter]
   (note->tuple letter "1"))
  ([letter digit] 
     [(letter->note letter) (Integer/parseInt digit)]))

(def abc-parse (insta/parser grammar))

(defn parse [text]
  (insta/transform {:NOTE note->tuple :S vector}
    (abc-parse text)))

(defn flatten-to-notes [composition]
  (->> composition
       (mapcat rest)
       (mapcat rest)
       (vec)))
