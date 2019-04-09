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
    public PowerupCard() {
    }

    /**
     * @return
     */
    @Override
    public String getResName() {
        return null;
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