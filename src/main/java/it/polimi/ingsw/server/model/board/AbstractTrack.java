package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * This class represents a common template for any Track.
 */
public abstract class AbstractTrack {


    /**
     * @param
     * @return an int representing the skulls left
     */
    public abstract int getSkullsLeft() ;

    /**
     * This method is used to score the Killshot Track.
     * Must be called from outside when {@code}skullsLeft is 0.
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