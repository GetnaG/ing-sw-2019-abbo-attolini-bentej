package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AgainstRulesException;

/**
 * A representation of a deck of Cards.
 *
 * @author Fahed Ben Tej
 * @see PowerupDeck
 * @see AmmoDeck
 * @see WeaponDeck
 */
public interface AbstractDeck {
    /**
     * Drawing a a random card from the deck.
     *
     * @return a card from the deck
     * @throws AgainstRulesException if deck is empty
     */
    AbstractCard drawCard() throws AgainstRulesException;

    /**
     * Returns the number of cards left in the deck.
     *
     * @return the number of card left in the deck.
     */
    int cardsLeft();


}