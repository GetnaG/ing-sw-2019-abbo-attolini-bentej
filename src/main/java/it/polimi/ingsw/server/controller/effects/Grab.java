package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * 
 */
public class Grab implements EffectInterface {

    /**
     * Default constructor
     */
    public Grab() {
    }

    /**
     * 
     */
    private void getGrabbable() {
        // TODO implement here
    }

    /**
     * 
     */
    private void applyGrabbed() {
        // TODO implement here
    }

    /**
     * 
     */
    private void discardWeapon() {
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