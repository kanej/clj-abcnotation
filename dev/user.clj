(ns user
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer :all]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [instaparse.core :as insta]

            [clj-abcnotation.core :as notation]))

(def butterfly
  "X: 1
T: Butterfly, The
Z: Jeremy
S: http://thesession.org/tunes/10#setting10
R: slip jig
M: 9/8
L: 1/8
K: Emin
|:B2E G2E F3|B2E G2E FED|B2E G2E F3|B2d d2B AFD:|
|:B2d e2f g3|B2d g2e dBA|B2d e2f g2a|b2a g2e dBA:|
|:B3 B2A G2A|B3 BAB dBA|B3 B2A G2A|B2d g2e dBA:|")

(def parsed-butterfly (notation/parse butterfly))

(comment
  (notation/parse butterfly)
  )


(def bar [:BAR
          {:note "B", :pitch 6, :duration 2, :part nil, :time nil}
          {:note "d", :pitch 8, :duration 1, :part nil, :time nil}
          {:note "g", :pitch 11, :duration 2, :part nil, :time nil}
          {:note "e", :pitch 9, :duration 1, :part nil, :time nil}
          {:note "d", :pitch 8, :duration 1, :part nil, :time nil}
          {:note "B", :pitch 6, :duration 1, :part nil, :time nil}
          {:note "A", :pitch 5, :duration 1, :part nil, :time nil}])

(def bars {:notes [bar bar]})

(defn flatten-to-notes [composition]
   (->> (:notes composition)
        (mapcat rest)
        vec))
