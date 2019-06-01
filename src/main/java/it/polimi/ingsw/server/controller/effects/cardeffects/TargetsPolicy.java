package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filters the targets based on their visibility.
 */
enum TargetsPolicy {
    /**
     * Only targets visible by the subject.
     */
    VISIBLE,
    /**
     * Only targets not visible by the subject.
     */
    NOT_VISIBLE,
    /**
     * Only targets visible by the last targeted in the chain.
     */
    VISIBLE_BY_PREVIOUS,
    /**
     * All the targets are allowed.
     */
    ALL;

    /**
     * Returns the valid targets according with this policy, the subject is
     * always excluded.
     *
     * @param subject         the player using the effect with this policy
     * @param allTargets      all the targets on the board
     * @param alreadyTargeted the targets that have already been affected in
     *                        this chain
     * @param board           the game board
     * @return all the valid targets
     */
    Set<Damageable> getValidTargets(Player subject,
                                    Set<Damageable> allTargets,
                                    List<? extends Damageable> alreadyTargeted,
                                    GameBoard board) {

        Set<Damageable> valid = getAllValidTargets(subject, allTargets,
                alreadyTargeted,
                board);
        valid.remove(subject);
        return valid;
    }

    /**
     * Returns all the valid targets, the subject could be included.
     *
     * @param subject         the player using the effect with this policy
     * @param allTargets      all the targets on the board
     * @param alreadyTargeted the targets that have already been affected in
     *                        this chain
     * @param board           the game board
     * @return all the valid targets
     */
    private Set<Damageable> getAllValidTargets(Player subject,
                                               Set<Damageable> allTargets,
                                               List<? extends Damageable> alreadyTargeted,
                                               GameBoard board) {
        switch (this) {
            case VISIBLE:
                return getVisibleTargetsBy(subject, allTargets, board);
            case NOT_VISIBLE:
                allTargets.removeAll(getVisibleTargetsBy(subject, allTargets, board));
                return allTargets;
            case VISIBLE_BY_PREVIOUS:
                return getVisibleTargetsBy(alreadyTargeted.get(alreadyTargeted.size() - 1), allTargets, board);
            case ALL:
            default:
                return allTargets;
        }
    }

    /**
     * Returns the targets visible by the provided target.
     *
     * @param target     the target from which the player must be visible
     * @param allTargets all the targets on the board
     * @param board      the game board
     * @return the targets visible by the provided target
     */
    private Set<Damageable> getVisibleTargetsBy(Damageable target,
                                                Set<? extends Damageable> allTargets,
                                                GameBoard board) {
        Set<Damageable> targets = new HashSet<>();
        for (Square s : target.getPosition().listOfVisibles(board))
            targets.addAll(board.getPlayerInSquare(s, allTargets));
        return targets;
    }
}
