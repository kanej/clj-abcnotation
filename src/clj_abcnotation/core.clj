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

(def letter->pitch
  { "C," -7 "D," -6 "E," -5 "F," -4 "G," -3 "a," -2 "b," -1
    "C"   0 "D"   1 "E"   2 "F"   3 "G"   4 "A"   5 "B"   6
    "c"   7 "d"   8 "e"   9 "f"  10 "g"  11 "a"  12 "b"  13
    "c'" 14 "d'" 15 "e'" 16 "f'" 17 "g'" 18 "a'" 19 "b'" 20})

(defn note->map
  ([letter]
   (note->map letter "1"))
  ([letter digit]
     {:note letter :pitch (letter->pitch letter) :duration (Integer/parseInt digit) :part nil :time nil}))

(defn idline->id [id-as-string]
  [:id (Integer/parseInt id-as-string)])

(defn string-trim [key]
  (fn [text] [key (.trim text)]))

(defn collapse-movements [movs x y]
  [:sun movs])

(def abc-parse (insta/parser grammar))

(defn vec->map [v]
  (apply hash-map (apply concat v)))

(defn interpolate-timings-into-bar [[time bars] [_ & notes]]
  (let [durations (map :duration notes)
        [updated-time timings] (reduce
                                (fn [[time timings] duration] [(+ time duration) (conj timings time)])
                                [time []]
                                durations)
        updated-notes (map merge notes (map #(hash-map :time %) timings))
        update-bar (into [] (cons :BAR updated-notes))]
    [updated-time (conj bars update-bar)]))

(defn interpolate-timings [{:keys [notes] :as notation}]
  (let [[_ updated-notes] (reduce interpolate-timings-into-bar [0 []] notes)]
    (assoc notation :notes updated-notes)))

(defn parse [text]
  (->> text
    (abc-parse)
    (insta/transform
      {:NOTE               note->map
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
    (interpolate-timings)))
