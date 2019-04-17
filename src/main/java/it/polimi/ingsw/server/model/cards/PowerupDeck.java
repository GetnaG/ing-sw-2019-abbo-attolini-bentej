package it.polimi.ingsw.server.model.cards;

import java.util.List;

/**
 * Represents the Powerup Deck. It takes care of creating the cards and shuffles them.
 */
public class PowerupDeck extends AbstractDeck {
    /**
     * Represents the deck.
     */
    private List<PowerupCard> deck;

    /**
     * Constructs a Powerup Deck. It takes care of creating the cards and shuffling them.
     * A new Powerup Deck contains 24 cards.
     * Once the Powerup Deck is empty, we can take create a new Powerup Deck using the discarded cards. This task is taken care by method {@code shuffleDeck()} called by method {@code drawCard()}.
     * This means that a call to method {@code drawCard()} will always be successful.
     * @author Fahed Ben Tej
     */
    public PowerupDeck() {
        //TODO Create Powerup Cards from JSON and shuffle them.
    }


    /**
     * Draws a Powerup Card.
     * If the powerup deck is empty, the deck is rebuilt using discarded cards.
     * This permits to always draw a card.
     * @return a Powerup Card
     */
    @Override
    public PowerupCard drawCard() {
        if (deck.isEmpty())
            shuffleDeck();

        PowerupCard  drawnCard = deck.get(deck.size() -1 ) ;
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
     * Takes the discarded Powerup Cards and shuffles them.
     */
    private void shuffleDeck() {
        //TODO Implement how to get the discarded cards once JSON-reading task is completed.
    }
}