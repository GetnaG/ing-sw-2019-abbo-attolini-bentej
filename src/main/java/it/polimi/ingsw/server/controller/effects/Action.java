package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link Action} represents a collection of {@link EffectInterface}.
 * An Action is run as an iteration through the single effects which is composed by.
 * The words effects and steps are used with the same meaning.
 * An action can also have a cost, this is the sum of the costs of the single
 * steps.
 *
 * @author Fahed Ben Tej
 * @author giubots
 */
public class Action implements Iterable<EffectInterface> {
    /**
     * The name of the Action
     */
    private String name;
    /**
     * The steps composing the Action.
     */
    private List<EffectInterface> effects;
    /**
     * The cost of this action, sum of the costs of the effects.
     */
    private List<AmmoCube> totalCost;

    /**
     * Constructs an Action composed by multiple effects.
     * The costs of the single effects is summed in {@linkplain #totalCost}.
     *
     * @param name    name of the action
     * @param effects list of effects that compose the action
     */
    public Action(String name, List<EffectInterface> effects) {
        this.name = name;
        this.effects = effects;
        totalCost = effects.stream().flatMap(effectInterface ->
                effectInterface.getCost().stream()).collect(Collectors.toList());
    }

    /**
     * Constructs an Action composed by a single effect.
     * The costs of the single effects is summed in {@linkplain #totalCost}.
     *
     * @param name   name of the action
     * @param effect the single effect that composes the action
     */
    public Action(String name, EffectInterface effect) {
        this(name, Collections.singletonList(effect));
    }

    /**
     * Runs all the effects (steps) in this action.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     all the targets on the board
     * @param board          the game board (for applying the effect)
     * @param allTargeted    a list of elements already targeted by this chain
     * @param damageTargeted a list of the elements that have received damage
     * @throws ToClientException if the execution is interrupted because the
     *                           player is suspended
     * @see EffectInterface#runEffect(Player, List, GameBoard, List, List)
     */
    void runAll(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> allTargeted, List<Damageable> damageTargeted) throws ToClientException {
        for (EffectInterface x : effects) {
            x.runEffect(subjectPlayer, allTargets, board, allTargeted, damageTargeted);
        }
    }

    /**
     * Gets the name of this action.
     *
     * @return the name of this action
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the cost of this action.
     * The cost of an Action is the sum of the costs of the composing
     * {@linkplain EffectInterface}s.
     *
     * @return the cost of this action
     */
    List<AmmoCube> getTotalCost() {
        return totalCost;
    }

    /**
     * Returns an iterator over the steps in this action.
     *
     * @return an iterator over the steps in this action
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return effects.iterator();
    }
}