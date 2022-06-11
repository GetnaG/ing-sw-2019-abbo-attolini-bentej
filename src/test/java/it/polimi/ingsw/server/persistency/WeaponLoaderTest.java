package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: giubots
 * Testing: with resources file
 */
class WeaponLoaderTest {
    /*Position of the file*/
    private static final String FILE = "weaponcardsTest.json";

    /*Existing entry with order*/
    private static final String EXISTING_ID = "AD_weapons_IT_022";
    private static final List<AmmoCube> EXISTING_COST =
            Arrays.asList(AmmoCube.YELLOW);
    private static final String[][] EXISTING_EFFECTS = {{"sledgehammer"}, {
            "sledgehammer_pulverize"}};

    private WeaponLoader normalLoader;

    @BeforeEach
    void setUp() {
        normalLoader =
                new WeaponLoader(WeaponLoaderTest.class.getResourceAsStream(FILE));
    }

    /*Testing with existing id*/
    @Test
    void get_existingId() {
        WeaponCard card = normalLoader.get(EXISTING_ID);

        assertNotNull(card);
        assertEquals(EXISTING_COST, card.getCost());

        List<Action> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (Action action : sequences) {
            for (EffectInterface e : action) {
                assertEquals(EXISTING_EFFECTS[i][j], e.getName());
                j++;
            }
            j = 0;
            i++;
        }
    }

    /*Testing that case is ignored*/
    @Test
    void get_existingId_ignoreCase() {
        WeaponCard card = normalLoader.get(EXISTING_ID.toUpperCase());

        assertNotNull(card);
        assertEquals(EXISTING_COST, card.getCost());


        List<Action> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (Action action : sequences) {
            for (EffectInterface e : action) {
                assertEquals(EXISTING_EFFECTS[i][j], e.getName());
                j++;
            }
            j = 0;
            i++;
        }
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
                new WeaponCard(EXISTING_ID, EXISTING_COST, EXISTING_EFFECTS,
                        true)));
    }

    /*Testing with existing id*/
    @Test
    void getAll_existingId() {
        List<WeaponCard> cards = normalLoader.getAll(EXISTING_ID);

        assertNotNull(cards);

        for (WeaponCard c : cards) {
            assertNotNull(c);
            assertEquals(EXISTING_ID, c.getId());
        }
    }

    /*Testing that case is ignored*/
    @Test
    void getAll_existingId_ignoreCase() {
        List<WeaponCard> cards = normalLoader.getAll(EXISTING_ID.toUpperCase());

        assertNotNull(cards);

        for (WeaponCard c : cards) {
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
