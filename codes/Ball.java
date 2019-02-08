/**
 * @author mdsinalpha
 */

public class Ball {
    private Position position;

    public Ball() {
        position = new Position(0, 0);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
