package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.AbstractCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.List;

/**
 * A player can grab any Grabbable object. A Grabbable object can be an AmmoCard
 * or a Turret.
 *
 * @author Fahed Ben Tej
 * @see it.polimi.ingsw.server.model.cards.AmmoCard
 * @see it.polimi.ingsw.server.model.board.Turret
 */
public class Grab implements EffectInterface {


    /**
     * Gets the grabbed object
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
     */
    public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        
    }

    /**
     * Return the name name of the Effect: "Grab"
     * @return
     */
    public String getName() {
        return "Grab";
    }

    /**
     * //FIXME
     * @return
     */
    public EffectInterface getDecorated() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToChain(EffectInterface last) {

    }

    /**
     * FIXME
     * @return
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return null;
    }
}