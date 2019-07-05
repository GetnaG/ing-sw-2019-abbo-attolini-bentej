package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.persistency.FromFile;

import java.util.Collections;
import java.util.List;

/**
 * Represent an AmmoDeck. It takes care of creating the cards and shuffling them.
 *
 * @author Fahed Ben Tej
 */
public class AmmoDeck implements AbstractDeck {

    /**
     * Represents the deck;
     */
    private List<AmmoCard> deck;
    /**
     * Game Board
     */
    private GameBoard board;

    /**
     * Constructs an AmmoDeck already shuffled. It takes care of creating the cards.
     * A new Ammo Deck contains 36 cards.
     */
    public AmmoDeck(GameBoard board) {
        this.board = board;

        deck = FromFile.ammoCards().getAll();
        Collections.shuffle(deck);
    }

    /**
     * Drawing a random Ammo Card from the deck. If the deck is empty re-shuffles the deck. This means that will always return an Ammo Card.
     *
     * @return a card from the deck
     */
    @Override
    public AmmoCard drawCard() {
        if (deck.isEmpty()) {
            deck = board.getDiscardedAmmos();
            Collections.shuffle(deck);
        }
        AmmoCard drawnCard = deck.get(deck.size() - 1);
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

