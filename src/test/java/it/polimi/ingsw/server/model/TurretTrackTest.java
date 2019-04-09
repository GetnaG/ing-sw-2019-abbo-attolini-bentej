package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurretTrackTest {

    /**
     * The turret track we are testing
     */
    private TurretTrack tt;

    @BeforeEach
    void setUp() {
        List<AmmoCard> cards = new ArrayList<>();
        tt = new TurretTrack(cards);
    }



    @Test
    void removeSkull() {
        tt.removeSkull();
        assertEquals(tt.getSkullsLeft(),7);
        tt.removeSkull();
        tt.removeSkull();
        tt.removeSkull();
        assertEquals(tt.getSkullsLeft(),4);
    }
}