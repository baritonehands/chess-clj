(ns chess.core
  (:require [chess.rules :as rules]
            [chess.board :as board]))

(defn move [board from to]
  (let [piece (board/piece-at board from)
        moves (rules/move-set piece)]
    (if (and (= (:player board) (:color piece))
             (>= (compare to [0 0]) 0)
             (<= (compare to [7 7]) 0)
             (moves to)
             (rules/path-clear? board from to))
      (board/move board from to)
      board)))
