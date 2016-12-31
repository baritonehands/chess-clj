# chess

An implementation of chess in Clojure.

## Usage

All the interesting namespaces are included in the user.clj file. When running a REPL...

Print a board:

```clojure
(print/board (board/generate))
```

Print possible moves for a piece:

```clojure
(print/move-set :queen [4 4]) ; default last arg is :white
(print/move-set :pawn [4 4] :black)
```

Generate a check:

```clojure
(def board (check-example))
(print/board board)
```

Generate a checkmate:

```clojure
(def board (checkmate-example))
(print/board board)
```

## License

Copyright © 2016 Brian Gregg

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
