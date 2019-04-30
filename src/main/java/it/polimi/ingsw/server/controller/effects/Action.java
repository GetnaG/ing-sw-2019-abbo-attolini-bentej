package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Action} represents a collection of {@link EffectInterface}.
 * An Action is run as an iteration through the single effects which is composed by.
 * The words effects and steps are used with the same meaning .
 */
public class Action implements EffectInterface {
    /**
     * The name of the Action
     */
    private String name;
    /**
     * The steps composing the Action.
     */
    private List<EffectInterface> effects;
    /**
     * An index pointing to the current effect who is seen as decorator
     */
    private int currentIndex;

    /**
     * Constructs an Action composed by multiple effects.
     * @param name      name of the action
     * @param effects   list of effects which compose the action
     */
    public Action(String name, List<EffectInterface> effects) {
        this.name = name;
        this.effects = effects;
        this.currentIndex = 0;
    }
    /**
     * Constructs an Action composed by a single effect.
     * @param name      name of the action
     * @param effect    the single effect which compose the action
     */
    public Action(String name, EffectInterface effect) {
        this.name = name;
        this.effects = new ArrayList<>();
        this.effects.add(effect);
        this.currentIndex = 0;
    }
    /**
     * Default constructor
     */
    public Action( ) {
    }

    /**
     * Runs all the effects (steps) composed by the Action.
     * @param subjectPlayer     who is running the Action
     * @param board             board used in the game
     * @param alredyTargeted    list of players already targeted during the turn
     */
    public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        effects.forEach(x -> x.runEffect(subjectPlayer, board, alredyTargeted));
    }

    /**
     * Gets the name of the action.
     * @return the name of action.
     */
    public String getName() {
        return name;
    }

    /**
     * An {@link EffectInterface} can be decorated with another {@link EffectInterface}.
     * This method returns the next effect in the chain.
     * @return  the next Effect in the chain or null it doesn't exist.
     */
    public EffectInterface getDecorated() {
        if(currentIndex == effects.size()-1)
            return null;
        currentIndex += 1;
        return effects.get(currentIndex-1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToChain(EffectInterface last) {
        effects.add(last);
    }


    @Override
    public Iterator<EffectInterface> iterator() {
        return effects.listIterator();
    }
}