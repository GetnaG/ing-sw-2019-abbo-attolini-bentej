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
     * @param weaponcard is the card added to the market
     */
    public void addCard(WeaponCard weaponcard){
        int pos = 0;
        for(WeaponCard i: weaponCards)
        {
            if(i==null){
                weaponCards.set(pos, weaponcard);
                break;
            }
            pos++;
        }
    }


    public void removeCardFromMarket(WeaponCard w){
        int pos = 0;
        for(WeaponCard i: weaponCards)
        {
            if(i.equals(w))
            {
                weaponCards.set(pos, null);
            }

        }
    }

    /**
     * @return null is returned if the player can't afford to pay the cost to pick the selected weapon
     *
     */

    public WeaponCard pickWeaponFromList(WeaponCard weapon){
        this.removeCardFromMarket(weapon);
        return weapon;
    }

}