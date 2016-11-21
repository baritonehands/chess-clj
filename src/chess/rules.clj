(ns chess.rules
  (require [chess.core :refer :all]
           [clojure.set :as set]))

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
    (if (#{1 6} (py piece)) ; Initial row, allow 2 spaces
      (conj moves [(px piece) (fy (fy (py piece)))])
      moves)))
