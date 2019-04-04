package it.polimi.ingsw.server.model;
import java.util.List;

/**
 * 
 */
public abstract class AbstractTrack {

    /**
     * Default constructor
     */
    public AbstractTrack() {
    }


    /**
     * @return
     */
    public int getSkullsLeft() {
        // TODO implement here
        return 0;
    }

    /**
     * 
     */
    public void score() {
        // TODO implement here
    }

    /**
     * @param tokens
     */
    public void addTokens(List<Player> tokens) {
        // TODO implement here
    }

    /**
     * 
     */
    public void removeSkull() {
        // TODO implement here
    }

}