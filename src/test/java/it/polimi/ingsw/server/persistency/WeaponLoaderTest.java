package it.polimi.ingsw.server.persistency;

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
 * Author: Abbo Giulio A.
 * Testing: with same json as src
 */
class WeaponLoaderTest {
    /*Position of the file*/
    private static final String FILE =
            "./resources/cards/jsons/weaponcards.json";

    /*Existing entry with order*/
    private static final String EXISTING_ID = "AD_weapons_IT_022";
    private static final List<AmmoCube> EXISTING_COST =
            Arrays.asList(AmmoCube.YELLOW);
    private static final String[][] EXISTING_EFFECTS = {{"sledgehammer"}, {
            "pulverize"}};

    private WeaponLoader normalLoader;

    @BeforeEach
    void setUp() {
        normalLoader = new WeaponLoader(FILE);
    }

    /*Testing the constructor when the file can not be found*/
    @Test
    void WeaponLoader_noFile() {
        assertThrows(IllegalArgumentException.class,
                () -> new WeaponLoader(""));
    }

    /*Testing with existing id*/
    @Test
    void get_existingId() {
        WeaponCard card = normalLoader.get(EXISTING_ID);

        assertNotNull(card);
        assertEquals(EXISTING_COST, card.getCost());

        List<EffectInterface> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (EffectInterface e : sequences) {
            while (e.getDecorated() != null) {
                assertEquals(EXISTING_EFFECTS[i][j], e.getName());
                e = e.getDecorated();
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

        List<EffectInterface> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (EffectInterface e : sequences) {
            while (e.getDecorated() != null) {
                assertEquals(EXISTING_EFFECTS[i][j], e.getName());
                e = e.getDecorated();
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
