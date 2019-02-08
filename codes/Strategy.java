/**
 * @author omidekz
 * man faghat in class ro neveshtam.
 */

class Strategy {

    private static int MY_TEAM_SCORE = -1, OPPONENT_SCORE = -1, FIRST_PLAYER_STARTER = -1;

    static Player[] init_players() {
        Player[] players = new Player[5];
        /*
        Here you can set each of your player's name and your team formation.
        In case of setting wrong position, server will set default formation for your team.
         */

        players[0] = new Player("goler", new Position(-6.5, 0));
        players[1] = new Player("rightGoler", new Position(-5.5, 1.2));
        players[2] = new Player("leftGoler", new Position(-5.5, -1.2));
        players[3] = new Player("righ", new Position(-1, 1));
        players[4] = new Player("left", new Position(-2, -1));

        return players;
    }

    static Triple do_turn(Game game) {
        Triple act = new Triple();
        int playerID;
        Position from, to;
        String msg = "";

        distancePlayersFromBall(game);
        boolean ballInAttackPossition = ballInAttackPossition(game);
        boolean hardDef = hardDef(game);
        boolean density = densityOfMyplayers(game),
                warningForBall = game.getBall().getPosition().getX() < -4;
        boolean flag = false;

        for (int i = 0; i < 5; i++) {
            Position player = game.getMyTeam().getPlayer(i).getPosition(),
                    ball = game.getBall().getPosition();
            if (player.getX() < ball.getX()
                    && distance(player, ball) <= 8.5
                    && Line.goodForStraightShot(game, i)) {

                act.setPlayerID(i);
                act.setPower(100);
                act.setAngle(getAngle(player, ball));
                msg = "straight\n";
                System.out.println(msg + String.format("xp:%f, yp:%f - xB:%f, yB:%f - cycle:%d",player.getX()
                        ,player.getY(),ball.getX(),ball.getY(),game.getCycle()));
                return act;
            }
        }

        for (int i = 0; i <5 ; i++) {
            Position player = game.getMyTeam().getPlayer(i).getPosition(),
                    ball = game.getBall().getPosition();
            if(player.getX() < ball.getX()
                    && Line.goodForBallToWall(game,i)){
                act.setPlayerID(i);
                act.setPower(100);
                act.setAngle(getAngle(player, Line.wallToBall(player,ball)));
                msg = "ball to wall\n";
                System.out.println(msg + String.format("xp:%f, yp:%f - xB:%f, yB:%f - cycle:%d",player.getX()
                        ,player.getY(),ball.getX(),ball.getY(),game.getCycle()));
                return act;
            }
        }

        for (int i = 0; i <5 ; i++) {
            Position player = game.getMyTeam().getPlayer(i).getPosition(),
                     ball = game.getBall().getPosition();
            if(player.getX() < ball.getX()
                && Line.goodForWallToBallShot(game,i)){
                act.setPlayerID(i);
                act.setPower(100);
                act.setAngle(getAngle(player, Line.wallToBall(player,ball)));
                msg = "wall to ball\n";
                System.out.println(msg + String.format("xp:%f, yp:%f - xB:%f, yB:%f - cycle:%d",player.getX()
                                ,player.getY(),ball.getX(),ball.getY(),game.getCycle()));
                return act;
            }
        }

        if (MY_TEAM_SCORE != -1 && game.getMyTeam().getScore() + game.getOppTeam().getScore()
                > MY_TEAM_SCORE + OPPONENT_SCORE) {

            if (game.getOppTeam().getScore() > OPPONENT_SCORE) {
                game.getMyTeam().getPlayer(FIRST_PLAYER_STARTER).distancceFromBall *= -1;
                flag = true;
            }

            MY_TEAM_SCORE = game.getMyTeam().getScore();
            OPPONENT_SCORE = game.getOppTeam().getScore();

        }
        if (hardDef) {
            msg = "hardDef\n";
            to = new Position(-6.5, game.getBall().getPosition().getY() > 0 ? .3f : -.3f);
            playerID = nearestToGate(game.getMyTeam(), to);
            from = game.getMyTeam().getPlayer(playerID).getPosition();
            act.setPower(getPower(from, to));
//            Line line = new Line(from,to);
//            if(line.isOnLine(game.getBall().getPosition(),0.75f)){
            //TODO toop beyn bazikon va pos e
//            }
            //            if(!checkPlayerForHardDef(game,game.getMyTeam().getPlayer(playerID))){
//                List<Player> players=new ArrayList<>();
//                for (int i = 0; i <5 ; i++) {
//                    if(game.getMyTeam().getPlayer(i).distancceFromBall<0)
//                        players.add(game.getMyTeam().getPlayer(i));
//                }
//                if(players.size()!=0){
//                    for (Player player : players) {
//                        if (checkPlayerForHardDef(game, player)) {
//                            int pi = findPlayer(game, player);
//                            playerID = pi==-1?0:pi;
//                            if(pi == -1)
//                                to = game.getMyTeam().getPlayer(playerID).getFirstPosition();
//                            break;
//                        }
//                    }
//                }else{
//                    playerID = 0;
//                    to = game.getMyTeam().getPlayer(playerID).getFirstPosition();
//                }
//            }
        } else if (ballInAttackPossition && density) {
            msg = "attack\n";
            playerID = nearestPlayerToBall(game);
            from = game.getMyTeam().getPlayer(playerID).getPosition();
            to = game.getBall().getPosition();
            act.setPower(100);
        } else {
            if (warningForBall) {
                msg = "warrForBall\n";
                playerID = nearestPlayerToBall(game);
                to = game.getBall().getPosition();
            } else {
                msg = "density\n";
                playerID = furthestDistanceFromFirstPos(game.getMyTeam());
                Position playerPos = game.getMyTeam().getPlayer(playerID).getFirstPosition();
                to = new Position(playerPos.getX() - .8f,
                                    playerPos.getY() +
                                    game.getBall().getPosition().getY() > playerPos.getY() ? .2f : -.2f);
            }
            from = game.getMyTeam().getPlayer(playerID).getPosition();
            act.setPower(!to.equals(game.getBall().getPosition()) ? getPower(from, to) : 90);
        }

        act.setPlayerID(playerID);

        int angel = getAngle(from, to);
        if (to.equals(game.getBall().getPosition()) && !checkAngel(angel)) { //TODO from here
            playerID = furthestDistanceFromFirstPos(game.getMyTeam());
            from = game.getMyTeam().getPlayer(playerID).getPosition();
            to = game.getMyTeam().getPlayer(playerID).getFirstPosition();
            act.setPlayerID(playerID);
            act.setPower(getPower(from, to));
            angel = getAngle(from, to);
        } // TODO util here

        act.setAngle(angel);

        System.out.println(msg);

//        System.out.printf("%f\n%s\n--------\n",game.getMyTeam().getPlayer(playerID).distancceFromBall
//                                    ,msg);

        if (MY_TEAM_SCORE == -1) {
            FIRST_PLAYER_STARTER = playerID;
            MY_TEAM_SCORE = game.getMyTeam().getScore();
            OPPONENT_SCORE = game.getOppTeam().getScore();
        }
        if (flag)
            FIRST_PLAYER_STARTER = playerID;
        return act;
    }

    private static int findPlayer(Game game, Player player) {
        for (int j = 0; j < 5; j++) {
            if (game.getMyTeam().getPlayer(j).equals(player))
                return j;
        }
        return -1;
    }

    private static boolean checkPlayerForHardDef(Game game, Player player) {
        Position ballPos = game.getBall().getPosition(),
                playerPos = player.getPosition();
        if (ballPos.getY() > 0
                && playerPos.getY() > ballPos.getY() && !(playerPos.getX() <= ballPos.getX() - .5))
            return false;
        if (-ballPos.getY() > 0
                && -playerPos.getY() > -ballPos.getY() && !(playerPos.getX() <= ballPos.getX() - .5))
            return false;
        return true;
    }

    private static boolean checkAngel(int angel) {
        return angel <= 179 || angel >= 271;
    }

    private static int inForce(Game game) {
        int index = 0;
        float min = game.getMyTeam().getPlayer(0).distancceFromBall;
        for (int i = 1; i < 5; i++)
            if (game.getMyTeam().getPlayer(i).distancceFromBall < min) {
                min = game.getMyTeam().getPlayer(i).distancceFromBall;
                index = i;
            }
        return index;
    }

    private static int getPower(Position from, Position to) {
        float tmpDis = 10;
        float power = (distance(from, to)*100 / tmpDis);
        System.out.print("\npower:" +power+"\n");
        return ((int) power) >=100?100:(int)power;
    }

    private static boolean hardDef(Game game) {
        Position start = new Position(-6.5, -1.5),
                end = new Position(-6, 1.5);
        for (int i = 0; i < 5; i++)
            if (isInThisArea(game.getMyTeam().getPlayer(i).getPosition(), start, end))
                return false;

        return true;
    }

    private static boolean isInThisArea(Position obj, Position startOfArea, Position endOfArea) {
        float er = 0.5f;
        if (obj.getX() <= endOfArea.getX() + er
                && (obj.getX() >= startOfArea.getX() - er)
                && (obj.getY() < endOfArea.getY() + er)
                && (obj.getY() > startOfArea.getY() - er))
            return true;
        return false;
    }

    private static void distancePlayersFromBall(Game game) {
        for (int i = 0; i < 5; i++)
            game.getMyTeam().getPlayer(i).distancceFromBall =
                    distancePlayersFromBall(game.getMyTeam().getPlayer(i).getPosition(),
                            game.getBall().getPosition());
    }

    private static float distancePlayersFromBall(Position from, Position to) {
        float dis = distance(from, to);
        return from.getX() > to.getX() ? -dis : dis;
    }

    private static int nearestPlayerToBall(Game game) {
        float min = 100;
        int index = -1;
        for (int i = 0; i < 5; i++)
            if (game.getMyTeam().getPlayer(i).distancceFromBall > 0) {
                min = game.getMyTeam().getPlayer(i).distancceFromBall;
                index = i;
                break;
            }

        for (int i = index + 1; i < 5; i++)
            if (game.getMyTeam().getPlayer(i).distancceFromBall > 0
             && game.getMyTeam().getPlayer(i).distancceFromBall < min) {
                min = game.getMyTeam().getPlayer(i).distancceFromBall;
                index = i;
            }
        if (index == -1)
            index = 2;
        return index;
    }

    private static int nearestToGate(Team team, Position goal) {
        float min = distance(team.getPlayer(0).getPosition(), goal);
        int index = 0;
        for (int i = 1; i < 5; i++) {
            float tmp = distance(team.getPlayer(i).getPosition(), goal);
            if (tmp < min) {
                min = tmp;
                index = i;
            }
        }
        return index;
    }

    private static int furthestDistanceFromFirstPos(Team team) {
        float max = distance(team.getPlayer(0).getPosition(),
                team.getPlayer(0).getFirstPosition());
        int index = 0;
        for (int i = 1; i < 5; i++) {
            float tmp = distance(team.getPlayer(i).getPosition(), team.getPlayer(i).getFirstPosition());
            index = tmp > max ? i : index;
            max = tmp > max ? tmp : max;
        }
        return index;
    }

    private static int getAngle(Position from, Position to) {
        double x1 = from.getX(), x2 = to.getX(), y1 = from.getY(), y2 = to.getY();
        int angle = Math.abs((int) Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)))); //Calculate the angle from the chosen player to the ball
        if (x2 > x1) {
            if (y2 < y1)
                angle = 360 - angle;
        } else {
            if (y2 < y1)
                angle += 180;
            else
                angle = 180 - angle;
        }
        return angle;
    }

    private static boolean ballInAttackPossition(Game game) {
        float x = (float) game.getBall().getPosition().getX();
        return x > -4 /*&& densityOfMyplayers(game)*/;
    }

    private static boolean densityOfMyplayers(Game game) {
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            if (isInThisArea(game.getMyTeam().getPlayer(i).getPosition(),
                    new Position(-7, -1),
                    new Position(-3, 1))) {
                counter++;
            }
        }
        return counter >= 2;
    }

    private static float distance(Position from, Position to) {
        return (float) Math.sqrt(Math.pow(from.getY() - to.getY(), 2)
                + Math.pow(from.getX() - to.getX(), 2));
    }

    //------------blah blah-------------------------------------
    private static void soutDetails(Game game) {
        showTeam("myTeam", game.getMyTeam());
        showTeam("OppTeam", game.getOppTeam());
        System.out.println("Ball: " + game.getBall().getPosition().toString());

    }
    private static void showTeam(String name, Team team)
    {
        System.out.println(name);
        for (int i = 0; i < 5; i++)
            System.out.print(team.getPlayer(i).getName() + " " + team.getPlayer(i).getPosition().toString() + ", ");
    }

}
