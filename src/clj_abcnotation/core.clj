(ns clj-abcnotation.core
  (:require [instaparse.core :as insta]))

(def grammar
  "S = INFORMATIONFIELD* MOVEMENTS
   <INFORMATIONFIELD> = idline | TITLELINE | AUTHORLINE | SOURCELINE | RYTHMNLINE | METERLINE | UNITNOTELENGTHLINE | KEYLINE
   idline = <'X:'> <WHITESPACE?> DIGIT+ <ENDLINE>
   TITLELINE = <'T:'> FREETEXT <ENDLINE> 
   AUTHORLINE = <'Z:'> FREETEXT <ENDLINE> 
   SOURCELINE = <'S:'> FREETEXT <ENDLINE>
   RYTHMNLINE = <'R:'> FREETEXT <ENDLINE>
   METERLINE = <'M:'> FREETEXT <ENDLINE>
   UNITNOTELENGTHLINE = <'L:'> FREETEXT <ENDLINE>
   KEYLINE = <'K:'> FREETEXT <ENDLINE>

   MOVEMENTS = MOVEMENT (<WHITESPACE?> MOVEMENT)* 
   MOVEMENT = <'|:'> <WHITESPACE?> BARS <WHITESPACE?> <':|'>
   <BARS> = BAR (<WHITESPACE?>  <'|'> <WHITESPACE?> BAR  )*
   BAR = NOTE (<WHITESPACE?> NOTE)*
   NOTE = LETTER DIGIT?
   <FREETEXT> = #'[a-zA-Z0-9\\,\\:\\/\\.\\#\\u0020]+'
   <ENDLINE> = '\\n'
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

(defn idline->id [id-as-string]
  [:id (Integer/parseInt id-as-string)])

(def abc-parse (insta/parser grammar))

(defn vec->map [v]
  (apply hash-map (apply concat v)))

(defn parse [text]
  (->> text
    (abc-parse) 
    (insta/transform 
      {:NOTE note->tuple 
       :S vector
       :idline idline->id})
    (vec->map)))

(defn flatten-to-notes [composition]
  (->> composition
       (mapcat rest)
       (mapcat rest)
       (vec)))
