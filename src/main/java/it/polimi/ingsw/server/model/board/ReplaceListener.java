package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.List;

/**
 * Implementing classes will handle the replacing of the cards on the board.
 * When a player takes a card from the board (a weapon or an ammo card) it
 * needs to be replaced.
 * <p>
 * When a card is taken from a square on the board, the classes that
 * implement this interface are notified with {@code addTurretSquare} or
 * {@code addSpawnSquare}. When a player puts one of his weapons back to the
 * weapon shop {@code replaceDiscardedWeapons} is called.
 * The replacing is triggered with {@code replaceAll}.
 *
 * @author Abbo Giulio A.
 * @see Square
 * @see WeaponCard
 * @see AmmoCard
 */
public interface ReplaceListener {
    /**
     * Adds a {@linkplain TurretSquare} to the squares that need a new card.
     *
     * @param toBeReplaced the {@code TurretSquare} without a {@code AmmoCard}
     * @throws NullPointerException if {@code toBeReplaced} is null
     */
    void addSquare(Square toBeReplaced);

    /**
     * Adds a {@linkplain SpawnSquare} to the squares that need a new card.
     *
     * @param toBeReplaced the {@code SpawnSquare} without some {@code
     *                     WeaponCard}s
     * @throws NullPointerException if {@code toBeReplaced} is null
     */
    void addSpawnSquare(SpawnSquare toBeReplaced);

    /**
     * Puts some weapons back to the shop in the {@code location}.
     * A player can not have more than three weapons at one time, when he
     * chooses to take one more, he has to put one back to the shop it took
     * it from.
     *
     * @param location the location of the shop
     * @param weapons  the {@linkplain List} of weapons to put back
     * @throws NullPointerException     if a parameter is null
     * @throws IllegalArgumentException if location does not have a weapon shop
     */
    void replaceDiscardedWeapons(Square location, List<WeaponCard> weapons);

    /**
     * Replaces all the missing cards on the squares.
     * This has effect on the squares added since the last time this has been
     * called.
     */
    void replaceAll();
}
