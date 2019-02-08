/**
 *
 * @author mdsinalpha
 */

public class Triple 
{
    private int player_id ;
    private int angle ;
    private int power ;
    
    public Triple(int player_id,int angle,int power)
    {
        this.player_id = player_id;
        this.angle = angle;
        this.power = power;
    }
    
    public Triple()
    {
        this(0,0,0);
    }
    
    public void setPlayerID(int player_id)
    {
        this.player_id = player_id;
    }
    
    public void setAngle(int angle)
    {
        this.angle = angle;
    }
    
    public void setPower(int power) 
    {
        this.power = power;
    }
    
    @Override
    public String toString()
    {
        return player_id + "," + angle + ","
                + power + "\n";
    }
}
