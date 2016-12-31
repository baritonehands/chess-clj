(ns chess.core
  (:require [chess.rules :as rules]
            [chess.board :as board]))

(defn move [board from to]
  (let [piece (board/piece-at board from)
        new-board (board/move board from to)]
    (if (and (= (:player board) (:color piece))
             (>= (compare to [0 0]) 0)
             (<= (compare to [7 7]) 0)
             (rules/valid-move? board piece to)
             (not (rules/in-check? new-board (:color piece))))
      new-board
      (do
        (println "INVALID MOVE!!!")
        board))))
