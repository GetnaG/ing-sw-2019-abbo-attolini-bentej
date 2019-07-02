package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void hasSpawnSquare() {
        //1//
        Square a = new Square(SquareColor.YELLOW), b = new Square(SquareColor.BLUE), c = new Square(SquareColor.WHITE);
        List<Square> l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        Room r = new Room(l);

        assertTrue(!r.hasSpawnSquare());

        //2//
        a = new Square(SquareColor.YELLOW);
        b = new Square(SquareColor.YELLOW);
        c = new Square(SquareColor.YELLOW);
        l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        r = new Room(l);

        assertEquals(3, r.getSquares().size());
    }

    @Test
    @Disabled
    void equals() {
        //1//
        Square a = new Square(SquareColor.BLUE), b = new Square(SquareColor.BLUE), c = new Square(SquareColor.BLUE);
        Square d = new Square(SquareColor.BLUE), e = new Square(SquareColor.BLUE), f = new Square(SquareColor.BLUE);
        List<Square> l = new ArrayList<>();
        List<Square> l2 = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        Room r1 = new Room(l);
        l2.add(d);
        l2.add(e);
        l2.add(f);
        Room r2 = new Room(l2);

        assertTrue(r1.equals(r2));

        //2//
        a.setNorth(b);
        a.setNorthBorder(Border.CORRIDOR);
        d.setNorth(e);
        d.setNorthBorder(Border.DOOR);

        assertTrue(!r2.equals(r1));

        //3//
        SpawnSquare a2 = new SpawnSquare(AmmoCube.BLUE, null);
        SpawnSquare d2 = new SpawnSquare(AmmoCube.BLUE, null);
        b = new Square(SquareColor.BLUE);
        e = new Square(SquareColor.BLUE);
        List<Square> list = new ArrayList<>();
        list.add(a2);
        list.add(b);
        list.add(c);
        List<Square> list2 = new ArrayList<>();
        list2.add(d2);
        list2.add(e);
        list2.add(f);

        Room r3 = new Room(list, a2);
        Room r4 = new Room(list2, d2);

        assertTrue(r3.equals(r4));

    }
}