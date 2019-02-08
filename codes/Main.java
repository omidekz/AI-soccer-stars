/**
 *
 * @author mdsinalpha
 */
public class Main 
    private static final String TEAM_NAME = "ABSANT"; //Write your team name here
{
    public static void main(String[] args) {
        Game game = new Game("192.168.62.82",9595);
        if(game.connect_to_server())
            game.start(TEAM_NAME); 
    }
}
