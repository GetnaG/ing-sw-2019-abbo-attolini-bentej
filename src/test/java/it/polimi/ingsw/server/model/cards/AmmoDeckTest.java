package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.KillshotTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AmmoDeckTest {
    private AmmoDeck deck1;
    private AmmoDeck deck2;
    private GameBoard gm1;
    private GameBoard gm2;
    @BeforeEach
    void setUp() {
        gm1 = new GameBoard(new KillshotTrack(), new ArrayList<>());
        gm2 = new GameBoard(new KillshotTrack(), new ArrayList<>());
        deck1 = new AmmoDeck(gm1);
        deck2 = new AmmoDeck(gm2);
    }

    @Test@Disabled
    void drawCard() {

        List<AmmoCard> drawnCards = new ArrayList<>();
        int NOCARDS = 2;
        //Testing that a deck doesn't contain the same card twice (using deck2).

        for (int i = 0; i<NOCARDS; i++) {
            AmmoCard drawnCard = deck2.drawCard();

            assertFalse(drawnCards.contains(drawnCard));

            drawnCards.add(drawnCard);
        }

        //Testing that is always possible to draw a card from a Ammo deck.
        for (int i = 0; i < 40 ; i++) {
            gm2.putAmmoCard(deck2.drawCard());
        }


    }

    @Test
    void cardsLeft() {
    }
}