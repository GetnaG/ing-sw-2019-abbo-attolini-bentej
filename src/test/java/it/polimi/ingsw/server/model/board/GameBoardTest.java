package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    static private GameBoard gm;
    static private GameBoard gm2;
    static private List<Room> configuration;
    static private List<Room> configuration2;
    static private SpawnSquare firstSquare;
    static private Square secondSquare;
    static private Square thirdSquare;
    static private Square fourthSquare;
    static private Square fifthSquare;
    static private Square sixthSquare;
    static private Square seventhSquare;


    @BeforeAll
    static void setUp(){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();
        List<Square> squares2 = new ArrayList<>();

        // Adding rooms and doors.
        firstSquare = new SpawnSquare(AmmoCube.BLUE,new WeaponMarket(new ArrayList<>()));
        secondSquare = new Square(SquareColor.BLUE);
        thirdSquare = new Square(SquareColor.BLUE);
        fourthSquare = new Square(SquareColor.BLUE);

        squares.add(firstSquare);
        squares.add(secondSquare);
        squares.add(thirdSquare);
        squares.add(fourthSquare);

        // By default they are all isolated
        squares.forEach(x -> x.setNorthBorder(Border.WALL));
        squares.forEach(x -> x.setEastBorder(Border.WALL));
        squares.forEach(x -> x.setSouthBorder(Border.WALL));
        squares.forEach(x -> x.setWestBorder(Border.WALL));

        // Now we define the connections
        firstSquare.setSouth(thirdSquare);
        firstSquare.setSouthBorder(Border.CORRIDOR);
        firstSquare.setEast(secondSquare);
        firstSquare.setEastBorder(Border.DOOR);

        secondSquare.setWest(firstSquare);
        secondSquare.setWestBorder(Border.DOOR);
        secondSquare.setSouthBorder(Border.CORRIDOR);
        secondSquare.setSouth(fourthSquare);

        thirdSquare.setNorth(firstSquare);
        thirdSquare.setNorthBorder(Border.CORRIDOR);
        thirdSquare.setEast(fourthSquare);
        thirdSquare.setEastBorder(Border.WALL);

        fourthSquare.setNorth(secondSquare);
        fourthSquare.setWestBorder(Border.CORRIDOR);
        fourthSquare.setWest(thirdSquare);
        fourthSquare.setWestBorder(Border.WALL);

        // Once created squares, we need to create rooms.
        List<Square> squaresInRoom = new ArrayList<>();

        squaresInRoom.add(firstSquare);
        squaresInRoom.add(thirdSquare);
        Room firstRoom = new Room(squaresInRoom, firstSquare );

        squaresInRoom = new ArrayList<>();
        squaresInRoom.add(secondSquare);
        squaresInRoom.add(fourthSquare);
        Room secondRoom = new Room(squaresInRoom);

        //firstSquare.setRoom(firstRoom);
        //thirdSquare.setRoom(firstRoom);
        //secondSquare.setRoom(secondRoom);
        //fourthSquare.setRoom(secondRoom);

        configuration = new ArrayList<>();
        configuration.add(firstRoom);
        configuration.add(secondRoom);
        gm = new GameBoard(new KillshotTrack(), configuration);

        // Second configuration
        fifthSquare = new Square(SquareColor.BLUE);
        sixthSquare = new Square(SquareColor.GREEN);
        seventhSquare = new Square(SquareColor.BLUE);

        squares2.addAll(squares);
        squares2.add(fifthSquare);
        squares2.add(sixthSquare);
        squares2.add(seventhSquare);

        // By default they are all isolated
        squares2.forEach(x -> x.setNorthBorder(Border.WALL));
        squares2.forEach(x -> x.setEastBorder(Border.WALL));
        squares2.forEach(x -> x.setSouthBorder(Border.WALL));
        squares2.forEach(x -> x.setWestBorder(Border.WALL));
        // Set connections with other squares

        firstSquare.setSouth(thirdSquare);
        firstSquare.setSouthBorder(Border.CORRIDOR);
        firstSquare.setEast(secondSquare);
        firstSquare.setEastBorder(Border.DOOR);

        secondSquare.setWest(firstSquare);
        secondSquare.setWestBorder(Border.DOOR);
        secondSquare.setSouthBorder(Border.CORRIDOR);
        secondSquare.setSouth(fourthSquare);

        thirdSquare.setNorth(firstSquare);
        thirdSquare.setNorthBorder(Border.CORRIDOR);
        thirdSquare.setEast(fourthSquare);
        thirdSquare.setEastBorder(Border.WALL);

        fourthSquare.setNorth(secondSquare);
        fourthSquare.setNorthBorder(Border.CORRIDOR);
        fourthSquare.setWest(thirdSquare);
        fourthSquare.setWestBorder(Border.WALL);

        fifthSquare.setSouth(sixthSquare);
        fifthSquare.setSouthBorder(Border.CORRIDOR);
        fifthSquare.setWest(fourthSquare);
        fifthSquare.setWestBorder(Border.DOOR);

        sixthSquare.setNorth(fifthSquare);
        sixthSquare.setNorthBorder(Border.CORRIDOR);
        sixthSquare.setEast(seventhSquare);
        sixthSquare.setEastBorder(Border.DOOR);

        seventhSquare.setWest(sixthSquare);
        seventhSquare.setWestBorder(Border.CORRIDOR);

        List<Square> room3 = new ArrayList<>();
        room3.add(fifthSquare);
        room3.add(sixthSquare);
        room3.add(seventhSquare);
        Room thirdRoom = new Room(room3);

        squaresInRoom = new ArrayList<>();
        squaresInRoom.add(secondSquare);
        squaresInRoom.add(fourthSquare);
        Room secondRoom2 = new Room(squaresInRoom);

        //fifthSquare.setRoom(thirdRoom);
        //sixthSquare.setRoom(thirdRoom);
        //seventhSquare.setRoom(thirdRoom);

        configuration2 = new ArrayList<>();
        configuration2.add(firstRoom);
        configuration2.add(secondRoom);
        configuration2.add(thirdRoom);
        gm2 = new GameBoard(new KillshotTrack(), configuration2);

        /*
        First configuration looks like :
                                            1   <>   2
                                            3   |    4
        First configuration looks like :
                                            1   <>   2
                                            3    |   4    5
                                                          6   7
         */

    }
    @Test
    void findSpawn() {
        SpawnSquare spawn = configuration.get(0).getSpawnSquare();
        assertEquals(spawn, gm.findSpawn(spawn.getSpawnColor()));

    }

    @Test
    @Disabled
    void getValidDestinations() {

        int maxDistance = 0;
        assertTrue(gm.getValidDestinations(firstSquare,maxDistance,true).isEmpty());
        assertTrue(gm.getValidDestinations(firstSquare,maxDistance,false).isEmpty());

        maxDistance = 1;
        List<Square> expected = new ArrayList<>();
        expected.add(secondSquare);
        expected.add(thirdSquare);
        assertTrue(expected.containsAll( gm.getValidDestinations(firstSquare,maxDistance,true)));

        maxDistance = 3;
        expected = new ArrayList<>();
        expected.add(secondSquare);
        expected.add(thirdSquare);
        assertTrue(expected.containsAll(gm.getValidDestinations(firstSquare,maxDistance,true)));

        expected = new ArrayList<>();
        maxDistance = 8;
        expected.add(secondSquare);
        expected.add(thirdSquare);
        expected.add(fourthSquare);
        assertTrue(expected.containsAll(gm.getValidDestinations(firstSquare,maxDistance,false)));

    }

    @Test
    @Disabled
    void minimumDistance() {
        fourthSquare.setEastBorder(Border.DOOR);
        fourthSquare.setEast(fifthSquare);
        assertEquals(0, gm2.minimumDistance(firstSquare, firstSquare, true));
        assertEquals(0, gm2.minimumDistance(firstSquare, firstSquare, false));

        assertEquals(2, gm2.minimumDistance(firstSquare, fourthSquare, true));
        assertEquals(4, gm2.minimumDistance(secondSquare, seventhSquare , true));
    }

    @Test
    void replaceAll() {
    }

    // used by other test classes

    public GameBoard setUpGameBoard(GameBoard board){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();
        List<Room> conf = new ArrayList<>();

        squares.add(new SpawnSquare(AmmoCube.BLUE, new WeaponMarket(new ArrayList<>())));
        squares.add(new Square(SquareColor.BLUE));
        squares.add(new Square(SquareColor.GREEN));
        squares.add(new Square(SquareColor.BLUE));
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

        //square1.setRoom(firstRoom);
        //square3.setRoom(firstRoom);
        //square2.setRoom(secondRoom);
        //square4.setRoom(secondRoom);

        conf = new ArrayList<>();
        conf.add(firstRoom);
        conf.add(secondRoom);
        return new GameBoard(new KillshotTrack(), conf);

    }

    @Test
    @Disabled
    void getPlayerInSquare() {
        Player p1 = new Player("A");
        p1.setPosition(firstSquare);
        Player p2 = new Player("B");
        p2.setPosition(firstSquare);
        Player p3 = new Player("C");
        p3.setPosition(firstSquare);
        Player p4 = new Player("D");
        p4.setPosition(secondSquare);
        List<Player> players = new ArrayList<>();
        List<Player> allPlayers = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        allPlayers.addAll(players);
        allPlayers.add(p4);

        assertTrue(players.containsAll(gm.getPlayerInSquare(firstSquare, allPlayers)));
        assertTrue(!players.contains(gm.getPlayerInSquare(thirdSquare, allPlayers)));


    }
}