import java.awt.*;
import java.util.stream.Stream;

public class Move implements Comparable {
    final Peg from;
    final Peg to;
    final Peg jumped;
    final Board fromBoard;
    final Board toBoard;

    public Move(Peg from, Peg to, Peg jumped, Board fromBoard) {
        this.from = from;
        this.to = to;
        this.jumped = jumped;
        this.fromBoard = fromBoard;
        this.toBoard = getUpdatedBoardAfterMove();
    }


    public Board getUpdatedBoardAfterMove() {
        if (toBoard != null) {
            return toBoard;
        }

        Board updated = fromBoard.clone();
        // Fill the "to" peg.
        updated.pegs.get(to.point).filled = true;

        // Unfill the "from" peg.
        updated.pegs.get(from.point).filled = false;

        // Unfill the "jumped" peg.
        updated.pegs.get(jumped.point).filled = false;

        updated.fromBoard = fromBoard;

        return updated;
    }

    Integer score() {
        final int[] score = {1};
        int filled = toBoard.score();
        int emptyCornerBonus = Board.PUZZLE_SIZE * filled;


        //final int[] score = {fromBoard.score() * 100};

        // Edge piece penalty
        for (int i = 0; i < Board.PUZZLE_SIZE; i++) {
            Stream.of(
                    new Point(0, i),
                    new Point(i, 0),
                    new Point(Board.NUM_ROWS - 1, i),
                    new Point(i, Board.NUM_COLS - 1)).forEach(
                    temp -> {
                        if (toBoard.pegs.containsKey(temp)) {
                            if (toBoard.pegs.get(temp).filled) {
                                score[0] -= emptyCornerBonus;
                            }
                        }
                    }
            );
        }

        // Distance from center penalty.
        Point center = new Point(Board.NUM_ROWS / 2, Board.NUM_COLS / 2);
        toBoard.pegs.forEach((point, peg) -> {
            if (peg.filled) {
                score[0] -= point.distance(center);
            }
        });

        // Middle ball bonus
        if (toBoard.pegs.get(center).filled) {
            score[0] += Board.PUZZLE_SIZE * 5;
        }

        // Proximity bonus
        Stream.of(
                new Point(to.point.x - 1, to.point.y),
                new Point(to.point.x + 1, to.point.y),
                new Point(to.point.x, to.point.y - 1),
                new Point(to.point.x, to.point.y + 1))
                .forEach(nearby -> {
                    if (toBoard.pegs.containsKey(nearby) && toBoard.pegs.get(nearby).filled){
                        score[0] += Board.PUZZLE_SIZE * 2;
                    }
                });

        return (-1 * score[0]);
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode() + jumped.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        return ((Move) o).from.equals(from)
                &&((Move) o).to.equals(to)
                &&((Move) o).jumped.equals(jumped);
    }

    @Override
    public int compareTo(Object o) {
        Move other = (Move) o;
        return score().compareTo(other.score());
    }
}
