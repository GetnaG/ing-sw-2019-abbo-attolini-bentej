package it.polimi.ingsw.server.model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    static private GameBoard gm;
    static private List<Room> configuration;
    static private SpawnSquare firstSquare;
    static private Square secondSquare;
    static private Square thirdSquare;
    static private Square fourthSquare;


    @BeforeAll
    static void setUp(){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();

        // Adding rooms and doors.
        firstSquare = new SpawnSquare(Color.GREEN,new WeaponMarket(new ArrayList<>()));
        secondSquare = new Square(Color.BLUE);
        thirdSquare = new Square(Color.GREEN);
        fourthSquare = new Square(Color.BLUE);

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
        Room firstRoom = new Room(squaresInRoom, (SpawnSquare)firstSquare );

        squaresInRoom = new ArrayList<>();
        squaresInRoom.add(secondSquare);
        squaresInRoom.add(fourthSquare);
        Room secondRoom = new Room(squaresInRoom);

        firstSquare.setRoom(firstRoom);
        thirdSquare.setRoom(firstRoom);
        secondSquare.setRoom(secondRoom);
        fourthSquare.setRoom(secondRoom);

        configuration = new ArrayList<>();
        configuration.add(firstRoom);
        configuration.add(secondRoom);
        gm = new GameBoard(new KillshotTrack(), configuration);



    }
    @Test
    void findSpawn() {
        SpawnSquare spawn = configuration.get(0).getSpawnSquare();
        assertEquals(spawn, gm.findSpawn(spawn.getSpawnColor()));

    }

    @Test
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
    void replaceAll() {
    }

    // used by other test classes

    public GameBoard setUpGameBoard(GameBoard board){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();
        List<Room> conf = new ArrayList<>();

        squares.add(new SpawnSquare(Color.GREEN, new WeaponMarket(new ArrayList<>())));
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
        return new GameBoard(new KillshotTrack(), conf);

    }
}