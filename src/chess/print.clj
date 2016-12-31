(ns chess.print
  (:require [clojure.string :as s]
            [chess.rules :as rules]
            [chess.board :refer :all]
            [io.aviso.ansi :refer :all]))

(defn display-name [piece]
  (let [color (:color piece)
        offset (if (= color :black) 6 0)]
    (if-not piece
      "   "
      (str (if (= (:color piece) :black)
             (str bold-white-font black-bg-font)
             (str bold-black-font white-bg-font))
           " "
           ({:pawn   "P"
             :rook   "R"
             :knight "N"
             :queen  "Q"
             :bishop "B"
             :king   "K"} (:rank piece))
           " "
           reset-font))))

(defn board [pm]
  (println (str "  |" (s/join "|" (map #(str "  " % "  ") (range 0 8))) "|"))
  (println (str "--+" (s/join "+" (repeat 8 "-----")) "+"))
  (doseq [row (range 0 8)]
    (print row "|")
    (doseq [col (range 0 8)]
      (print (str " " (display-name (piece-at pm [col row])) " |")))
    (println (str "\n--+" (s/join "+" (repeat 8 "-----")) "+"))))

(defn moves [moves piece]
  (println (str "  |" (s/join "|" (map #(str "  " % "  ") (range 0 8))) "|"))
  (println (str "--+" (s/join "+" (repeat 8 "-----")) "+"))
  (doseq [row (range 0 8)]
    (print row "|")
    (doseq [col (range 0 8)]
      (print
        (cond
          (= (:pos piece) [col row]) (str " " (display-name piece) " |")
          (moves [col row]) (str "  " "X" "  |")
          :else "     |")))
    (println (str "\n--+" (s/join "+" (repeat 8 "-----")) "+"))))

(defn move-set [rank pos & [color]]
  (let [piece {:rank rank :pos pos :color (or color :white)}]
    (moves (rules/move-set piece) piece)))
