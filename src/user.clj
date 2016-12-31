(ns user
  (:require [chess.rules :as rules]
            [chess.board :as board]
            [chess.print :as print]
            [chess.core :refer :all]))

(defn move-all [& moves]
  (let [board (board/generate)]
    (reduce (partial apply move)
            board
            (partition 2 moves))))

(defn check-example []
  (move-all [4 6] [4 4]
            [1 0] [0 2]
            [5 7] [2 4]
            [7 1] [7 2]
            [2 4] [5 1]))

(defn checkmate-example []
  (move-all [4 6] [4 4]
            [1 0] [0 2]
            [5 7] [2 4]
            [7 1] [7 2]
            [3 7] [7 3]
            [6 0] [5 2]
            [7 3] [5 1]))
