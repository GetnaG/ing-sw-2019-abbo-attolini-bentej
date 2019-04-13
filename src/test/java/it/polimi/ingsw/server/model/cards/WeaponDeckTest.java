package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AgainstRulesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    private WeaponDeck deck1;
    private WeaponDeck deck2;

    @BeforeEach
    void setUp() {
        deck1 = new WeaponDeck();
        deck2 = new WeaponDeck();
    }

    @Test
    @Disabled // Enable once we actually implement the creating of cards from JSON
    void drawCard() {

        List<WeaponCard> drawnCards = new ArrayList<>();

        //Testing that a deck doesn't contain the same card twice (using deck2).

        for (int i = 0; i<24; i++) {
            try {
                WeaponCard drawnCard = deck2.drawCard();
                assertFalse(drawnCards.contains(drawnCard));
                drawnCards.add(drawnCard);

            } catch (AgainstRulesException e) {
                System.out.println(e.getMessage());
            }
        }

        //Testing that is not always possible to draw a card from a weapon deck (using deck1).
       //
        deck1 = new WeaponDeck();
        //Should be no problem drawing all 24 cards.
        for (int i = 0; i < 24 ; i++) {
            try{
                deck1.drawCard();
            }catch(AgainstRulesException e) {
            }finally {
                assertEquals(0,deck1.cardsLeft());
            }
        }
        //Should raise an AgainstRulesException now
        boolean flagExceptionRaised = false;
        try{ deck1.drawCard();}
        catch(AgainstRulesException e)
            { flagExceptionRaised = true ; }
        finally {
            assertTrue(flagExceptionRaised);
        }

    }

    @Test
    void cardsLeft() {
    }
}