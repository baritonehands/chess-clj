(ns chess.board)

(def specials [:rook :knight :bishop :queen
               :king :bishop :knight :rook])

(def all-moves (vec (for [row (range 0 8)
                          col (range 0 8)]
                      [col row])))

(defn px [piece] (get (:pos piece) 0))
(defn py [piece] (get (:pos piece) 1))

(defn generate []
  (letfn [(fill-row [color row pieces]
            (map #(vector [%2 row]
                          {:rank   %1
                           :color  color
                           :pos    [%2 row]
                           :moved? false})
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

(defn filter-pieces [board & more]
  (let [by (->> (partition 2 more)
                (map vec)
                (into {}))
        include (fn [piece]
                  (every? #(or (not (% by)) (= (% by) (% piece))) (keys by)))]
    (filter include (vals (:pieces board)))))

(defn move [board from to]
  (let [piece (piece-at board from)]
    (-> (assoc-in board [:pieces to] (assoc piece :pos to :moved? true))
        (update :pieces dissoc from)
        (update :player #(if (= % :white) :black :white)))))
