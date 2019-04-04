package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.controller.*;
import java.util.*;

/**
 * 
 */
public class PowerupCard extends AbstractCard {

    /**
     * Default constructor
     */
    public PowerupCard() {
    }

    /**
     * 
     */
    private boolean usableAsAction;

    /**
     * 
     */
    private boolean usableOnDealingDamage;

    /**
     * 
     */
    private boolean usableOnReceivingDamage;


    /**
     * 
     */
    private AmmoCube cube;

    /**
     * @return
     */
    public EffectInterface getEffect() {
        // TODO implement here
        return null;
    }

}