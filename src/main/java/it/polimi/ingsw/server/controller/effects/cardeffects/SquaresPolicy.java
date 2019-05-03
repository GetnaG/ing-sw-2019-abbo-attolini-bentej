package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Filters the squares and defines the movement.
 */
enum SquaresPolicy {
    /**
     * Only squares visible by the subject.
     */
    VISIBLE,
    /**
     * Moves the availableTargets to the subject.
     */
    TO_SUBJECT,
    /**
     * Moves the availableTargets to the last target in the chain.
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

    List<Square> getDestinations(Player subject, GameBoard board,
                                 Damageable lastTargeted) {
        switch (this) {
            case VISIBLE:
                return subject.getPosition().listOfVisibles(board);
            case VISIBLE_NOT_SELF:
                List<Square> visible =
                        subject.getPosition().listOfVisibles(board);
                visible.remove(subject.getPosition());
                return visible;
            case TO_PREVIOUS:
                return Arrays.asList(lastTargeted.getPosition());
            case SUBJECT_CARDINALS:
                return subject.getPosition().getCardinals();
            case ALL:
                return new ArrayList<>(board.getAllSquares());
            case TO_SUBJECT:
            case NONE:
            default:
                return null;
        }
    }

    void apply(Player subject, Damageable target, List<Square> destination) {
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
                    target.setPosition(destination.get(0));
                return;
            default:
        }
    }
}
