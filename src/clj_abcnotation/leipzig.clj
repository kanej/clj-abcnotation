(ns clj-abcnotation.leipzig
  (:require [overtone.live :as overtone]
            [leipzig.melody :refer [bpm is phrase then times where with]]
            [leipzig.scale :as scale]
            [leipzig.canon :as canon]
            [leipzig.chord :as chord]
            [leipzig.live :as live]


            [clj-abcnotation.core :as notation])
  (:use [overtone.inst.piano]))

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

(defmethod live/play-note :piano [{midi :pitch}] (piano midi))

(defmethod live/play-note :leader [{midi :pitch}]
  (-> midi overtone/midi->hz beep))

(defmethod live/play-note :follower [{midi :pitch}]
  (-> midi overtone/midi->hz ping))

(defmethod live/play-note :bass [{midi :pitch seconds :duration}]
  (-> midi overtone/midi->hz (/ 2) (seeth seconds)))

(def scale "|: CDEFGabcdefedcbaGFEDC :|")

(def row-row-row-your-boat
  "|: C3 C3|C2D E3|E2D E2F|G6|
   ccc GGG|EEE CCC|G2F E2D|C6:|")

(def butterfly "X: 1
T: Butterfly, The
Z: Jeremy
S: http://thesession.org/tunes/10#setting10
R: slip jig
M: 9/8
L: 1/8
K: Emin
|:B2E G2E F3|B2E G2E FED|B2E G2E F3|B2d d2B AFD:|")

(defn to-melody [abc]
  (->> abc
      notation/parse
      :notes
      (mapcat rest)))

(defn play-tune [abc speed key]
  (->> (to-melody abc)
    ;;(canon/canon (comp (canon/simple 4) (partial where :part (is :follower))))
    ;;(with bass)
    (where :time speed)
    (where :duration speed)
    (where :pitch key)
    (where :part (is :piano))
    live/play))

(comment
  (play-tune butterfly (bpm 240) (comp scale/E scale/minor))
  (play-tune row-row-row-your-boat (bpm 240) (comp scale/C scale/major))
  (play-tune scale (bpm 240) (comp scale/C scale/major))
  )

