package it.polimi.ingsw.server.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/*
 * Author: giubots
 * Testing: implemented methods
 */
class AbstractCardTest {
    private AbstractCard card;
    private String id;

    @BeforeEach
    void setUp() {
        id = "testing id";
        card = new AbstractCard(id) {
        };
    }

    /*Testing the returned value*/
    @Test
    void getId() {
        assertEquals(id, card.getId());
    }

    /*Testing equals method (abstract card are equal if they have the same id)*/
    @Test
    void equals_AbstractCard() {
        /*Equal cards*/
        assertEquals(card, card);
        assertEquals(card, new AbstractCard(id) {
        });

        /*With a different card*/
        assertNotEquals(card, new AbstractCard("other") {
        });

        /*With null*/
        assertNotEquals(card, null);
    }

    /*Testing that hashCode works*/
    @Test
    void hashCode_AbstractCard() {
        /*Equal cards*/
        assertEquals(card.hashCode(), card.hashCode());
        assertEquals(card.hashCode(), new AbstractCard(id) {
        }.hashCode());

        /*Different cards*/
        assertNotEquals(card.hashCode(), new AbstractCard("other") {
        }.hashCode());
    }
}