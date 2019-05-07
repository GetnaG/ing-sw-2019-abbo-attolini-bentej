package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void hasSpawnSquare() {
        //1//
        Square a = new Square(Color.YELLOW), b = new Square(Color.BLUE), c = new Square(Color.WHITE);
        List<Square> l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        Room r = new Room(l);

        assertTrue(!r.hasSpawnSquare());

        //2//
        a = new Square(Color.YELLOW);
        b = new Square(Color.YELLOW);
        c = new Square(Color.YELLOW);
        l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        r = new Room(l);

        assertEquals(3, r.getSquares().size());
    }

    @Test
    void equals() {
        //1//
        Square a = new Square(Color.BLUE), b = new Square(Color.BLUE), c = new Square(Color.BLUE);
        Square d = new Square(Color.BLUE), e = new Square(Color.BLUE), f = new Square(Color.BLUE);
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
        List<Square> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        List<Square> list2 = new ArrayList<>();
        list2.add(d);
        list2.add(e);
        list2.add(f);

        Room r3 = new Room(list, a2);
        Room r4 = new Room(list2, d2);


        //assertTrue(r3.equals(r4));
    }
}