package it.polimi.ingsw.server.controller.effects;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;

/**
 * 
 */
public class Shoot implements EffectInterface {

    /**
     * Default constructor
     */
    public Shoot() {
    }

    /**
     * 
     */
    private void chooseWeapon() {
        // TODO implement here
    }

    /**
     * 
     */
    private void getEffectsAndFilter() {
        // TODO implement here
    }

    /**
     * 
     */
    private void chooseAndApplyEffects() {
        // TODO implement here
    }

    /**
     * 
     */
    private void useTargetingScope() {
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

}