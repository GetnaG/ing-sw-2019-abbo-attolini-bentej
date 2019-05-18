package it.polimi.ingsw.server.persistency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: that the same object is returned an it is not null.
 */
class FromFileTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void ammoCards() {
        BasicLoader loader = FromFile.ammoCards();
        assertNotNull(loader);
        assertEquals(loader, FromFile.ammoCards());
    }

    @Test
    void powerups() {
        BasicLoader loader = FromFile.powerups();
        assertNotNull(loader);
        assertEquals(loader, FromFile.powerups());
    }

    @Test
    void weapons() {
        BasicLoader loader = FromFile.weapons();
        assertNotNull(loader);
        assertEquals(loader, FromFile.weapons());
    }

    @Test
    void effects() {
        BasicLoader loader = FromFile.effects();
        assertNotNull(loader);
        assertEquals(loader, FromFile.effects());
    }
}