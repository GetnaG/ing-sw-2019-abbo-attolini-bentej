package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filters the squares and defines the movement.
 */
enum SquaresPolicy {
    /**
     * Only squares visible by the subject.
     */
    VISIBLE,
    /**
     * Moves the targets to the subject.
     */
    TO_SUBJECT,
    /**
     * Moves the targets to the last target in the chain.
     */
    TO_PREVIOUS,
    /**
     * All squares are allowed.
     */
    ALL,
    /**
     * Only squares visible by the subject except the subject's.
     */
    VISIBLE_NOT_SELF,
    /**
     * All the squares in the cardinal directions from the player's
     * square are allowed, including his own.
     */
    SUBJECT_CARDINALS,
    /**
     * The squares are not relevant in this effect.
     */
    NONE;

    /**
     * Returns all the valid squares according to this policy.
     *
     * @param subject         the player using the effect with this policy
     * @param board           the game board of this game
     * @param alreadyTargeted the target that have already been affected in
     *                        this chain of effects
     * @return all the valid squares, null if squares are not relevant
     * @throws AgainstRulesException if the destinations can not be calculated
     */
    Set<Square> getValidDestinations(Player subject, GameBoard board,
                                     List<? extends Damageable> alreadyTargeted) throws AgainstRulesException {
        switch (this) {
            case VISIBLE:
                return new HashSet<>(subject.getPosition().listOfVisibles(board));
            case VISIBLE_NOT_SELF:
                Set<Square> visible = new HashSet<>(
                        subject.getPosition().listOfVisibles(board));
                visible.remove(subject.getPosition());
                return visible;
            case TO_PREVIOUS:
                try {
                    return new HashSet<>(Collections.singletonList(alreadyTargeted.get(alreadyTargeted.size() - 1).getPosition()));
                } catch (Exception e) {
                    throw new AgainstRulesException("No previous players");
                }
            case SUBJECT_CARDINALS:
                return new HashSet<>(subject.getPosition().getCardinals());
            case ALL:
                return new HashSet<>(board.getAllSquares());
            case TO_SUBJECT:
            case NONE:
            default:
                return null;
        }
    }

    /**
     * Applies the right movement to the provided target.
     *
     * @param subject     the player using the effect with this policy
     * @param target      the target to be affected
     * @param destination a set containing the chosen destination
     */
    void apply(Player subject, Damageable target, Set<? extends Square> destination) {
        switch (this) {
            case TO_SUBJECT:
                target.setPosition(subject.getPosition());
                return;
            case ALL:
            case VISIBLE:
            case VISIBLE_NOT_SELF:
            case TO_PREVIOUS:
            case SUBJECT_CARDINALS:
            case NONE:
                if (destination != null && !destination.isEmpty())
                    target.setPosition(destination.iterator().next());
                return;
            default:
        }
    }
}
