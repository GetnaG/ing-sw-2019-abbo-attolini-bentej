package it.polimi.ingsw.server.model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private GameBoard gm;
    private List<Room> configuration;

    @BeforeAll
    void setUp(){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();
        squares.add(new Square(Color.BLUE));
        squares.add(new Square(Color.BLUE));
        squares.add(new Square(Color.GREEN));
        squares.add(new SpawnSquare( new WeaponMarket()));

        // By default they are all isolated
        squares.forEach(x -> x.setNorthBorder(Border.WALL));
        squares.forEach(x -> x.setEastBorder(Border.WALL));
        squares.forEach(x -> x.setSouthBorder(Border.WALL));
        squares.forEach(x -> x.setWestBorder(Border.WALL));

        // Adding rooms and doors.
        // Room: (1,3)
        squares.get(0).setSouth(squares.get(2));
        squares.get(0).setSouthBorder(Border.CORRIDOR);
        squares.get(1).setNorth(squares.get(0));
        squares.get(1).setNorthBorder(Border.CORRIDOR);
        List<Square> squareList = new ArrayList<>();
        squareList.add(squares.get(0));
        squareList.add(squares.get(2));
        //Spawn in first room
        Room firstRoom = new Room(squareList, (SpawnSquare)squares.get(3) );
        squareList = new ArrayList<>();
        // Room: (2,4)
        squares.get(1).setSouth(squares.get(4));
        squares.get(1).setSouthBorder(Border.CORRIDOR);
        squares.get(4).setNorth(squareList.get(1));
        squares.get(4).setNorthBorder(Border.CORRIDOR);
        squareList.add(squares.get(1));
        squareList.add(squares.get(4));
        Room secondRoom = new Room(squareList);

        squareList.get(0).setRoom(firstRoom);
        squareList.get(2).setRoom(firstRoom);
        squareList.get(1).setRoom(secondRoom);
        squareList.get(3).setRoom(secondRoom);

        configuration.add(firstRoom);
        configuration.add(secondRoom);
        gm = new GameBoard(new KillshotTrack(), configuration );



    }
    @Test
    void findSpawn() {
        SpawnSquare spawn = configuration.get(0).getSpawnSquare();
        assertEquals(spawn, gm.findSpawn(spawn.getColor()));

    }

    @Test
    void getValidDestinations() {
        Square firstSquare = configuration.get(0).getSquares().get(0);
        Square secondSquare = configuration.get(0).getSquares().get(1);
        Square thirdSquare = configuration.get(1).getSquares().get(0);
        Square fourthSquare = configuration.get(1).getSquares().get(1);

        int maxDistance = 0;
        assertTrue(gm.getValidDestinations(firstSquare,maxDistance,true).isEmpty());

        maxDistance = 1;
        List<Square> expected = new ArrayList<>();
        expected.add(secondSquare);
        expected.add(thirdSquare);
        assertEquals(expected, gm.getValidDestinations(firstSquare,maxDistance,true));

        maxDistance = 3;
        expected = new ArrayList<>();
        expected.add(secondSquare);
        expected.add(thirdSquare);
        assertEquals(expected, gm.getValidDestinations(firstSquare,maxDistance,true));

        expected = new ArrayList<>();
        maxDistance = 8;
        expected.add(secondSquare);
        expected.add(thirdSquare);
        expected.add(fourthSquare);
        assertEquals(expected,gm.getValidDestinations(firstSquare,maxDistance,false));

    }



    @Test
    void putAmmoCard() {
    }

    @Test
    void putPowerupCard() {
    }

    @Test
    void addTurretSquare() {
    }

    @Test
    void addSpawnSquare() {
    }

    @Test
    void replaceDiscardedWeapons() {
    }

    @Test
    void replaceAll() {
    }
}