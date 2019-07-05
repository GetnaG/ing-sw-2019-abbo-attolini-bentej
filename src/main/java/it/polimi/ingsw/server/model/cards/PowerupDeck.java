package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.persistency.FromFile;

import java.util.Collections;
import java.util.List;

/**
 * Represents the Powerup Deck. It takes care of creating the cards and shuffles them.
 */
public class PowerupDeck implements AbstractDeck {
    /**
     * Represents the deck.
     */
    private List<PowerupCard> deck;
    /**
     * Game Board used/
     */
    private GameBoard board;

    /**
     * Constructs a Powerup Deck. It takes care of creating the cards and shuffling them.
     * A new Powerup Deck contains 24 cards.
     * Once the Powerup Deck is empty, we can take create a new Powerup Deck using the discarded cards. This task is taken care by method {@code shuffleDeck()} called by method {@code drawCard()}.
     * This means that a call to method {@code drawCard()} will always be successful.
     */
    public PowerupDeck(GameBoard board) {
        this.board = board;

        deck = FromFile.powerups().getAll();
        Collections.shuffle(deck);
    }


    /**
     * Draws a Powerup Card.
     * If the powerup deck is empty, the deck is rebuilt using discarded cards.
     * This permits to always draw a card.
     *
     * @return a Powerup Card
     */
    @Override
    public PowerupCard drawCard() {
        if (deck.isEmpty()) {
            deck = board.getDiscardedPowerups();
            Collections.shuffle(deck);
        }

        PowerupCard drawnCard = deck.get(deck.size() - 1);
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

}