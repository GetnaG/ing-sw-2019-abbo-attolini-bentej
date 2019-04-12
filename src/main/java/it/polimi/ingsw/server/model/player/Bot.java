package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.ToClientInterface;
import it.polimi.ingsw.server.controller.ScoreListener;

/**
 * 
 */
public class Bot extends Player {


    /**
     * 
     */
    private PlayerBoardInterface botBoard;

    public Bot(String nickname) {
        super(nickname);
    }
}