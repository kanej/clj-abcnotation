(ns clj-abcnotation.core-test
  (:require [clojure.test :refer :all]
            [clj-abcnotation.core :refer :all]))

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


(def single-bar "|: def :|")

(deftest parsing-notes
  (testing "Parse the first bar"
    (is (= (parse single-bar) {:MOVEMENTS [:MOVEMENT [:BAR [:D5 1] [:E5 1] [:F#5 1]]]} ))))

(deftest parsing-full-piece
  (testing "Parse the butterfly including its information fields"
    (let [song (parse butterfly)]
      (is (= 1 (:id song))))))
