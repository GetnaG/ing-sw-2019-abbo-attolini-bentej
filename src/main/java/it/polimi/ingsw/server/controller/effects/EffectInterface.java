package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * This class represents an effect of a card or a step.
 * <p>
 * An effect has a name and a cost that must be payed to run the effect.
 * The effect can be run with the provided method.
 *
 * @author giubots
 */
public interface EffectInterface {

    /**
     * Runs this effect for the specified player.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     all the targets on the board
     * @param board          the game board (for applying the effect)
     * @param allTargeted    a list of elements already targeted by this chain
     * @param damageTargeted a list of the elements that have received damage
     * @throws ToClientException if the execution is interrupted because the
     *                           player is suspended
     */
    void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board,
                   List<Damageable> allTargeted, List<Damageable> damageTargeted)
            throws ToClientException;

    /**
     * Returns the name of this effect.
     *
     * @return the name of this effect
     */
    String getName();

    /**
     * Returns the cost of this effect.
     *
     * @return the cost of this effect
     */
    List<AmmoCube> getCost();
}