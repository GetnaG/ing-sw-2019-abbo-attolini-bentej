package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: giubots
 * Testing correct behaviour with normal usage
 */
class AmmoCardTest {
    private AmmoCard card;
    private String id;
    private AmmoCube[] cubes;
    private Boolean hasPowerup;

    @BeforeEach
    void setUp() {
        id = "my id";
        cubes = new AmmoCube[]{AmmoCube.YELLOW, AmmoCube.RED};
        hasPowerup = false;
        card = new AmmoCard(id, cubes, hasPowerup);
    }

    /*Testing that the cubes are returned right*/
    @Test
    void getCubes() {
        assertEquals(Arrays.asList(cubes), card.getCubes());
    }

    /*Testing that has powerup works*/
    @Test
    void hasPowerup() {
        assertEquals(hasPowerup, card.hasPowerup());
    }
}