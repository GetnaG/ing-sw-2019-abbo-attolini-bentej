package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.server.controller.turns.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.*;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.NormalPlayerBoard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controls the flow of the Game.
 * <p>
 * The Game is seen as a sequence of turns. The order of the turns follows the order of connection.
 *
 *  @author Fahed B. Tej
 */
public class DeathmatchController implements SuspensionListener, ScoreListener {

    /**
     * Players in the Game
     */
    private List<Player>  players;

    /**
     * Current Player
     */
    private Player currentPlayer;

    /**
     * Game Board used in the game
     */
    private GameBoard board;

    /**
     *  Configuration of the map
     */
    private List<Room> gameConfiguration;

    /**
     * Suspended Players
     */
    private List<Player> suspendedPlayers;

    /**
     * Damageable objects killed during turn.
     */
    private List<Damageable> killedInTurn;

    /**
     * Constructs a DeathmatchController with the given users
     * @param users     users in the game. The order of the turns is based on the given list.
     */
    public DeathmatchController(List<User> users, int skullsLeft){
        this.players = users.stream()
                .map(u ->
                        buildPlayer(u,users.indexOf(u)==0, "FigureRes"+users.indexOf(u))) //TODO Make FigureRes parametric
                .collect(Collectors.toList());

        this.board = new GameBoard(new KillshotTrack(skullsLeft), null);
        this.gameConfiguration = loadRooms();
        this.board.setConfiguration(this.gameConfiguration);
        this.suspendedPlayers = new ArrayList<>();
        this.killedInTurn = new ArrayList<>();
    }

    /**
     * Given a user, constructs a player
     * @param user  to be transformed in player
     * @return      player relative to the user
     */
    private Player buildPlayer(User user, boolean isFirst, String figureRes){
        return new Player(user.getName(),isFirst,figureRes,user,new NormalPlayerBoard(),this,this);
    }

    /**
     * Returns a configuration of the board (i.e. a list of rooms)
     *
     * @return a configuration of the board
     */
    //TODO There are 4 types of Configurations in the game. This method returns always the same type. However, it has to return the type choosen by the first player.
    private List<Room> loadRooms(){

        //Creating the squares with an ammo card
        Square sq1 = new Square(SquareColor.GREEN, board.getAmmoCard());
        Square sq2 = new Square(SquareColor.GREEN, board.getAmmoCard());
        Square sq3 = new Square(SquareColor.GREEN, board.getAmmoCard());
        SpawnSquare sq4 = new SpawnSquare(AmmoCube.YELLOW,getWeaponMarketWithCards());
        SpawnSquare sq5 = new SpawnSquare(AmmoCube.RED,getWeaponMarketWithCards());
        Square sq6 = new Square(SquareColor.PURPLE, board.getAmmoCard());
        Square sq7 = new Square(SquareColor.PURPLE, board.getAmmoCard());
        Square sq8 = new Square(SquareColor.YELLOW, board.getAmmoCard());
        Square sq9 = new Square(SquareColor.RED, board.getAmmoCard());
        Square sq10 = new Square(SquareColor.BLUE, board.getAmmoCard());
        SpawnSquare sq11 = new SpawnSquare(AmmoCube.BLUE, getWeaponMarketWithCards());

        /*Setting connections and doors
            9    <>   10   :   11
            :         <>       <>
            5     |   6    :   7    <>   8
            <>        <>       |         :
            1     :   2     :  3    <>  4
         */

        List<Square> neighbours = new ArrayList<>();
        List<Border> borderNeighbours = new ArrayList<>();
        neighbours.add(sq5);
        neighbours.add(sq2);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.CORRIDOR);
        setUpSquare(sq1, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq6);
        neighbours.add(sq3);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.CORRIDOR);
        setUpSquare(sq2, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq7);
        neighbours.add(sq4);
        borderNeighbours.add(Border.WALL);
        borderNeighbours.add(Border.DOOR);
        setUpSquare(sq3, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq8);
        borderNeighbours.add(Border.CORRIDOR);
        setUpSquare(sq4, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq9);
        neighbours.add(sq6);
        borderNeighbours.add(Border.CORRIDOR);
        borderNeighbours.add(Border.WALL);
        setUpSquare(sq5, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq10);
        neighbours.add(sq7);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.CORRIDOR);
        setUpSquare(sq6, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq11);
        neighbours.add(sq8);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.DOOR);
        setUpSquare(sq7, neighbours, borderNeighbours);

        setNeighboor(sq9,sq10, Border.DOOR,2);
        setNeighboor(sq10,sq11, Border.DOOR,2);

        // Creating rooms

        List<Square> roomSquares = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();

        // Green room
        roomSquares.add(sq1);
        roomSquares.add(sq2);
        roomSquares.add(sq3);
        rooms.add(new Room(roomSquares));
        // Yellow Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq4);
        roomSquares.add(sq8);
        rooms.add(new Room(roomSquares, sq4));
        // Purple Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq6);
        roomSquares.add(sq7);
        rooms.add(new Room(roomSquares));
        //Red Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq5);
        roomSquares.add(sq9);
        rooms.add(new Room(roomSquares, sq5));
        // Blue Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq10);
        roomSquares.add(sq11);
        rooms.add(new Room(roomSquares, sq11));

        return rooms;
    }

    /**
     * Constructs a Weapon Market
     *
     * @return a Weapon Market
     */
    private WeaponMarket getWeaponMarketWithCards(){
        List<WeaponCard> cards  = new ArrayList<>();
        try {
            cards.add(board.getWeaponCard());
            cards.add(board.getWeaponCard());
            cards.add(board.getWeaponCard());
        }catch (AgainstRulesException e) {
            // TODO Handle exception
        }

        return new WeaponMarket(cards);
    }

    /**
     * Sets the neighbours and borders to the given square.
     *
     * @param square           the square
     * @param neighbours       the neighbours of the square. Represented via a list in wich the index gives information about the orientation of the square.
     *                         1 means north, 2 means east, 3 means south, 4 means west.
     * @param borderNeighbours the type of border between the square and the neighbour
     */
    private void setUpSquare(Square square, List<Square> neighbours, List<Border> borderNeighbours){
        for (Square n : neighbours){
            int orientation = neighbours.indexOf(n);
            setNeighboor(square, n, borderNeighbours.get(orientation),orientation);
        }
    }

    /**
     * Given two squares, sets the relationship between them according to the given parameters
     * @param s1            first square
     * @param s2            second square
     * @param borderType    the type of relation between them
     * @param orientation   the orientation of the second square according to the first. 1 means north, 2 means east, 3 means south, 4 means west
     */
    private void setNeighboor(Square s1, Square s2, Border borderType, int orientation){
        if (s1 == null || s2 == null)
            return;

        switch (orientation){
            case 1:
                // North
                s1.setNorthBorder(borderType);
                s2.setSouthBorder(borderType);
                s1.setNorth(s2);
                s2.setSouth(s1);
            case 2:
                // East
                s1.setEastBorder(borderType);
                s2.setWestBorder(borderType);
                s1.setEast(s2);
                s2.setWest(s1);
            case 3:
                // South
                s1.setSouthBorder(borderType);
                s2.setNorthBorder(borderType);
                s1.setSouth(s2);
                s2.setNorth(s1);
            case 4:
                s1.setWestBorder(borderType);
                s2.setEastBorder(borderType);
                s1.setWest(s2);
                s2.setEast(s1);

        }
    }

    /**
     * Adds the users to the game.
     *
     * @param connectedUsers    user in the game
     */
    public void addUsers(List<User> connectedUsers){
        for (User user: connectedUsers){
            players.add(
                    buildPlayer(
                            user,
                            connectedUsers.indexOf(user) + players.size() > 0,
                            "FigureRes"+ connectedUsers.indexOf(user)));
        }
    }

    /**
     * Starts the game. The game is divided in the following four phases:
     * (1) Initial Turn
     * (2) NormalTurns and RespawnTurns
     * (3) FinalFrenzy
     * (4) Scoring
     */
    public void start() {
        // First turn
        firstTurn();
        // A series of normal turns interrupted by Final Frenzy
        while(!checkFinalFrenzy()){
            currentPlayer = nextPlayer(currentPlayer);
            normalTurn(currentPlayer);
            scoreAllKilled();
        }
        // Final frenzy
        finalFrenzyTurn();
        // Scoring the game
        score();
    }

    /**
     * In the first turn, every player follows this steps:
     * (1) Draw 2 powerup cards
     * (2) choose 1 to keep
     * (3) Start the first turn by discarding the other
     * powerup. Its color determines the spawnpoint.
     */
    private void firstTurn() {
        for (Player p : players){
            currentPlayer = p;
            new FirstTurn().startTurn(currentPlayer, board);
        }
    }

    /**
     * Checks if Final Frenzy has been triggered.
     *
     * @return true if Final Frenzy has been triggered, false otherwise.
     */
    private boolean checkFinalFrenzy() {
        return  board.checkFinalFrenzy();
    }
    /*
        1. Final frenzy is triggered when the last skull is taken from the killshot track.
        2. All players with no damage flip over their boards. They will be worth only a minimal amount.
        3. Each player, including the one who triggered final frenzy, gets one more turn. They flip their action tiles to the final frenzy side.
        a. Those who play before the starting player
        choose twice from this set of actions:
        I. Move up to 4 squares.
                II. Move up to 2 squares and grab something.
        III. Move up to 1 square, reload, and shoot.
                b. The starting player and all those who play after choose 1 of these actions:
        I. Either move up to 3 squares and grab
        something.
                II. Or move up to 2 squares, reload, and shoot.
        4. Boards that receive killshots in final frenzy are flipped to the 2-1-1-1 side after they are scored.
     */
    private void finalFrenzyTurn() {
        // players.stream().filter(p -> p.getPlayerBoard().getDamage().size()==0).forEach(p -> p.getPlayerBoard().flip());
        Player playerTriggeredFinalFrenzy = currentPlayer;
        currentPlayer = nextPlayer(currentPlayer);

        while (nextPlayer(currentPlayer) == nextPlayer(playerTriggeredFinalFrenzy)) {
            // FrenzyTurn differs from those who come before the first player and those who come after.
            if (players.indexOf(currentPlayer) > players.indexOf(playerTriggeredFinalFrenzy)) {
                // For those who come before the first player
                new FrenzyTurnBefore(currentPlayer, board).startTurn(currentPlayer, board);
            } else {
                // for those who come after the first player
                new FrenzyTurnAfter(currentPlayer, board).startTurn(currentPlayer, board);
            }
            currentPlayer = nextPlayer(currentPlayer);
        }
    }

    /**
     * Deals with the normal turn of the {@code currentPlayer}.
     */
    private void normalTurn(Player currentPlayer) {
        NormalTurn turn = new NormalTurn(currentPlayer, board);
        int exitValue = turn.startTurn(currentPlayer, board);
        respawnDeadPlayers();
    }


    /**
     * Returns the next non-suspended player.
     *
     * @param p current player
     * @return  next non-suspended player
     */
    private Player nextPlayer(Player p) {
        Player next;
        if (players.indexOf(p) + 1 == players.size())
            next = players.get(0);
        else
            next = players.get(players.indexOf(currentPlayer)+1);
        if (suspendedPlayers.contains(next))
            return nextPlayer(next);
        return next;
    }

    /**
     * Respawns all dead players. Each player gets to play a Respawn Turn.
     */
    private void respawnDeadPlayers(){
        List<Player> toBeRespawned = players.stream().filter(p -> p.getPlayerBoard().isDead()).collect(Collectors.toList());
        for (Player p : toBeRespawned){
            new RespawnTurn().startTurn(p, board);
        }
    }

    /**
     * Scores all the game. This means that each {@linkplain Player} will have his definitive score.
     */
    private void score() {
        board.scoreBoard();
    }

    /**
     * Changes the given player status to suspended.
     *
     * @param player    player to be suspended
     * @param marchSuspensionListener
     */
    public void playerSuspension(String player, SuspensionListener marchSuspensionListener) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.add(p);
                return;
            }
    }

    /**
     * Changes the given player to resumed.
     *
     * @param player    player resumed
     */
    public void playerResumption(String player) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.remove(p);
                return;
            }
    }

    public boolean playerUpdate(String player, ToClientInterface newConnection) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.remove(p);
                p.setToClient(newConnection);
                return true;
            }
        return false;
    }


    /**
     * This calls {@code scoreBoard} on the {@linkplain GameBoard}. It is used
     * at the end of the game.
     */
    public void scoreBoard() {
        board.scoreBoard();
    }

    /**
     * Adds a {@linkplain Damageable} object to the objects that will be
     * scored.
     *
     * @param killed the {@code Damageable} to be scored
     * @throws NullPointerException if {@code killed} is null
     */
    public void addKilled(Damageable killed) {
        killedInTurn.add(killed);
    }

    /**
     * Scores all the {@linkplain Damageable} objects added. This method must
     * also take care of adding the <i>kill shot</i> and
     * <i>overkill</i> tokens (if applicable) to the {@code GameBoard}.
     * This affects all the objects added since the last call to {@code
     * emptyKilledList}.
     */
    public void scoreAllKilled() {
        killedInTurn.stream().forEach(d -> d.scoreAndResetDamage());
    }



    /**
     * Returns a {@linkplain List} of the objects that will be scored. These are
     * the {@linkplain Damageable} added since the last call to {@code
     * emptyKilledList}.
     *
     * @return a list of the objects that will be scored
     */
    public List<Damageable> getKilled() {
        return killedInTurn;
    }


    /**
     * Clears the list of objects on which scoring will be performed. Invoking
     * {@code scoreAllKilled} will effect only the objects added after the last
     * call to this method. A call to {@code scoreAllKilled} immediately after
     * this method will produce no effect.
     */
    public void emptyKilledList() {
        killedInTurn.removeAll(killedInTurn);
    }
}