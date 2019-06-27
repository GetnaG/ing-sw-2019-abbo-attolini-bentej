package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.board.*;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RespawnTurnTest {

    private Player player;
    private GameBoard board;

    @BeforeEach
    void setUp() {
        player = new Player("Alice");
        board = getSetUppedGameBoard();

    }

    @Disabled // Implement once player has toClientInterface actually implemented.
    @Test
    void startTurn() {
        RespawnTurn rt = new RespawnTurn(builder -> {});
        rt.startTurn(player, board);
        assertEquals(player.getAllPowerup().get(0).getCube(), player.getPosition().getSquareColor());

    }

    private GameBoard getSetUppedGameBoard(){
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
        GameBoard gm = new GameBoard(new KillshotTrack(), conf);
        return gm;

    }
}