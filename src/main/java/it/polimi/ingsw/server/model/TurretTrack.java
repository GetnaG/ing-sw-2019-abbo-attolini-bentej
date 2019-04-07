package it.polimi.ingsw.server.model;
import java.util.*;

/**
 *
 */
public class TurretTrack extends AbstractTrack {

    /**
     * Default constructor
     */
    public TurretTrack() {
    }

    /**
     * @return an int representing the skulls left
     */
    @Override
    public int getSkullsLeft() {
        return 0;
    }

    /**
     * @return void
     */
    @Override
    public void score() {

    }

    /**
     * Adding Token to the Track.
     *
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     *
     * @return
     */
    @Override
    public void addTokens(List<Player> tokens) {

    }

    /**
     * Removing the leftmost skull.
     */
    @Override
    public void removeSkull() {

    }

    /**
     *
     */
    private AmmoCard[] cards;

}