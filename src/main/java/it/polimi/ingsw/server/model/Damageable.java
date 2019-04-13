package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.ScoreListener;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.List;

/**
 * An entity of the game which can be a target for weapons and power-up effects.
 * This interface models the target of the various effects that are used in
 * the game, for example the Player and the spawn points in turret mode.
 * <p>
 * Each {@code Damageable} object will be able to receive damage and
 * <i>marks</i>, to be moved and scored.
 * The methods for scoring can be used by a {@link ScoreListener}.
 *
 * @author Abbo Giulio A.
 * @see ScoreListener
 */
public interface Damageable {

    /**
     * Adds one damage by each of the shooters.
     * If a {@linkplain Player} {@code A} dealt n damage to this entity, then
     * the {@code shooters} list must contain n times the element {@code A};
     * if n is greater than the number of damage points needed to reach
     * overkill, the excess is lost.
     *
     * @param shooters the {@linkplain Player}s that dealt the damage
     * @throws NullPointerException if {@code shooters} is null
     */
    void giveDamage(List<Player> shooters);

    /**
     * Adds one <i>mark</i> by each of the shooters.
     * If a {@linkplain Player} {@code A} dealt n marks to this entity, then
     * the {@code shooters} list must contain n times the element {@code A}. The
     * target can have at most three marks by each player at any time, the
     * excess is wasted.
     *
     * @param shooters the {@linkplain Player}s that dealt the marks
     * @throws NullPointerException if {@code shooters} is null
     */
    void giveMark(List<Player> shooters);

    /**
     * Returns the {@linkplain Square} on which the entity is standing.
     *
     * @return the position of the {@code Damageable}; null if not spawned yet
     */
    Square getPosition();

    /**
     * Changes the position of this entity to the {@code newPosition}.
     * The {@code newPosition} must be a valid {@linkplain Square}.
     *
     * @param newPosition the new position of this entity
     * @throws NullPointerException if {@code newPosition} is null
     */
    void setPosition(Square newPosition);

    /**
     * Gives points to those who damaged this entity and resets the damage.
     * The amount of points given depends on the type of {@code Damageable},
     * this method also ensures that when its execution is over the entity's
     * damage has been brought back to zero and the event has been taken
     * into account for future scoring if necessary.
     */
    void scoreAndResetDamage();

    /**
     * Returns the {@linkplain Player} who dealt the kill shot damage point.
     * This information could be useful for final scoring.
     *
     * @return the {@code Player} who dealt the kill shot; null if not present
     */
    Player getKillshotPlayer();

    /**
     * Returns the {@linkplain Player} who dealt the overkill damage point.
     * This information could be useful for final scoring.
     *
     * @return the {@code Player} who dealt the overkill; null if not present
     */
    Player getOverkillPlayer();
}
