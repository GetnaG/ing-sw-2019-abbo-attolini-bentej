package it.polimi.ingsw.server.model.cards;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerupDeckTest {

    private PowerupDeck deck1;
    private PowerupDeck deck2;

    @BeforeEach
    void setUp() {
        deck1 = new PowerupDeck();
        deck2 = new PowerupDeck();
    }

    @Test
    @Disabled // Enable once we actually implement the creating of cards from JSON
    void drawCard() {

        List<PowerupCard> drawnCards = new ArrayList<>();

        //Testing that a deck doesn't contain the same card twice (using deck2).

        for (int i = 0; i<24; i++) {
            PowerupCard drawnCard = deck2.drawCard();

            assertFalse(drawnCards.contains(drawnCard));

            drawnCards.add(drawnCard);
        }

        //Testing that is always possible to draw a card from a powerup deck.
        for (int i = 0; i < 40 ; i++) {
            deck2.drawCard();
        }

        //TODO Implements more rigorous tests once created cards using JSON
        fail();

    }

    @Test
    @Disabled // Enable once we actually implement the creating of cards from JSON
    void cardsLeft() {
        assertEquals(24, deck1.cardsLeft());
        assertEquals(24, deck2.cardsLeft());

        deck1.drawCard();
        deck1.drawCard();
        assertEquals(22, deck1.cardsLeft());
        assertEquals(24, deck2.cardsLeft());
    }
}