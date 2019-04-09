package it.polimi.ingsw.server.model.player;
import java.util.*;

/**
 * 
 */
public interface PlayerBoardInterface {

    /**
     * 
     */
    //TODO ugly workaround
    public List<Player> damage = null;



    /**
     * @return
     */
    public boolean isAdr1Unlocked();

    /**
     * @return
     */
    public boolean isAdr2Unlocked();

    /**
     * @return
     */
    public boolean isDead();

    /**
     * @return
     */
    public int getScoreAndAddSkull();

}