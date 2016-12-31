(ns chess.rules
  (require [chess.board :refer [px py]]
           [clojure.set :as set]
           [chess.board :as board]))

(defmulti move-set :rank)

(defmethod move-set :default [_] #{})

(defn rook-set [piece]
  (set (for [row (range 0 8)
             col (range 0 8)
             :when (or (= row (py piece))
                       (= col (px piece)))]
         [col row])))

(defmethod move-set :rook [piece] (rook-set piece))

(defn bishop-set [piece]
  (set (for [row (range 0 8)
             col (range 0 8)
             :when (or (= (- row col) (- (py piece) (px piece)))
                       (= (+ row col) (+ (py piece) (px piece))))]
         [col row])))

(defmethod move-set :bishop [piece] (bishop-set piece))

(defmethod move-set :queen [piece]
  (set/union (rook-set piece) (bishop-set piece)))

(defmethod move-set :king [piece]
  (set (for [dx [-1 0 1]
             dy [-1 0 1]
             :when (or (not= dx 0) (not= dy 0))]
         [(+ (px piece) dx) (+ (py piece) dy)])))

(defmethod move-set :knight [piece]
  (set (for [[dx dy] [[1 2] [2 1] [-1 2] [2 -1] [1 -2] [-2 1] [-1 -2] [-2 -1]]]
         [(+ dx (px piece)) (+ dy (py piece))])))

(defmethod move-set :pawn [piece]
  (let [fy (if (= (:color piece) :white) dec inc)
        moves (set (for [dx [-1 0 1]]
                     [(+ dx (px piece)) (fy (py piece))]))]
    (if (#{1 6} (py piece))                                 ; Initial row, allow 2 spaces
      (conj moves [(px piece) (fy (fy (py piece)))])
      moves)))

(defmulti path-clear?
          (fn [board from to] (:rank (board/piece-at board from))))

(defmethod path-clear? :knight [board from to]
  (let [piece (board/piece-at board from)
        target (board/piece-at board to)]
    (or (not target)
        (not= (:color piece) (:color target)))))

(defmethod path-clear? :pawn [board from to]
  (let [piece (board/piece-at board from)
        target (board/piece-at board to)
        dy (fn [[_ fy] [_ ty]]
             ({-2 -1 2 1} (- fy ty)))]
    (or (and target
             (not= (:color piece) (:color target))
             (not= (px piece) (px target)))
        (and (not target)
             (if (dy from to)
               (board/empty-at? board [(first to) (+ (second to) (dy from to))])
               true)))))

(defn abs-range [a b]
  (if (> a b)
    (reverse (range (inc b) a))
    (seq (range (inc a) b))))

(defn path [[fx fy] [tx ty]]
  (let [xs (abs-range fx tx)
        ys (abs-range fy ty)]
    (cond
      (and xs ys) (map vector xs ys)
      xs (map #(vector % fy) xs)
      ys (map #(vector fx %) ys))))

(defmethod path-clear? :default [board from to]
  (let [piece (board/piece-at board from)
        target (board/piece-at board to)]
    (and (or (not target)
             (not= (:color piece) (:color target)))
         (every? #(board/empty-at? board %) (path from to)))))

(defn valid-move? [board piece to]
  (and
    ((move-set piece) to)
    (path-clear? board (:pos piece) to)))

(defn valid-moves [board piece]
  (filter (partial valid-move? board piece) board/all-moves))

(defn in-check? [board color]
  (let [king (first (board/filter-pieces board :color color :rank :king))
        opp (board/filter-pieces board :color (if (= :white color) :black :white))
        capturers (filter #(valid-move? board % (:pos king)) opp)]
    (not (empty? capturers))))

(defn checkmate? [board color]
  (not-any? (fn [piece]
              (->> (valid-moves board piece)
                   (map #(in-check? (board/move board (:pos piece) %) color))
                   (some false?)))
            (board/filter-pieces board :color color)))
