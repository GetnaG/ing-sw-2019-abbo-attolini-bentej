package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.board.*;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NormalTurnTest {
    private GameBoard board;
    private Player player;

    // Card are needed

    @Disabled
    @BeforeEach
    void setUp() {

        GameBoard board = setUpGameBoard();
        player = new Player("Alice");


    }
    @Disabled
    @Test
    void startTurn() {
    }

    private GameBoard setUpGameBoard(){
        //Creating a simple configuration
        List<Square> squares = new ArrayList<>();
        List<Room> conf = new ArrayList<>();

        squares.add(new SpawnSquare(AmmoCube.BLUE, new WeaponMarket(new ArrayList<>())));
        squares.add(new Square(SquareColor.BLUE));
        squares.add(new Square(SquareColor.GREEN));
        squares.add(new Square(SquareColor.BLUE));
        // Adding rooms and doors.
        SpawnSquare square1 = new SpawnSquare(AmmoCube.BLUE, new WeaponMarket(new ArrayList<>()));
        Square square2 = new Square(SquareColor.BLUE);
        Square square3 = new Square(SquareColor.GREEN);
        Square square4 = new Square(SquareColor.BLUE);

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