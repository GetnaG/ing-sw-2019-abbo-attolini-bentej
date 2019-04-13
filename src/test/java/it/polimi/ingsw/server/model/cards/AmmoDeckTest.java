package it.polimi.ingsw.server.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AmmoDeckTest {
    private AmmoDeck deck1;
    private AmmoDeck deck2;

    @BeforeEach
    void setUp() {
        deck1 = new AmmoDeck();
        deck2 = new AmmoDeck();
    }

    @Test
    @Disabled // Enable once we actually implement the creating of cards from JSON
    //TODO I
    void drawCard() {

        List<AmmoCard> drawnCards = new ArrayList<>();

        //Testing that a deck doesn't contain the same card twice (using deck2).

        for (int i = 0; i<24; i++) {
            AmmoCard drawnCard = deck2.drawCard();

            assertFalse(drawnCards.contains(drawnCard));

            drawnCards.add(drawnCard);
        }

        //Testing that is always possible to draw a card from a Ammo deck.
        for (int i = 0; i < 40 ; i++) {
            deck2.drawCard();
        }

        //TODO Implements more rigorous tests once created cards using JSON
        fail();


    }

    @Test
    void cardsLeft() {
    }
}