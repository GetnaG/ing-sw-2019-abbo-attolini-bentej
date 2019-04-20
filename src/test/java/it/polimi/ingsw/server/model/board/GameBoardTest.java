package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        firstSquare = new SpawnSquare(Color.BLUE, new WeaponMarket(null));
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
}