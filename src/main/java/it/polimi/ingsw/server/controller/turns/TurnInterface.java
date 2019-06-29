package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * Defines the behaviour of a turn.
 *
 * @author Fahed Ben Tej
 */
public interface TurnInterface {
    /**
     * Starts the player's turn.
     *
     * @param subjectPlayer the player who's turn is starting
     * @param allTargets all the targets on the board
     * @param board GameBoard of the game
     */
    void startTurn(Player subjectPlayer, List<Damageable> allTargets, GameBoard board);

}