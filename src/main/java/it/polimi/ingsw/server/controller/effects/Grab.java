package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.AmmoCard;
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
     * Runs Grab effect for the specified player.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     all the targets on the board
     * @param board          the game board (for applying the effect)
     * @param allTargeted    a list of elements already targeted by this chain
     * @param damageTargeted a list of the elements that have received damage
     */
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> allTargeted, List<Damageable> damageTargeted){
        // We can grab an AmmoCard
        if(subjectPlayer.getPosition().getAmmoCard() != null){
            AmmoCard card = subjectPlayer.getPosition().getAmmoCard();
            // get the cubes
            // If the tile depicts a powerup card, draw one
        }
        // We can grab a Weapon

        if(subjectPlayer.getPosition().getTurret() != null){
            // we can grab a Turret
        }
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