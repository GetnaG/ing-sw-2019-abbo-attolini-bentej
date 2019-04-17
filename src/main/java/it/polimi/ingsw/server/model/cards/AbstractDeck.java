package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AgainstRulesException;

/**
 * A representation of a deck of Cards. As an abstract class, this class cannot be instantiated.
 * @see PowerupDeck
 * @see AmmoDeck
 * @see WeaponDeck
 * @author Fahed Ben Tej
 *
 */
public abstract class AbstractDeck  {


    /**
     * Drawing a a random card from the deck.
     * @return a card from the deck
     * @throws  AgainstRulesException if deck is empty
     */

    public abstract AbstractCard drawCard() throws AgainstRulesException;

    /**
     * Returns the number of cards left in the deck.
     * @return the number of card left in the deck.
     */
    public abstract int cardsLeft();


}