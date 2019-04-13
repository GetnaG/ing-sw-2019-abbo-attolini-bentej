package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AgainstRulesException;

import java.util.List;

/**
 * Represent an AmmoDeck. It takes care of creating the cards and shuffling them.
 * @author Fahed Ben Tej
 */
public class AmmoDeck extends AbstractDeck {

    /**
     * Represents the deck;
     */
    private List<AmmoCard> deck;
    /**
     * Constructs an AmmoDeck already shuffled. It takes care of creating the cards.
     * A new Ammo Deck contains 36 cards.
     */
    public AmmoDeck() {
        //TODO Read cards from JSON and shuffle them;


    }

    /**
     * Drawing a random Ammo Card from the deck.
     * @return a card from the deck
     * @throws AgainstRulesException if deck is empty
     */
    @Override
    public AmmoCard drawCard() {
        if (deck.isEmpty())
             ;

        AmmoCard  drawnCard = deck.get(deck.size() -1 ) ;
        deck.remove(drawnCard);
        return drawnCard;
    }

    /**
     * Returns the number of cards left in the deck.
     *
     * @return the number of card left in the deck.
     */
    @Override
    public int cardsLeft() {
        return deck.size();
    }

    /**
     * Takes the discarded Ammo Cards and shuffles them.
     */
    private void shuffleDeck() {
        //TODO Implement how to get the discarded cards once JSON-reading task is completed.
    }

}

