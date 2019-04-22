package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class Move implements EffectInterface {

    /**
     * Default constructor
     */
    public Move() {
    }

    /**
     * @param subjectPlayer
     * @param board
     * @param alredyTargeted
     */
    @Override
    public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public EffectInterface getDecorated() {
        return null;
    }


    @Override
    public Iterator<EffectInterface> iterator() {
        return null;
    }
}