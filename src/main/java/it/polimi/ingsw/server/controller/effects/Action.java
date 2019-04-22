package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Action implements EffectInterface {

    private String name;
    private List<EffectInterface> effects;

    /**
     * Default constructor
     */
    public Action(String name, List<EffectInterface> effects) {
        this.name = name;
        this.effects = effects;
    }
    /**
     * Default constructor
     */
    public Action(String name, EffectInterface effect) {
        this.name = name;
        this.effects = new ArrayList<>();
        this.effects.add(effect);
    }
    /**
     * Default constructor
     */
    public Action( ) {
    }

    /**
     * @param subjectPlayer
     * @param board
     * @param alredyTargeted
     */
    public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        // TODO implement here
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public EffectInterface getDecorated() {
        return effects.get(0);
    }

    @Override
    public Iterator<EffectInterface> iterator() {
        return null;
    }
}