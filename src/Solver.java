import java.awt.*;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Solver {
    /**
     * Given an initial-state Board, we can
     * generate a bunch of possible Moves.
     * We stick each Move into our Queue, and
     * then we pull each move out, and generate a new
     * state of the board, and repeat.
     */
    public void solveBoard(Board board) {

        // Get the initial set of moves from the initial board.
        PriorityQueue<Move> available = getAvailableMoves(board);

        // Set aside memory for a working board state. This will be
        // changing as we search the board states.
        Board workingBoard = board;

        // Pull out the first move from the queue.
        Move move = available.poll();

        // Start a counter on how many times we reach into the bag.
        int statesSearched = 0;
        int terminals = 0;
        int best = 5;

        // Start looping
        do {
            // move will be "null" when we have an empty bag.
            if (move == null) {
                System.out.printf("No more moves!");
                // bail.
                return;
            }

            // we have a move pulled from the bag, what does the board look
            // like in this move?
            workingBoard = move.getUpdatedBoardAfterMove();

            // If the working board in this move is a solution, we are done.
            if (workingBoard.isSolved()) {
                System.out.print("SOLVED\n");
                workingBoard.printWorkingPast();
                return;
            } else {
                // Otherwise, generate a bunch of new moves from this state.
                // Put them back into the bag.
                PriorityQueue<Move> here = getAvailableMoves(workingBoard);
                if (here.size() == 0) {
                    if (terminals++ % 100000 == 0) {
                        System.out.println("dead end: " + workingBoard.score() + " : " + move.score());
                        System.out.println(workingBoard);
                    }
                    if (workingBoard.score() < best) {
                        best = workingBoard.score();
                        System.out.println("---------");
                        System.out.println("best game: " + best);
                        workingBoard.printWorkingPast();
                        System.out.println("---------");
                    }
                }
                available.addAll(here);
            }

            if (statesSearched % 1000000 == 0 ){
                System.out.println("going... " + statesSearched);
            }

            // Print out how many times we've gone into the bag.
            ++statesSearched;

            // Reach into the bag again and start over.
            move = available.poll();
        } while (move != null);


        System.out.println("out of moves");
    }

    public PriorityQueue<Move> getAvailableMoves(Board board) {
        // Get valid moves from open pegs.
        PriorityQueue<Move> moves = new PriorityQueue<>();
        board.pegs.values().forEach(peg -> {
            if (!peg.filled) {
                // up
                Point up = new Point(peg.point.x, peg.point.y - 2);
                Point upJumped = new Point(peg.point.x, peg.point.y - 1);
                if (validFrom(board, up) && validJump(board, upJumped)) {
                    moves.add(new Move(
                            board.pegs.get(up),
                            peg,
                            board.pegs.get(upJumped),
                            board));
                }

                // left
                Point left = new Point(peg.point.x - 2, peg.point.y);
                Point leftJumped = new Point(peg.point.x - 1, peg.point.y);
                if (validFrom(board, left) && validJump(board, leftJumped)) {
                    moves.add(new Move(
                            board.pegs.get(left),
                            peg,
                            board.pegs.get(leftJumped),
                            board));
                }

                // right
                Point right = new Point(peg.point.x + 2, peg.point.y);
                Point rightJumped = new Point(peg.point.x + 1, peg.point.y);
                if (validFrom(board, right) && validJump(board, rightJumped)) {
                    moves.add(new Move(
                            board.pegs.get(right),
                            peg,
                            board.pegs.get(rightJumped),
                            board));
                }

                // down
                Point down = new Point(peg.point.x, peg.point.y + 2);
                Point downJumped = new Point(peg.point.x, peg.point.y + 1);
                if (validFrom(board, down) && validJump(board, downJumped)) {
                    moves.add(new Move(
                            board.pegs.get(down),
                            peg,
                            board.pegs.get(downJumped),
                            board));
                }
            }
        });
        return moves;
    }

    private boolean validJump(Board board, Point toJump) {
        if (board.pegs.containsKey(toJump)) {
            return board.pegs.get(toJump).filled;
        }
        return false;
    }

    private boolean validFrom(Board board, Point from) {
        if (board.pegs.containsKey(from)) {
            return board.pegs.get(from).filled;
        }
        return false;
    }

    private Set<Peg> getOpenPegs(Board board) {
        Set<Peg> openPegs = new HashSet<>();
        board.pegs.forEach((point, peg) -> {
            if (!peg.filled) {
                openPegs.add(peg);
            }
        });
        return openPegs;
    }
}
