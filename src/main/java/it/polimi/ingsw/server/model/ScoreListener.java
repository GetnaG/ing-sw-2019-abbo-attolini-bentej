package it.polimi.ingsw.server.model;

import java.util.List;

/**
 * Classes implementing this will handle scoring for players and the board.
 * Scoring is done on objects that implement the {@link Damageable} interface or
 * on a {@link GameBoard} object, this last one should be provided to the
 * implementing class by its constructor.
 * <p>
 * The {@code Damageable} objects are provided through the {@code addKilled}
 * method and scored when {@code scoreAllKilled} is called. Scoring consists in
 * moving the <i>kill shot</i> and <i>overkill</i> tokens to the {@code
 * GameBoard} and giving points to the players, this second part is left to the
 * {@code Damageable} object because its implementation varies during the game.
 *
 * @author Abbo Giulio A.
 * @see Damageable
 * @see GameBoard
 */
public interface ScoreListener {

    /**
     * Adds a {@linkplain Damageable} object to the objects that will be
     * scored.
     *
     * @param killed the {@code Damageable} to be scored
     * @throws NullPointerException if {@code killed} is null
     */
    void addKilled(Damageable killed);

    /**
     * Scores all the {@linkplain Damageable} objects added. This method must
     * also take care of adding the <i>kill shot</i> and
     * <i>overkill</i> tokens (if applicable) to the {@code GameBoard}.
     * This affects all the objects added since the last call to {@code
     * emptyKilledList}.
     */
    void scoreAllKilled();

    /**
     * This calls {@code scoreBoard} on the {@linkplain GameBoard}. It is used
     * at the end of the game.
     */
    void scoreBoard();

    /**
     * Returns a {@linkplain List} of the objects that will be scored. These are
     * the {@linkplain Damageable} added since the last call to {@code
     * emptyKilledList}.
     *
     * @return a list of the objects that will be scored
     */
    List<Damageable> getKilled();

    /**
     * Clears the list of objects on which scoring will be performed. Invoking
     * {@code scoreAllKilled} will effect only the objects added after the last
     * call to this method. A call to {@code scoreAllKilled} immediately after
     * this method will produce no effect.
     */
    void emptyKilledList();
}
