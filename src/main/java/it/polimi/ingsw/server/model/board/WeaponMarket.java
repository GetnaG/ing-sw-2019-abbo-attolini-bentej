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

    public WeaponMarket() {

    }


    public List<WeaponCard> getCards() {
        return weaponCards;
    }

    /**
     * Adds card to market. If market is full, no card is added.
     * @param weaponcard
     */
    public void addCard(WeaponCard weaponcard){
        int pos = 0;
        for(WeaponCard i: weaponCards)
        {
            if(i==null){
                weaponCards.set(pos, weaponcard);
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

            /*
            else throw: error: weapon not found
             */

        }
    }

    /**
     * @return null is returned if the player can't afford to pay the cost to pick the selected weapon
     * alternative solution: filter the weapon to show to the player
     */

    public WeaponCard pickWeaponFromList(){  //pick weapon from filtered list???
        /*
        the player chooses a card to pick  // among the filtered market
        WeaponCard weapon = player...
                */
        WeaponCard weapon = new WeaponCard();// -- remove this line after defining the method player.pickWeapon

       /*
       check: is the selected weapon available to the player? is he able to pay the cost?
        */

        this.removeCardFromMarket(weapon);
        return weapon;
    }

}