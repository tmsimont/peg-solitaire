import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
    Map<Point, Peg> pegs;
    Board fromBoard;

    public final static int PUZZLE_SIZE = 5;
    public final static int NUM_ROWS = PUZZLE_SIZE;
    public final static int NUM_COLS = PUZZLE_SIZE;
    public final static int MAX_WIDTH = 3;
    private static Set<Point> cornersCut;

    Board() {
        buildBoard();
    }

    public static Set<Point> cornersCut() {
        if (cornersCut == null) {
            Set<Point> excludedPoints = new HashSet<>();
            int edgeWidth = (NUM_ROWS - MAX_WIDTH) / 2;
            int j = 0;
            while (edgeWidth > 0) {
                for (int i = 0; i < edgeWidth; i++) {
                    excludedPoints.add(new Point(i, j));
                    excludedPoints.add(new Point(NUM_ROWS - (1 + i), j));
                    excludedPoints.add(new Point(j, NUM_COLS - (1 + i)));
                    excludedPoints.add(new Point(NUM_ROWS - (1 + j), NUM_COLS - (1 + i)));
                }
                edgeWidth--;
                j++;
            }
            cornersCut = excludedPoints;
        }
        return cornersCut;
    }

    private void buildBoard() {
        pegs = new HashMap<>();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Point point = new Point(row, col);
                Peg peg = new Peg(point);
                pegs.put(point, peg);
                peg.filled = true;
            }
        }
        pegs.get(new Point(NUM_ROWS / 2, NUM_COLS / 2)).filled = false;
        cornersCut().forEach(point -> pegs.remove(point));
    }

    public Board clone() {
       Board clone = new Board();
       clone.pegs.clear();
       pegs.forEach((k,v) -> clone.pegs.put(new Point(k.x, k.y), v.clone()));
       return clone;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Point point = new Point(row, col);
                if (pegs.containsKey(point)) {
                    out.append(pegs.get(point));
                } else {
                    out.append(" ");
                }
                out.append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    boolean isSolved() {
        return score() == 1;
    }

    int score() {
        final int[] filled = {0};
        pegs.values().forEach(peg -> {
            if (peg.filled) {
                filled[0]++;
            }
        });
        return filled[0];
    }


    void printWorkingPast() {
        int depth = 0;
        Board workingBoard = this;
        while (workingBoard != null) {
            System.out.println("d-" + depth);
            System.out.println(workingBoard);
            workingBoard = workingBoard.fromBoard;
            depth++;
        }
        System.out.println(depth);
    }
}
