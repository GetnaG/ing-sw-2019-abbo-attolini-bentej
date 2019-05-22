package it.polimi.ingsw.client.resources;

import org.junit.jupiter.api.Test;

import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: testing is not done on test resources, but on the actual ones.
 */
class RTest {
    @Test
    void string() {
        assertEquals("game", R.string("gameTest"));
        assertEquals("label", R.string("labelTest"));

        assertThrows(MissingResourceException.class,
                () -> R.string("notExisting"));
    }

    @Test
    void image() {
        assertDoesNotThrow(() -> R.image("AD_ammo_04"));

        assertThrows(MissingResourceException.class,
                () -> R.image("notExisting"));

    }
}