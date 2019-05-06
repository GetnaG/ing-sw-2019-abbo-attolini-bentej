package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
       // testDestinationS.setSouthBorder(Border.DOOR);

        assertTrue(testDestinationS.getSouthBorder() == Border.DOOR);

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

        assertTrue(testDestinationS2.getRoom() == roomB);

        //assertTrue(!testS.checkVisible(testDestinationS2));

    }

    /*
    @Test
    void listOfVisibles() {
    }

    @Test
    void equals() {
    }

    @Test
    void straight() {
    }
    */
}