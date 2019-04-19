package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

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
     * @return
     */
    @Override
    public List<Damageable> runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        return null;
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
}