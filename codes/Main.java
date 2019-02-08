/**
 *
 * @author mdsinalpha
 */
public class Main 
{
    public static void main(String[] args) {
        Game game = new Game("192.168.62.82",9595);
        if(game.connect_to_server())
            game.start("abSANT"); //Write your team name here
    }
}
