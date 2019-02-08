/**
 * @author mdsinalpha
 */

public class Team {
    private final Player[] players;
    private int score;

    public Team() {
        players=new Player[5];
        for (int i = 0; i <5 ; i++) {
            players[i]=new Player(i);
        }
        score = 0;
    }

    public Player getPlayer(int id) {
        return players[id];
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
