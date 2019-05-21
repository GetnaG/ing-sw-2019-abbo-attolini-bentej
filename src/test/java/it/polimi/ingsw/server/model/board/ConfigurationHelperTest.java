package it.polimi.ingsw.server.model.board;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationHelperTest {


    @Test
    void boardCreator() {

        ConfigurationHelper ch = new ConfigurationHelper(/*Configurations.STANDARD1*/);

        List<Room> m = ch.loadRooms();

        for(Room r: m){
            for(Square s: r.getSquares())
            System.out.println("Square " + s.getID() + " : north -> " + s.getNorth() + " northB -> " + s.getNorthBorder() + "\n      " +
                                                       " : south -> " + s.getSouth() + " southB -> " + s.getSouthBorder() + "\n       " +
                                                       " : east  -> " + s.getEast() + " east B -> " + s.getEastBorder() + "\n       " +
                                                       " : west  -> " + s.getWest() + " west B -> " + s.getWestBorder() + "\n\n");
        }

        assertEquals(m.size(), 5);
    }
}