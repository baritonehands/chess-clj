(ns chess.board)

(def specials [:rook :knight :bishop :queen
               :king :bishop :knight :rook])

(defn px [piece] (get (:pos piece) 0))
(defn py [piece] (get (:pos piece) 1))

(defn generate []
  (letfn [(fill-row [color row pieces]
            (map #(vector [%2 row]
                          {:rank  %1
                           :color color
                           :pos   [%2 row]})
                 pieces
                 (range 0 8)))]
    {:pieces (into {}
                   (concat (fill-row :black 1 (repeat :pawn))
                           (fill-row :white 6 (repeat :pawn))
                           (fill-row :black 0 specials)
                           (fill-row :white 7 specials)))
     :player :white}))

(defn piece-at [board pos]
  (get-in board [:pieces pos]))

(defn empty-at? [board pos]
  (not (piece-at board pos)))

(defn move [board from to]
  (let [piece (piece-at board from)]
    (-> (assoc-in board [:pieces to] (assoc piece :pos to))
        (update :pieces dissoc from)
        (update :player #(if (= % :white) :black :white)))))
