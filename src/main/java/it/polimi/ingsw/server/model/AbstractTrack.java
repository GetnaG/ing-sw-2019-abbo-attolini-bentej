package it.polimi.ingsw.server.model;
import java.util.List;

/**
 * This class represents a common template for any Track.
 */
public abstract class AbstractTrack {

    /**
     * Default constructor
     */
    public AbstractTrack() {
    }


    /**
     * @param
     * @return an int representing the skulls left
     */
    public abstract int getSkullsLeft() ;

    /**
     * This method is used to score the Killshot Track.
     * @param
     *
     * @return void
     */
    public abstract void score() ;

    /**
     * Adding Token to the Track.
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     * @return
     */
    public abstract void addTokens(List<Player> tokens);

    /**
     * Removing the leftmost skull.
     */
    public abstract void removeSkull() ;

}