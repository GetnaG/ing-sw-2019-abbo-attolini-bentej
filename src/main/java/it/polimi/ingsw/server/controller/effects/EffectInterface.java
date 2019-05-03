package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * This class represents an effect of a card or an action.
 * <p>
 * An effect has a name and a reference to a following one if necessary,
 * this way a chain of effects can be built; this chain can be iterated with
 * the provided {@link EffectIterator}.
 * The effect can be run with the provided method.
 *
 * @author Abbo Giulio A.
 * @see EffectIterator
 */
public interface EffectInterface extends Iterable<EffectInterface> {

    /**
     * Runs this effect for the specified player.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     all the targets on the board
     * @param board          the game board (for applying the effect)
     * @param allTargeted    a list of elements already targeted by this chain
     * @param damageTargeted a list of the elements that have received damage
     */
    void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board,
                   List<Damageable> allTargeted, List<Damageable> damageTargeted);

    /**
     * Returns the name of this effect.
     *
     * @return the name of this effect
     */
    String getName();

    /**
     * Returns the next effect in the chain.
     * An iterator should be used to access the effects in the chain instead
     * of this method.
     *
     * @return the next effect in the chain, null if there is not one
     */
    EffectInterface getDecorated();

    /**
     * Sets the provided effect as the last effect in the chain.
     *
     * @param last the effect to be added
     */
    void addToChain(EffectInterface last);
}