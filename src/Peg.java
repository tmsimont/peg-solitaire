import java.awt.*;

public class Peg {
    final Point point;
    boolean filled;

    public Peg(Point point) {
        this.point = point;
    }

    public String toString() {
        if (filled) {
            return "·";
        } else {
            return "o";
        }
    }

    public Peg clone() {
        Peg clone = new Peg(new Point(point.x, point.y));
        clone.filled = filled;
        return clone;
    }

    @Override
    public int hashCode() {
        return point.hashCode() + (filled ? 1000 : 2000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        return ((Peg) o).point.equals(point) && filled == ((Peg) o).filled;
    }
}
