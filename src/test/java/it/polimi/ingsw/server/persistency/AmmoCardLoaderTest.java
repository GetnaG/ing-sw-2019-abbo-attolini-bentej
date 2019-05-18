package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.AmmoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: with resources file
 */
class AmmoCardLoaderTest {
    /*Position of the file*/
    private static final String FILE = "ammocardsTest.json";

    /*Existing entry*/
    private static final String EXISTING_ID = "AD_ammo_042";
    private static final AmmoCube[] EXISTING_CUBES = {
            AmmoCube.YELLOW,
            AmmoCube.BLUE,
            AmmoCube.BLUE};
    private static final boolean EXISTING_POWERUP = false;

    private AmmoCardLoader normalLoader;

    @BeforeEach
    void setUp() {
        normalLoader =
                new AmmoCardLoader(AmmoCardLoaderTest.class.getResourceAsStream(FILE));
    }

    /*Testing with existing id*/
    @Test
    void get_existingId() {
        AmmoCard card = normalLoader.get(EXISTING_ID);

        assertNotNull(card);
        assertEquals(EXISTING_ID, card.getId());
        assertEquals(Arrays.asList(EXISTING_CUBES), card.getCubes());
        assertEquals(EXISTING_POWERUP, card.hasPowerup());
    }

    /*Testing that case is ignored*/
    @Test
    void get_existingId_ignoreCase() {
        AmmoCard card = normalLoader.get(EXISTING_ID.toLowerCase());

        assertNotNull(card);
        assertEquals(EXISTING_ID, card.getId());
        assertEquals(Arrays.asList(EXISTING_CUBES), card.getCubes());
        assertEquals(EXISTING_POWERUP, card.hasPowerup());
    }

    /*Testing not existing id*/
    @Test
    void get_notExisting() {
        assertThrows(NoSuchElementException.class,
                () -> normalLoader.get(""));
    }

    /*Testing that returns something (could be empty)*/
    @Test
    void getAll_noParam() {
        assertNotNull(normalLoader.getAll());

        assertTrue(normalLoader.getAll().contains(new AmmoCard(EXISTING_ID,
                EXISTING_CUBES, EXISTING_POWERUP)));
    }

    /*Testing with existing id*/
    @Test
    void getAll_existingId() {
        List<AmmoCard> cards = normalLoader.getAll(EXISTING_ID);

        assertNotNull(cards);

        for (AmmoCard c : cards) {
            assertNotNull(c);
            assertEquals(EXISTING_ID, c.getId());
        }
    }

    /*Testing that case is ignored*/
    @Test
    void getAll_existingId_ignoreCase() {
        List<AmmoCard> cards = normalLoader.getAll(EXISTING_ID.toLowerCase());

        assertNotNull(cards);

        for (AmmoCard c : cards) {
            assertNotNull(c);
            assertEquals(EXISTING_ID, c.getId());
        }
    }

    /*Testing not existing id*/
    @Test
    void getAll_notExisting() {
        assertTrue(normalLoader.getAll("").isEmpty());
    }
}