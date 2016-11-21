(ns chess.core
  (:require [clojure.string :as s]))

(def specials [:rook :knight :bishop :queen
               :king :bishop :knight :rook])

(defn px [pm] (get (:pos pm) 0))
(defn py [pm] (get (:pos pm) 1))

(defn generate []
  (letfn [(fill-row [color row pieces]
            (map #(vector [%2 row]
                          {:rank  %1
                           :color color
                           :pos [%2 row]})
                 pieces
                 (range 0 8)))]
    (into {}
          (concat (fill-row :black 1 (repeat :pawn))
                  (fill-row :white 6 (repeat :pawn))
                  (fill-row :black 0 specials)
                  (fill-row :white 7 specials)))))
