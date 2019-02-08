public class Line {
    private float m = Float.MAX_VALUE;
    private float x0,y0; // vase player

    public Line(Position player,Position ball){
        if((float)player.getX() != (float)ball.getX())
            m = (float) ( (ball.getY() - player.getY()) / (ball.getX() - player.getX()));
//        x0 = (float) ball.getX();
//        y0 = (float) ball.getY();
        x0 = (float) player.getX();
        y0 = (float) player.getY();
    }
    public Line(float m,Position pos){
        this.m = m;
        x0 = (float) pos.getX();
        y0 = (float) pos.getY();
    }

    public boolean isOnLine(Position a, Position ball,float minRange,float maxRange){
        if(a.getX() <= x0)
            return false;
        float range = a.getX() < ball.getX()?maxRange:minRange;
        float dis = distanceFromLine(a);
        return dis <= range;
    }
    public boolean isOnLine(Position node,float range){
        float dis = distanceFromLine(node);
        return dis<range-.05f;
    }
    public float distanceFromLine(Position node){
        Line tmpLine = new Line(m!=0.0f?-(1/m):-Float.MAX_VALUE,node);

        float x = ((tmpLine.y0 - y0 + m*x0 - tmpLine.m*tmpLine.x0) / (m - tmpLine.m));
        float y = getY(x);
        return (float) Math.sqrt(Math.pow(y-node.getY(),2) + Math.pow(x-node.getX(),2));
    }
    public boolean isStraightToGate(){
        float y = getY(7.0f);
        return y<=1.39f && y>=-1.39f;
    }

    public static boolean goodForStraightShot(Game game, int playerID){
        Position playerPos = game.getMyTeam().getPlayer(playerID).getPosition(),
                 ballPos = game.getBall().getPosition();
        Line line = new Line(playerPos,ballPos);
        if(!line.isStraightToGate())
            return false;
        for (int i = 0; i <5 ; i++)
            if(i!=playerID && line.isOnLine(game.getMyTeam().getPlayer(i).getPosition(),ballPos,0.75f,1f))
                return false;

        for (int i = 0; i <5 ; i++)
            if(line.isOnLine(game.getOppTeam().getPlayer(i).getPosition(),ballPos,0.75f,1f))
                return false;
        return true;
    }

    public static boolean goodForWallToBallShot(Game game,int playerID){
        Position player = game.getMyTeam().getPlayer(playerID).getPosition();
        Position ball = game.getBall().getPosition();
        if(player.getX() > ball.getX()){
            return false;
        }

        Position wallToBallPos = Line.wallToBall(player,ball);
        if(wallToBallPos.getX() > ball.getX() || wallToBallPos.getX() < player.getX())
            return false;
        System.out.print(String.format("%d - x:%f, y:%f\n",playerID,wallToBallPos.getX(),wallToBallPos.getY()));
        Line playerToWall = new Line(player,wallToBallPos);

        if(     playerToWall.isOnLine(game.getBall().getPosition(),.75f)||
                isPlayerOnLine(game.getMyTeam(),playerToWall,playerID)||
                isPlayerOnLine(game.getMyTeam(),playerToWall,-1))
            return false;

        Line wallToBall = new Line(Line.wallToBall(player,ball),ball);
        if(     !wallToBall.isStraightToGate()||
                isPlayerOnLine(game.getMyTeam(),wallToBall,playerID)||
                isPlayerOnLine(game.getOppTeam(),wallToBall,-1)
        )
            return false;
        return true;
    }

    public static boolean goodForBallToWall(Game game,int playerID){
        Position player = game.getMyTeam().getPlayer(playerID).getPosition(),
                ball = game.getBall().getPosition();
        Line playerToBall = new Line(player,ball);
        Position wall =new Position( playerToBall.getX(ball.getY()>player.getY()?4:-4),ball.getY()>player.getY()?4:-4);
        Line wallToGate = new Line(wall,wallToBall(player,ball));
        if(!wallToGate.isStraightToGate())
            return false;
        if(isPlayerOnLine(game.getMyTeam(),playerToBall,playerID) ||
            isPlayerOnLine(game.getOppTeam(),playerToBall,-1))
            return false;
        if(isPlayerOnLine(game.getMyTeam(),wallToGate,-1)||
            isPlayerOnLine(game.getOppTeam(),wallToGate,-1))
            return false;
        return true;
    }

    private static boolean isPlayerOnLine(Team team, Line playerToWall,int playerID) {
        for (int i = 0; i <5 ; i++)
            if(i != playerID
                    && playerToWall.isOnLine(team.getPlayer(i).getPosition(),.75f))
                return true;
        return false;
    }

    public static Position wallToBall(Position player,Position ball){
        float ballDistanceFromWall =(float) (4 - ball.getY());
        float playerDistanceFromWall;
        float y;
        if(ballDistanceFromWall <= 4){
            y=4;
            playerDistanceFromWall = (float)(4-player.getY());
        }else{
            y=-4;
            ballDistanceFromWall = (float) (4-Math.abs(ball.getY()));
            playerDistanceFromWall = (float)(4-Math.abs(player.getY()));
        }
        float x =(float) (player.getX() +
                (playerDistanceFromWall/(playerDistanceFromWall+ballDistanceFromWall))*(ball.getX() - player.getX()) ) ;
        return new Position(x,y);
    }
    public static Position ballToWall(Position player,Position ball){
        Line line = new Line(player,ball);
        float x;
        if(ball.getY() > player.getY()) {
            x = line.getX(4);
        }else {
            x = line.getX(-4);
        }
        float nX = 2*x - (float) player.getX();
        float nY = (float) player.getY();
        return new Position(nX,nY);
    }
    //----------------------------
    private float getY(float x){
        return (m * (x - x0) + y0);
    }
    private float getX(float y){
        return ((y - y0) + m*x0)/m;
    }

    public float getM(){return m;}
    public float getX0(){return x0;}
    public float getY0(){return y0;}

//    public static void main(String[] args) {
//        Position po = Line.ballToWall(new Position(0,1),new Position(1,2));
//        System.out.println("x:"+po.getX()+", y:"+po.getY());
//    }

}
