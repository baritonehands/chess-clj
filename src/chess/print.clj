(ns chess.print
  (:require [clojure.string :as s]
            [chess.rules :as rules]))

(defn display-name [piece]
  (if-not piece
    ".."
    (str (s/upper-case (first (name (:color piece))))
         ({:pawn   "P"
           :rook   "R"
           :knight "N"
           :queen  "Q"
           :bishop "B"
           :king   "K"} (:rank piece)))))

(defn board [pm]
  (println (str "+" (s/join "+" (repeat 8 "----")) "+"))
  (doseq [row (range 0 8)]
    (print "|")
    (doseq [col (range 0 8)]
      (print (str " " (display-name (pm [col row])) " |")))
    (print "\n"))
  (println (str "+" (s/join "+" (repeat 8 "----")) "+")))

(defn move-set [rank pos & [color]]
  (let [moves (rules/move-set {:rank rank :pos pos :color (or color :white)})]
    (println (str "+" (s/join "+" (repeat 8 "---")) "+"))
    (doseq [row (range 0 8)]
      (print "|")
      (doseq [col (range 0 8)]
        (print
          (cond
            (= pos [col row]) (str " " "0" " |")
            (moves [col row]) (str " " "X" " |")
            :else " . |")))
      (print "\n"))
    (println (str "+" (s/join "+" (repeat 8 "---")) "+"))))