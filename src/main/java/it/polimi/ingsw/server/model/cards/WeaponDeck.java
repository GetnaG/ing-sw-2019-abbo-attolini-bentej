package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.persistency.FromFile;

import java.util.Collections;
import java.util.List;

/**
 * Represents a WeaponDeck. It takes care of creating the cards and shuffles them.
 * According to the rules, a Weapon Deck contains 21 weapon cards.
 * Once the deck is empty, we can no longer draw a card from it.
 *
 * @author Fahed Ben Tej
 */
public class WeaponDeck implements AbstractDeck {

    /**
     * Represents the deck.
     */
    private List<WeaponCard> deck;

    /**
     * Constructs a Weapon Deck. It takes care of creating the cards and shuffling them.
     * A new Weapon Deck contains 21 cards.
     */
    public WeaponDeck() {
        deck = FromFile.weapons().getAll();
        Collections.shuffle(deck);
    }


    @Override
    public WeaponCard drawCard() throws AgainstRulesException {
        if (deck.isEmpty())
            throw new AgainstRulesException("Deck is empty.");

        WeaponCard drawnCard = deck.get(deck.size() - 1);
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