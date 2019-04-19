package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class CardEffect implements EffectInterface {

    /**
     * Default constructor
     */
    public CardEffect() {
    }

    /**
     * 
     */
    private int damageQty;

    /**
     * 
     */
    private int marksQty;

    /**
     * 
     */
    private boolean allowSameTarget;


    /**
     * @return
     */
    public List<AmmoCube> getAddictionalCost() {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    private void checkIfTargetCanUseTagBack() {
        // TODO implement here
    }

    /**
     * @param subjectPlayer 
     * @param board 
     * @param alredyTargeted 
     * @return
     */
    public List<Damageable> runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String getName() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public EffectInterface getDecorated() {
        // TODO implement here
        return null;
    }

    public List<AmmoCube> getCost() {
        //TODO implement cost
        return new ArrayList<>();
    }

}