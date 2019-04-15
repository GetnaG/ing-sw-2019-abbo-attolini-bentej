package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.*;

/**
 * 
 */
public class WeaponMarket {

    private List<WeaponCard> weaponCards;

    /**
     * Default constructor
     */
    public WeaponMarket() {
    }


    public List<WeaponCard> getCards() {
        return weaponCards;
    }

    /**
     * Adds card to market. If market is full, no card is added.
     */
    public void addCard(WeaponCard weaponCard){
        
    }

    public void removeCardFromMarket(WeaponCard w){
        /*
        if w is in weaponcards
            remove w
         */
    }

    public WeaponCard pickWeaponFromList(){
        /*
        the player chooses a card to pick
        this.removeCardFromMarket(weapon);
         */
       // return weapon;
        return null;
    }

}