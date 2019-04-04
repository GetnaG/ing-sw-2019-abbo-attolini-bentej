package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;

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