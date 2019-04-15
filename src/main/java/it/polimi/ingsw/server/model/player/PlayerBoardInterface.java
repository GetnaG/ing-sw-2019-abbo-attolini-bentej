package it.polimi.ingsw.server.model.player;

import java.util.List;

/**
 * This class handles damage and marks for the {@link Player}.
 * This keeps track of the damage, marks, skulls, and scores the players
 * accordingly.
 *
 * @author Abbo Giulio A.
 * @see Player
 */
public interface PlayerBoardInterface {

    /**
     * Checks if the first adrenaline action is unlocked.
     * This depends on the amount of damage added since last
     * {@linkplain #resetDamage()}.
     *
     * @return true if the first adrenaline action is available
     */
    boolean isAdr1Unlocked();

    /**
     * Checks if the second adrenaline action is unlocked.
     * This depends on the amount of damage added since last
     * {@linkplain #resetDamage()}.
     *
     * @return true if the second adrenaline action is available
     */
    boolean isAdr2Unlocked();

    /**
     * Checks if the player is dead.
     * A player is dead after he has received damage that counts as a kill shot.
     *
     * @return true if the player is dead
     */
    boolean isDead();

    /**
     * Adds one damage point for each element of the {@code shooters} list.
     * Damage exceeding overkill is discarded.
     *
     * @param shooters a {@linkplain List} containing those who are dealing the
     *                 damage
     * @throws NullPointerException if {@code shooters} is null
     */
    void addDamage(List<Player> shooters);

    /**
     * Adds one mark point for each elements of the {@code shooters} list.
     * Marks that are not applicable are wasted.
     *
     * @param shooters a {@linkplain List} containing those who are dealing
     *                 the marks
     * @throws NullPointerException if {@code shooters} is null
     */
    void addMarks(List<Player> shooters);

    /**
     * Calculates the score based on the number of damage points registered.
     * For each player who has dealt damage since last
     * {@linkplain #resetDamage()}, this
     * calculates how many points he has done and adds them to his score.
     */
    void score();

    /**
     * Adds a skull to the player board: next scoring will be less valuable.
     */
    void addSkull();

    /**
     * Brings the damage back to zero.
     * If there are any adrenaline actions unlocked, they get locked again.
     */
    void resetDamage();

    /**
     * Returns the {@linkplain Player} who dealt the kill shot damage.
     * This can be useful for final scoring.
     *
     * @return the {@linkplain Player} who dealt the kill shot damage or null
     * if not applicable
     */
    Player getKillshot();

    /**
     * Returns the {@linkplain Player} who dealt the over kill damage.
     * This can be useful for final scoring and for revenge marks.
     *
     * @return the {@linkplain Player} who dealt the kill shot damage or null
     * if not applicable
     */
    Player getOverkill();

    /**
     * Returns the marks that this boars has.
     * Used when changing board.
     *
     * @return the marks this board has.
     */
    List<Player> getMarks();
}
