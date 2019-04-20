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
     */
    public void startTurn(Player currentPlayer, GameBoard board);

}