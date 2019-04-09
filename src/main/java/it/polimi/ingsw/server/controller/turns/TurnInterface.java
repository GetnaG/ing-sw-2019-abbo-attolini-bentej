package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

/**
 * 
 */
public interface TurnInterface {

    /**
     * @param currentPlayer 
     * @param board GameBoard
     */
    public void startTurn(Player currentPlayer, GameBoard board);

}