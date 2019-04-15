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


    /**
     * 
     */
    private WeaponCard[] cards;

    /**
     * @return
     */
    public List<WeaponCard> getCards() {
        return weaponCards;
    }

    /**
     * Adds card to market. If market is full, no card is added.
     */
    public void addCard(WeaponCard weaponCard){
        
    }

}