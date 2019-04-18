package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/*
 * Author: Abbo Giulio A.
 * Testing correct behaviour with normal usage; equals and hashCode
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

    /*Testing returned id*/
    @Test
    void getId() {
        assertEquals(id, card.getId());
    }

    /*Testing equals method (abstract card are equal if they have the same id)*/
    @Test
    void equals_AbstractCard() {
        assertEquals(card, card);
        assertEquals(card, new AmmoCard(id, cubes, hasPowerup));

        /*With a different card*/
        assertNotEquals(card, new AmmoCard("test", cubes, hasPowerup));

        /*With another abstract card*/
        assertEquals(card, new AbstractCard(id) {
        });
    }

    /*Testing that hashCode works*/
    @Test
    void hashCode_AbstractCard() {
        /*Equal cards*/
        assertEquals(card.hashCode(),
                new AmmoCard(id, cubes, hasPowerup).hashCode());

        /*Different cards*/
        assertNotEquals(card.hashCode(),
                new AmmoCard("test", cubes, hasPowerup).hashCode());

        /*Another abstract card equal to this*/
        assertEquals(card.hashCode(), (new AbstractCard(id) {
        }).hashCode());
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