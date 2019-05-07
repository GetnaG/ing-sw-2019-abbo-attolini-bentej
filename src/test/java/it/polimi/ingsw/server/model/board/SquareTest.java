package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.AmmoCard;
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

        n2.getSouth();

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
        assertTrue(testDestinationS.checkVisible(testS));

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
        assertTrue(testDestinationS.checkVisible(testS));


        //3//
        Square testDestinationS2 = new Square(Color.YELLOW);
        listB.remove(1);
        listB.add(testDestinationS2);
        testDestinationS2.setSouth(testS);
        testS.setNorth(testDestinationS2);
        testS.setNorthBorder(Border.WALL);
        roomB.setSquares(listB);

        assertTrue(!testS.checkVisible(testDestinationS2));
        assertTrue(!testDestinationS2.checkVisible(testS));

        //4//
        testS = new Square(Color.BLUE);
        Square a = new Square(Color.GREEN);
        SpawnSquare testD = new SpawnSquare(AmmoCube.RED, null);

        testS.setSouth(a);
        a.setEast(testD);
        testS.setSouthBorder(Border.DOOR);
        a.setEastBorder(Border.DOOR);

        List<Square> list1 = new ArrayList<>();
        list1.add(testS);
        Room r1 = new Room(list1);

        List<Square> list2 = new ArrayList<>();
        list2.add(a);
        Room r2 = new Room(list2);

        List<Square> list3 = new ArrayList<>();
        list3.add(testD);
        Room r3 = new Room(list3, testD);

        assertTrue(!testS.checkVisible(testD));
        assertTrue(!testD.checkVisible(testS));
    }


    @Test
    void listOfVisibles() {
        Square testS = new Square(Color.BLUE);
        Square a = new Square(Color.GREEN);
        SpawnSquare testD = new SpawnSquare(AmmoCube.RED, null);

        testS.setSouth(a);
        a.setEast(testD);
        testS.setSouthBorder(Border.DOOR);
        a.setEastBorder(Border.DOOR);

        List<Square> list1 = new ArrayList<>();
        list1.add(testS);
        Room r1 = new Room(list1);

        List<Square> list2 = new ArrayList<>();
        list2.add(a);
        Room r2 = new Room(list2);

        List<Square> list3 = new ArrayList<>();
        list3.add(testD);
        Room r3 = new Room(list3, testD);

        List<Room> config = new ArrayList<>();
        config.add(r1);
        config.add(r2);
        config.add(r3);
        GameBoard g = new GameBoard(null, config);

        // List<Square> check = new ArrayList<>();
        //check = testS.listOfVisibles(g);

        assertEquals(2, testS.listOfVisibles(g).size());

        Square b = new Square(Color.PURPLE);
        b.setSouth(testD);
        b.setWest(testS);
        b.setSouthBorder(Border.WALL);
        b.setWestBorder(Border.CORRIDOR);

        assertEquals(3, testS.listOfVisibles(g).size());
    }


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
        assertTrue(testDestinationS.straight(testS));


        //2//
        testS = new Square();
        testDestinationS = new Square();

        testS.setWest(testDestinationS);
        testDestinationS.setEast(testS);
        testS.setWestBorder(Border.CORRIDOR);
        testDestinationS.setEastBorder(Border.CORRIDOR);

        assertTrue(testS.straight(testDestinationS));
        assertTrue(testDestinationS.straight(testS));

        //3//
        testS = new Square();
        testDestinationS = new Square();

        testS.setNorth(a);
        testS.setNorthBorder(Border.CORRIDOR);
        a.setEastBorder(Border.DOOR);
        a.setEast(testDestinationS);

        assertTrue(!testS.straight(testDestinationS));
        assertTrue(!testDestinationS.straight(testS));

    }


    @Test
    void getAmmoCard() {
        Square testS = new Square();
        AmmoCard a = new AmmoCard("test", new AmmoCube[]{}, false);
        GameBoard g = new GameBoard(null, null);


        testS.setAmmoCard(a);
        testS.setReplacer(g);

        AmmoCard card1 = testS.getAmmoCard();
        testS.replacer.replaceAll();
        AmmoCard card2 = testS.getAmmoCard();

        assert (card1 != card2);
    }

}