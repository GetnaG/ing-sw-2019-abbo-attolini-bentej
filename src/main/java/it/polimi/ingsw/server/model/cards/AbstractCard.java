package it.polimi.ingsw.server.model.cards;
/**
 * Represents a general-purpose abstract card.
 * It is extended by the following classes:
 * {@code AmmoCard}
 * {@code PowerupCard}
 * {@code WeaponCard}
 */
public abstract class AbstractCard {

    /**
     * Default constructor
     */
    public AbstractCard() {
    }

    /**
     * @return
     */
    public abstract String getResName() ;

}