package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

/**
 * 
 */
public class NormalBotTurn extends NormalTurn {

    /**
     * Default constructor
     */
    public NormalBotTurn(Player player, GameBoard board) {
        super(player,board);
    }

    public void failingMethod(){

    }
}