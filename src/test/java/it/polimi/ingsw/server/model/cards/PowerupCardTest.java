package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/*
 * Author: Abbo Giulio A.
 * Testing correct behaviour with normal usage
 */
class PowerupCardTest {
    private PowerupCard card;
    private String id;
    private String effect;
    private AmmoCube cube;

    @BeforeEach
    void setUp() {
        id = "Powerup id";
        effect = "effect id";
        cube = AmmoCube.BLUE;

        /*A powerup card with other fields set to true*/
        card = new PowerupCard(id, effect, cube);
    }

    /*Testing that the effect id is set correctly*/
    @Test
    void getEffect() {
        assertEquals(effect, card.getEffect().getName());
    }

    /*Testing the returned value*/
    @Test
    void isUsableAsAction() {
        assertTrue(card.isUsableAsAction());
    }

    /*Testing the returned value*/
    @Test
    void isUsableOnDealingDamage() {
        assertTrue(card.isUsableOnDealingDamage());
    }

    /*Testing the returned value*/
    @Test
    void isUsableOnReceivingDamage() {
        assertTrue(card.isUsableOnReceivingDamage());
    }

    /*Testing that the cubes returned are right*/
    @Test
    void getCube() {
        assertEquals(cube, card.getCube());
    }
}