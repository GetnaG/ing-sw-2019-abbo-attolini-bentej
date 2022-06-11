package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: giubots
 * Testing: with resources file
 */
class PowerupLoaderTest {
    /*Position of the file*/
    private static final String FILE = "powerupcardsTest.json";

    /*Existing entry*/
    private static final String EXISTING_ID = "AD_powerups_IT_022";
    private static final boolean EXISTING_AS_ACTION = false;
    private static final boolean EXISTING_ON_DEALING = false;
    private static final boolean EXISTING_ON_RECEIVING = true;
    private static final String EXISTING_EFFECT = "plasmaGun";
    private static final AmmoCube EXISTING_COLOR = AmmoCube.BLUE;

    private PowerupLoader normalLoader;

    @BeforeEach
    void setUp() {
        normalLoader =
                new PowerupLoader(PowerupLoaderTest.class.getResourceAsStream(FILE));
    }

    /*Testing with existing id*/
    @Test
    void get_existingId() {
        PowerupCard card = normalLoader.get(EXISTING_ID);

        assertNotNull(card);
        assertEquals(EXISTING_AS_ACTION, card.isUsableAsAction());
        assertEquals(EXISTING_ON_DEALING, card.isUsableOnDealingDamage());
        assertEquals(EXISTING_ON_RECEIVING, card.isUsableOnReceivingDamage());
        assertEquals(EXISTING_EFFECT, card.getEffect().getName());
        assertEquals(EXISTING_COLOR, card.getCube());
    }

    /*Testing that case is ignored*/
    @Test
    void get_existingId_ignoreCase() {
        PowerupCard card = normalLoader.get(EXISTING_ID.toLowerCase());

        assertNotNull(card);
        assertEquals(EXISTING_AS_ACTION, card.isUsableAsAction());
        assertEquals(EXISTING_ON_DEALING, card.isUsableOnDealingDamage());
        assertEquals(EXISTING_ON_RECEIVING, card.isUsableOnReceivingDamage());
        assertEquals(EXISTING_EFFECT, card.getEffect().getName());
        assertEquals(EXISTING_COLOR, card.getCube());
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

        assertTrue(normalLoader.getAll().contains(
                new PowerupCard(EXISTING_ID, EXISTING_EFFECT, EXISTING_COLOR)));
    }

    /*Testing with existing id*/
    @Test
    void getAll_existingId() {
        List<PowerupCard> cards = normalLoader.getAll(EXISTING_ID);

        assertNotNull(cards);

        for (PowerupCard c : cards) {
            assertNotNull(c);
            assertEquals(EXISTING_ID, c.getId());
        }
    }

    /*Testing that case is ignored*/
    @Test
    void getAll_existingId_ignoreCase() {
        List<PowerupCard> cards = normalLoader.getAll(EXISTING_ID.toUpperCase());

        assertNotNull(cards);

        for (PowerupCard c : cards) {
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