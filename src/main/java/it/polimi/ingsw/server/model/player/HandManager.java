package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.*;

/**
 * 
 */
public class HandManager {

    /**
     * Default constructor
     */
    public HandManager() {
    }

    /**
     * 
     */
    private List<WeaponCard> loadedWeapons;

    /**
     * 
     */
    private List<WeaponCard> unloadedWeapons;

    /**
     * 
     */
    private List<PowerupCard> powerups;


    /**
     * @param card 
     * @return
     */
    public boolean addPowerup(PowerupCard card) {
        // TODO implement here
        return false;
    }

    /**
     * @param card 
     * @return
     */
    public WeaponCard addWeaponCard(WeaponCard card) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<PowerupCard> getPowerups() {
        // TODO implement here
        return null;
    }

    /**
     * @param card
     */
    public void removePowerup(PowerupCard card) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<WeaponCard> getLoadedWeapons() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<WeaponCard> getUnloadedWeapons() {
        // TODO implement here
        return null;
    }

    /**
     * @param weapon
     */
    public void unload(WeaponCard weapon) {
        // TODO implement here
    }

    /**
     * @param weapon
     */
    public void reload(WeaponCard weapon) {
        // TODO implement here
    }

}