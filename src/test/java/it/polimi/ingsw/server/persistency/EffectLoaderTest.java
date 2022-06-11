package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: giubots
 * Testing: with resources file
 */
class EffectLoaderTest {
    /*Position of the file*/
    private static final String FILE = "effectsTest.json";

    /*Existing entry*/
    private static final String EXISTING_ID = "sledgehammer";

    private EffectLoader normalLoader;

    @BeforeEach
    void setUp() {
        normalLoader =
                new EffectLoader(EffectLoaderTest.class.getResourceAsStream(FILE));
    }

    /*Testing with existing id*/
    @Test
    void get() {
        EffectInterface effect = normalLoader.get(EXISTING_ID);

        assertNotNull(effect);
        assertEquals(EXISTING_ID, effect.getName());
    }

    /*Testing that case is ignored*/
    @Test
    void get_existingId_ignoreCase() {
        EffectInterface effect = normalLoader.get(EXISTING_ID.toLowerCase());

        assertNotNull(effect);
        assertEquals(EXISTING_ID, effect.getName());
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
    }

    /*Testing with existing id*/
    @Test
    void getAll_existingId() {
        List<EffectInterface> effects = normalLoader.getAll(EXISTING_ID);

        assertNotNull(effects);

        for (EffectInterface e : effects) {
            assertNotNull(e);
            assertEquals(EXISTING_ID, e.getName());
        }
    }

    /*Testing that case is ignored*/
    @Test
    void getAll_existingId_ignoreCase() {
        List<EffectInterface> effects = normalLoader.getAll(EXISTING_ID.toLowerCase());

        assertNotNull(effects);

        for (EffectInterface e : effects) {
            assertNotNull(e);
            assertEquals(EXISTING_ID, e.getName());
        }
    }

    /*Testing not existing id*/
    @Test
    void getAll_notExisting() {
        assertTrue(normalLoader.getAll("").isEmpty());
    }
}