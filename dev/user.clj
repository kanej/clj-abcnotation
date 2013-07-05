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
  "|:B2E G2E F3|B2E G2E FED|B2E G2E F3|B2d d2B AFD:|
   |:B2d e2f g3|B2d g2e dBA|B2d e2f g2a|b2a g2e dBA:|
   |:B3 B2A G2A|B3 BAB dBA|B3 B2A G2A|B2d g2e dBA:|")
