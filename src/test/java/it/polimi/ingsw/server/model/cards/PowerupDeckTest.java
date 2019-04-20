package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.KillshotTrack;
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
    private GameBoard gm1;
    private GameBoard gm2;
    private int NOCARDS = 4;
    @BeforeEach
    void setUp() {
        gm1 = new GameBoard(new KillshotTrack(), new ArrayList<>());
        gm2 = new GameBoard(new KillshotTrack(), new ArrayList<>());
        deck1 = new PowerupDeck(gm1);
        deck2 = new PowerupDeck(gm2);
    }

    @Test
    void drawCard() {
        List<PowerupCard> drawnCards = new ArrayList<>();

        //Testing that a deck doesn't contain the same card twice (using deck2).

        for (int i = 0; i<NOCARDS; i++) {
            PowerupCard drawnCard = deck1.drawCard();

            assertFalse(drawnCards.contains(drawnCard));

            drawnCards.add(drawnCard);
        }

        //Testing that is always possible to draw a card from a powerup deck.
        for (int i = 0; i < 40 ; i++) {
            gm2.putPowerupCard(deck2.drawCard());
        }


    }

    @Test
    void cardsLeft() {
        assertEquals(NOCARDS, deck1.cardsLeft());
        assertEquals(NOCARDS, deck2.cardsLeft());

        deck1.drawCard();
        deck1.drawCard();
        assertEquals(NOCARDS-2, deck1.cardsLeft());
        assertEquals(NOCARDS-2, deck1.cardsLeft());
    }
}