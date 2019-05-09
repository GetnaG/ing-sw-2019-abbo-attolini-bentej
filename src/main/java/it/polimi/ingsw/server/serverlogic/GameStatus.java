package it.polimi.ingsw.server.serverlogic;

/**
 * Represents the status of a Game. Used in {@linkplain ServerHall}.
 *
 * @author Fahed B. Tej
 */
public enum GameStatus {
    /**
     * Game is not started
     */
    NOTSTARTED,
    /**
     * Game is has started and it's waiting for at least 3 users to start the timer
     */
    STARTING,
    /**
     * Game is waiting for the timer to expire so it can start the game
     */
    STARTINGTIMER
}
