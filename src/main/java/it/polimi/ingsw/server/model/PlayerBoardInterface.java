
import java.util.*;

/**
 * 
 */
public interface PlayerBoardInterface {

    /**
     * 
     */
    private List<Player> damage;



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