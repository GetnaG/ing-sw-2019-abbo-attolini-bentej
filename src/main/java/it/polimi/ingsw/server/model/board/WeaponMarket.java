package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.List;

/**
 *
 */
public class WeaponMarket {


    private List<WeaponCard> weaponCards;

    /**
     * Default constructor
     */
    public WeaponMarket(List<WeaponCard> weaponCards) {
        this.weaponCards = weaponCards;
    }


    public List<WeaponCard> getCards() {
        return weaponCards;
    }

    /**
     * Adds card to market. If market is full, no card is added.
     *
     * @param weaponcard is the card added to the market
     */
    public void addCard(WeaponCard weaponcard) {
        int pos = 0;
        for (WeaponCard i : weaponCards) {
            if (i == null) {
                weaponCards.set(pos, weaponcard);
                break;
            }
            pos++;
        }
    }

    /**
     * @param weapon is the weapon chosen by the player to pick
     */
    public void pickWeaponFromList(WeaponCard weapon) {
        int pos = 0;
            for (WeaponCard i : weaponCards) {
                if (i != null && i.equals(weapon)) {
                    weaponCards.set(pos, null);
                    return;
                }
                pos++;
            }
    }

    public boolean isValidWeapon(WeaponCard w){
        for (WeaponCard i: weaponCards) {
            if(i.getId().equals(w.getId()))
                return true;
        }
        return false;
    }

}