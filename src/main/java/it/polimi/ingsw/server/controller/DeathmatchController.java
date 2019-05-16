package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.server.controller.turns.FirstTurn;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.*;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.NormalPlayerBoard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controls the flow of the Game. The Game is seen as a sequence of turns. The order of the turns follows the order of connection.
 *
 *  @author Fahed B. Tej
 */
public class DeathmatchController implements SuspensionListener, ScoreListener {

    /**
     * Players in the Game
     */
    private List<Player>  players;

    /**
     * 
     */
    private Player currentPlayer;

    /**
     * 
     */
    private GameBoard board;

    /**
     *
     */
    private List<Room> gameConfiguration;


    /**
     * Constructs a DeathmatchController with the given users
     * @param users     users in the game. The order of the turns is based on the given list.
     */
    public DeathmatchController(List<User> users){
        this.players = users.stream()
                .map(u ->
                        buildPlayer(u,users.indexOf(u)==0, "FigureRes"+users.indexOf(u))) //TODO Make FigureRes parametric
                .collect(Collectors.toList());

        this.gameConfiguration = loadRooms();
        this.board = new GameBoard(new KillshotTrack(8), loadRooms());
    }

    /**
     * Given a user, constructs a player
     * @param user  to be transformed in player
     * @return      player relative to the user
     */
    private Player buildPlayer(User user, boolean isFirst, String figureRes){
        return new Player(user.getName(),isFirst,figureRes,user,new NormalPlayerBoard(),this,this);
    }

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
     * Starts the game.
     */
    public void start() {
        callFirstTurn();

    }

    /**
     * In the first turn, every player follows this steps:
     * (1) Draw 2 powerup cards
     * (2) choose 1 to keep
     * (3) Start the first turn by discarding the other
     * powerup. Its color determines the spawnpoint.
     */
    private void callFirstTurn() {
        for (Player p : players){
            currentPlayer = p;
            new FirstTurn().startTurn(currentPlayer, board);
        }
    }

    /**
     * 
     */
    private void checkFinalFrenzy() {
        // TODO implement here
    }

    /**
     * 
     */
    private void CallNormalTurn() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callScoreKilled() {
        // TODO implement here
    }

    /**
     * 
     */
    private void handleOverAndDoubleKill() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callRespawnForKilled() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callFinalFrenzyTurn() {
        // TODO implement here
    }

    /**
     * 
     */
    public void scoreBoard() {
        // TODO implement here
    }

    /**
     * @param player
     */
    public void playerSuspension(Player player) {
        // TODO implement here
    }

    /**
     * @param player
     */
    public void playerResumption(Player player) {
        // TODO implement here
    }

    /**
     * @param killed
     */
    public void addKilled(Damageable killed) {
        // TODO implement here
    }

    /**
     * 
     */
    public void scoreAllKilled() {
        // TODO implement here
    }



    /**
     * @return
     */

    public List<Damageable> getKilled() {
        // TODO implement here
        return null;
    }


    /**
     * 
     */
    public void emptyKilledList() {
        // TODO implement here
    }

    /**
     * Users that are playing this game
     */
    public void addUsers(Collection<User> users){

    }

}