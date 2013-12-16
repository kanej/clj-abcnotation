(ns clj-abcnotation.core
  (:require [instaparse.core :as insta]))

(def grammar
  "S = INFORMATIONFIELD* MOVEMENTS
   <INFORMATIONFIELD> = idline | titleline | authorline | sourceline | rythmnline | meterline | unitnotelengthline | keyline
   idline = <'X:'> <WHITESPACE?> DIGIT+ <ENDLINE>
   titleline = <'T:'> FREETEXT <ENDLINE>
   authorline = <'Z:'> FREETEXT <ENDLINE>
   sourceline = <'S:'> FREETEXT <ENDLINE>
   rythmnline = <'R:'> FREETEXT <ENDLINE>
   meterline = <'M:'> FREETEXT <ENDLINE>
   unitnotelengthline = <'L:'> FREETEXT <ENDLINE>
   keyline = <'K:'> FREETEXT <ENDLINE>

   MOVEMENTS = MOVEMENT (<WHITESPACE?> MOVEMENT)*
   <MOVEMENT> = <'|:'> <WHITESPACE?> BARS <WHITESPACE?> <':|'>
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

(defn string-trim [key]
  (fn [text] [key (.trim text)]))

(defn collapse-movements [movs x y]
  [:sun movs])

(def abc-parse (insta/parser grammar))

(defn vec->map [v]
  (apply hash-map (apply concat v)))

(defn parse [text]
  (->> text
    (abc-parse)
    (insta/transform
      {:NOTE               note->tuple
       :S                  vector
       :idline             idline->id
       :titleline          (string-trim :title)
       :authorline         (string-trim :author)
       :sourceline         (string-trim :source)
       :rythmnline         (string-trim :rythmn)
       :meterline          (string-trim :meter)
       :unitnotelengthline (string-trim :keynotelength)
       :keyline            (string-trim :key)
       :MOVEMENTS          (fn [& movs] [:notes movs])
      })
    (vec->map)
       ))

(comment (defn flatten-to-notes [composition]
   (->> (:notes composition)
        (mapcat rest)
        vec)))
