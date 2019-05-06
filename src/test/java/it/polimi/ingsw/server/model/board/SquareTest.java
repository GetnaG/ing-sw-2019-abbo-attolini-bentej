package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SquareTest {

    @Test
    void getCardinals() {
        List<Square> cardinalList;
        int len;
        Square testS = new Square();
        Square n1 = new Square();
        Square n2 = new Square();
        Square s1 = new Square();
        Square s2 = new Square();
        Square e1 = new Square();

        testS.setNorth(n1);
        n1.setNorth(n2);

        testS.setSouth(s1);
        s1.setSouth(s2);

        testS.setEast(e1);

        cardinalList = testS.getCardinals();
        len = cardinalList.size();

        assertEquals(6, len);
    }

    @Test
    void checkVisible() {
        //1//
        Square testS = new Square(Color.BLUE);
        Square testDestinationS = new Square(Color.BLUE);
        List<Square> listA = new ArrayList<>();
        SpawnSquare testSPSQ = new SpawnSquare(AmmoCube.BLUE, null);
        listA.add(testSPSQ);
        listA.add(testS);
        listA.add(testDestinationS);
        Room roomA = new Room(listA, testSPSQ);

        assertTrue(testS.getRoom().equals(testDestinationS.getRoom()));
        assertTrue(testS.checkVisible(testDestinationS));

        //2//
        listA.remove(2);

        testS.setNorth(testDestinationS);
        testDestinationS.setSouth(testS);
        testDestinationS.setColor(Color.YELLOW);
        testS.setNorthBorder(Border.DOOR);

        List<Square> listB = new ArrayList<>();
        SpawnSquare testSPSQ2 = new SpawnSquare(AmmoCube.YELLOW, null);
        listB.add(testSPSQ2);
        listB.add(testDestinationS);
        Room roomB = new Room(listB, testSPSQ2);

        assertTrue(testS.checkVisible(testDestinationS));

        //3//
        Square testDestinationS2 = new Square(Color.YELLOW);
        listB.remove(1);
        listB.add(testDestinationS2);
        testDestinationS2.setSouth(testS);
        testS.setNorth(testDestinationS2);
        testS.setNorthBorder(Border.WALL);
        roomB.setSquares(listB);

        assertTrue(!testS.checkVisible(testDestinationS2));
    }

    /*
    @Test
    void listOfVisibles() {
    }
    */

    @Test
    void equals() {
        Square a = new Square(Color.BLUE);
        Square b = new Square(Color.BLUE);
        Square x = new Square();
        Square y = new Square();
        Square z = new Square();

        //1//
        a.setNorth(x);
        b.setNorth(x);
        a.setEast(y);
        b.setEast(y);
        a.setWest(z);
        b.setWest(z);

        assertTrue(a.equals(b));
        assertEquals(a, b);

        //2//
        b.setNorth(null);
        b.setWest(x);
        b.setSouth(y);
        b.setEast(null);

        assertTrue(!a.equals(b));

        //3//
        b.setColor(Color.YELLOW);

        assertTrue(!a.equals(b));

    }


    @Test
    void straight() {
        Square testS = new Square();
        Square testDestinationS = new Square();
        Square a = new Square(), b = new Square(), c = new Square(), d = new Square();

        //1//
        testS.setNorth(a);
        testDestinationS.setNorth(a);
        testS.setSouth(b);
        testDestinationS.setSouth(b);
        testS.setEast(c);
        testDestinationS.setEast(c);
        testS.setWest(d);
        testDestinationS.setWest(d);

        assertTrue(testS.straight(testDestinationS));

        //2//
        testS = new Square();
        testDestinationS = new Square();
        testS.setWest(testDestinationS);
        testDestinationS.setEast(testS);
        testS.setWestBorder(Border.CORRIDOR);
        testDestinationS.setEastBorder(Border.CORRIDOR);

        assertTrue(testS.straight(testDestinationS));

    }

}