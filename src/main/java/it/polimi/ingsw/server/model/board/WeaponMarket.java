package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.List;

/**
 * This class manages the WeaponMarket found in spawn squares,
 * it always contains three weapon cards
 * if the card is not set a null value is assigned
 */
public class WeaponMarket {


    /**
     * Represents the weapon cards that characterise the market
     * <p>
     * It is filled at the beginning of the game
     * It is Updated each time a player picks a weapon
     * It is Refilled during the end of the turn
     */
    private List<WeaponCard> weaponCards;

    /**
     * Default constructor
     */
    public WeaponMarket(List<WeaponCard> weaponCards) {
        this.weaponCards = weaponCards;
    }

    /**
     * Returns the cards in the market.
     *
     * @return the cards in the market
     */
    public List<WeaponCard> getCards() {
        return weaponCards;
    }

    /**
     * Adds card to market. If market is full, no card is added.
     *
     * @param weaponCard is the card added to the market
     */
    public void addCard(WeaponCard weaponCard) {
        int pos = 0;
        for (WeaponCard i : weaponCards) {
            if (i == null) {
                weaponCards.set(pos, weaponCard);
                break;
            }
            pos++;
        }
    }

    /**
     * @param weapon is the weapon chosen by the player to pick
     */
    void pickWeaponFromList(WeaponCard weapon) {
        int pos = 0;
        for (WeaponCard i : weaponCards) {
            if (i != null && i.equals(weapon)) {
                weaponCards.set(pos, null);
                return;
            }
            pos++;
        }
    }

    /**
     * Returns true if the provided weapon is valid and in this market.
     *
     * @param w the card to check
     * @returntrue if the provided weapon is valid and in this market
     */
    boolean isValidWeapon(WeaponCard w) {
        for (WeaponCard i : weaponCards) {
            if (i != null && i.getId().equals(w.getId()))
                return true;
        }
        return false;
    }
}