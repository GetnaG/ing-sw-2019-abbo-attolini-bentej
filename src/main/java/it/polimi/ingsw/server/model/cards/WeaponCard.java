package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;

import java.util.List;

/**
 * 
 */
public class WeaponCard extends AbstractCard {
    private String resName;

    public WeaponCard(String resourceId, List<AmmoCube> cost) {
        super(resourceId);
        this.cost = cost;
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


    public List<AmmoCube> getCost() {
        return cost;
    }
}