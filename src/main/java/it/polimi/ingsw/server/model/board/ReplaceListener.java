package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

/**
 * Implementing classes will handle the replacing of the cards on the board.
 * When a player takes a card from the board (a weapon or an ammo card) it
 * needs to be replaced.
 * <p>
 * When a card is taken from a square on the board, the classes that
 * implement this interface are notified with {@code addTurretSquare} or
 * {@code addSpawnSquare}.
 * The replacing is triggered with {@code replaceAll}.
 *
 * @author giubots
 * @see Square
 * @see WeaponCard
 * @see AmmoCard
 */
public interface ReplaceListener {
    /**
     * Adds a {@linkplain Square} to the squares that need a new card.
     *
     * @param toBeReplaced the square without a {@code AmmoCard}
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
     * Replaces all the missing cards on the squares.
     * This has effect on the squares added since the last time this has been
     * called.
     */
    void replaceAll();
}
