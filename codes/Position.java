/**
 * @author mdsinalpha
 */

public class Position {
    private final double x, y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString() {
        return (float)x + ":" + (float) y;
    }


    public boolean equals(Position obj) {
        return (float) x ==  (float)obj.getX() && (float) y == (float) obj.getY();
    }
}
