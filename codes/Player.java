
public class Player {
    private final int id;
    private final String name;
    private Position position;
    public float distancceFromBall;
    public int index = -1;

    public Player(int id) {
        this.id = id;
        name = null;
        position = null;
    }

    public Player(String name, Position position) {
        id = -1;
        this.name = name;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }
    public String getDistance(){
        return distancceFromBall+"";
    }

    public Position getFirstPosition() {
        return Strategy.init_players()[id].getPosition();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\nNAME: %s\n%s\nfromBall: %s\n",
                            getId(),getName(),getPosition().toString(),getDistance());
    }

    @Override
    public boolean equals(Object obj) {
        Player other = (Player) obj;
        return getFirstPosition().equals(other.getFirstPosition());
    }
}
