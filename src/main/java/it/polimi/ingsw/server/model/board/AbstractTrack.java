package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * This class represents a common template for any Track.
 */
public interface AbstractTrack {


    /**
     * @return an int representing the skulls left
     */
    int getSkullsLeft();

    /**
     * This method is used to score the Track.
     * Must be called from outside when {@code}skullsLeft is 0.
     */
    void score();

    /**
     * Adding Token to the Track.
     *
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     */
    void addTokens(List<Player> tokens);

    /**
     * Removing the leftmost skull.
     */
    void removeSkull();

    /**
     * Gets game mode.
     */
    String getGameMode();

    List<List<Player>> getTokens();
}