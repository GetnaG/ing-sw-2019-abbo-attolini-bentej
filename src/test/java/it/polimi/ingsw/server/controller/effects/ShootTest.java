package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.server.controller.ScoreListener;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.*;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ShootTest {
    private GameBoard board;
    private List<Square> configuration;
    private List<Player> players;


    @BeforeEach
    void setUp(){
        board = getDumbGameBoard();
        ToClientInterface client = getDumbToClientInterface();
        ScoreListener scoreListener = getDumbScoreListener();

        players = new ArrayList<>();
        players.add(new Player("A",true,"A",client, scoreListener));

        List<AmmoCube> redCubes = new ArrayList<>();
        redCubes.add(AmmoCube.RED);
        redCubes.add(AmmoCube.RED);
        redCubes.add(AmmoCube.RED);
        List<AmmoCube> blueCubes = new ArrayList<>();
        blueCubes.add(AmmoCube.BLUE);
        blueCubes.add(AmmoCube.BLUE);
        blueCubes.add(AmmoCube.BLUE);
        List<AmmoCube>  yellowCubes= new ArrayList<>();
        yellowCubes.add(AmmoCube.YELLOW);
        yellowCubes.add(AmmoCube.YELLOW);
        yellowCubes.add(AmmoCube.YELLOW);


        players.get(0).addAmmo(redCubes);
        players.get(0).addAmmo(blueCubes);
        players.get(0).addAmmo(yellowCubes);

        WeaponCard card;
        String id;
        List<AmmoCube> cubes;
        String[][] effects;

        id = "Weapon id";
        cubes = Arrays.asList(AmmoCube.BLUE, AmmoCube.RED, AmmoCube.RED);
        effects = new String[][]{{"00", "01"}, {"10", "11", "12"}, {"20"}};

        card = new WeaponCard(id, cubes, effects, true);

        players.get(0).buy(card, new ArrayList<>());

    }
    @Test
    @Disabled // To complete once effects actually run.
    void runEffect() {
        Shoot shoot = new Shoot();

        shoot.runEffect(players.get(0), null, board, new ArrayList<>(), new ArrayList<>());


    }

    private GameBoard getDumbGameBoard(){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();
        List<Room> conf = new ArrayList<>();

        squares.add(new SpawnSquare(AmmoCube.BLUE, new WeaponMarket(new ArrayList<>())));
        squares.add(new Square(Color.BLUE));
        squares.add(new Square(Color.GREEN));
        squares.add(new Square(Color.BLUE));
        // Adding rooms and doors.
        SpawnSquare square1 = (SpawnSquare) squares.get(0);
        Square square2 = squares.get(1);
        Square square3 = squares.get(2);
        Square square4 = squares.get(3);

        // By default they are all isolated
        squares.forEach(x -> x.setNorthBorder(Border.WALL));
        squares.forEach(x -> x.setEastBorder(Border.WALL));
        squares.forEach(x -> x.setSouthBorder(Border.WALL));
        squares.forEach(x -> x.setWestBorder(Border.WALL));

        // Now we define the connections
        square1.setSouth(square3);
        square1.setSouthBorder(Border.CORRIDOR);
        square1.setEast(square2);
        square1.setEastBorder(Border.DOOR);

        square2.setWest(square1);
        square2.setWestBorder(Border.DOOR);
        square2.setSouthBorder(Border.CORRIDOR);
        square2.setSouth(square4);

        square3.setNorth(square1);
        square3.setNorthBorder(Border.CORRIDOR);
        square3.setEast(square4);
        square3.setEastBorder(Border.WALL);

        square4.setNorth(square2);
        square4.setWestBorder(Border.CORRIDOR);
        square4.setWest(square3);
        square4.setWestBorder(Border.WALL);

        // Once created squares, we need to create rooms.
        List<Square> squaresInRoom = new ArrayList<>();

        squaresInRoom.add(square1);
        squaresInRoom.add(square3);
        Room firstRoom = new Room(squaresInRoom, (SpawnSquare)square1 );

        squaresInRoom = new ArrayList<>();
        squaresInRoom.add(square2);
        squaresInRoom.add(square4);
        Room secondRoom = new Room(squaresInRoom);

        square1.setRoom(firstRoom);
        square3.setRoom(firstRoom);
        square2.setRoom(secondRoom);
        square4.setRoom(secondRoom);

        conf = new ArrayList<>();
        conf.add(firstRoom);
        conf.add(secondRoom);
        configuration = squares;
        return new GameBoard(new KillshotTrack(), conf);

    }

    private ToClientInterface getDumbToClientInterface(){
        return new ToClientInterface() {
            @Override
            public EffectInterface chooseEffectsSequence(List<EffectInterface> options) {
                return options.get(0);
            }

            @Override
            public PowerupCard chooseSpawn(List<PowerupCard> option) {
                return option.get(0);
            }

            @Override
            public PowerupCard choosePowerup(List<PowerupCard> options) {
                return options.get(0);
            }

            @Override
            public Square chooseDestination(List<Square> options) {
                return options.get(0);
            }

            @Override
            public WeaponCard chooseWeaponCard(List<WeaponCard> options) {
                return options.get(0);
            }

            @Override
            public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) {
                return options.get(0);
            }

            @Override
            public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) {
                return options.get(0);
            }

            @Override
            public WeaponCard chooseWeaponToReload(List<WeaponCard> options) {
                return options.get(0);
            }

            @Override
            public Action chooseAction(List<Action> options) {
                return options.get(0);
            }

            @Override
            public PowerupCard choosePowerupForPaying(List<PowerupCard> options) {
                return options.get(0);
            }

            @Override
            public PowerupCard askUseTagback(List<PowerupCard> options) {
                return options.get(0);
            }

            @Override
            public List<Damageable> chooseTarget(List<List<Damageable>> options) {
                return null;
            }

            @Override
            public String chooseUserName() {
                return null;
            }

        };

    }

    private ScoreListener getDumbScoreListener() {
        return new ScoreListener() {
            @Override
            public void addKilled(Damageable killed) {

            }

            @Override
            public void scoreAllKilled() {

            }

            @Override
            public void scoreBoard() {

            }

            @Override
            public List<Damageable> getKilled() {
                return null;
            }

            @Override
            public void emptyKilledList() {

            }
        };
    }


}