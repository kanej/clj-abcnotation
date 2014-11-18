(ns clj-abcnotation.leipzig
  (:require [overtone.live :as overtone]
            [leipzig.melody :refer [bpm is phrase then times where with]]
            [leipzig.scale :as scale]
            [leipzig.canon :as canon]
            [leipzig.chord :as chord]
            [leipzig.live :as live]

            [clj-abcnotation.core :as notation]))

(overtone/definst beep [freq 440]
  (-> freq
      overtone/saw
      (* (overtone/env-gen (overtone/perc) :action overtone/FREE))))

(overtone/definst ping [freq 440]
  (-> freq
      overtone/square
      (* (overtone/env-gen (overtone/perc) :action overtone/FREE))))

(overtone/definst seeth [freq 440 dur 1.0]
  (-> freq
      overtone/saw
      (* (overtone/env-gen (overtone/perc (* dur 1/2) (* dur 1/2)) :action overtone/FREE))))

(defmethod live/play-note :leader [{midi :pitch}]
  (-> midi overtone/midi->hz beep))

(defmethod live/play-note :follower [{midi :pitch}]
  (-> midi overtone/midi->hz ping))

(defmethod live/play-note :bass [{midi :pitch seconds :duration}]
  (-> midi overtone/midi->hz (/ 2) (seeth seconds)))


(def butterfly "X: 1
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

(def melody
  (->> butterfly
      notation/parse
      :notes
      (mapcat rest)))

(defn play-tune [speed key]
  (->> melody
    ;;(canon/canon (comp (canon/simple 4) (partial where :part (is :follower))))
    ;;(with bass)
    (where :time speed)
    (where :duration speed)
    (where :pitch key)
    (where :part (is :leader))
    live/play))

(comment (play-tune (bpm 120) (comp scale/E scale/minor)))
