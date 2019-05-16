package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

/**
 * Defines the behaviour of a turn.
 *
 * @author Fahed Ben Tej
 */
public interface TurnInterface {

    /**
     * Starts player's turn.
     * @param currentPlayer the player who's turn is starting.
     * @param board GameBoard of the game.
     *
     * @return -1 if Final Frenzy is triggered, else 0.
     */
    public int startTurn(Player currentPlayer, GameBoard board);

}