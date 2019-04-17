package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;

/**
 * 
 */
public class PowerupCard extends AbstractCard {

    /**
     * Default constructor
     */
    public PowerupCard(AmmoCube color) {
        this.cube = color;
    }

    /**
     * @return
     */

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

    public AmmoCube getCube() {
        return cube;
    }
}