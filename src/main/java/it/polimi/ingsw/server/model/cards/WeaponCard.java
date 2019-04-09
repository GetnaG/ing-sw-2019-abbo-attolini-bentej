package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;

import java.util.List;

/**
 * 
 */
public class WeaponCard extends AbstractCard {

    /**
     * Default constructor
     */
    public WeaponCard() {
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
    private List<AmmoCube> cost;


    /**
     * @return
     */
    public List<EffectInterface> getPossibleSequences() {
        // TODO implement here
        return null;
    }

}