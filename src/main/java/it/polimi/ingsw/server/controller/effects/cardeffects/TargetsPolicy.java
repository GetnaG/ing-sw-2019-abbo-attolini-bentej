package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.AgainstRulesException;
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
     * Only targets visible by the last targeted in the chain and not him.
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
     * @throws AgainstRulesException if the valid targets can not be calculated
     */
    Set<Damageable> getValidTargets(Player subject,
                                    Set<? extends Damageable> allTargets,
                                    List<? extends Damageable> alreadyTargeted,
                                    GameBoard board) throws AgainstRulesException {

        Set<Damageable> valid = getAllValidTargets(subject, new HashSet<>(allTargets),
                alreadyTargeted, board);
        valid.remove(subject);
        return valid;
    }

    /**
     * Returns all the valid targets, the subject could be included.
     * The provided {@code allTargets} could be modified.
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
                                               GameBoard board) throws AgainstRulesException {
        switch (this) {
            case VISIBLE:
                return getVisibleTargetsBy(subject, allTargets, board);
            case NOT_VISIBLE:
                allTargets.removeAll(getVisibleTargetsBy(subject, allTargets, board));
                return allTargets;
            case VISIBLE_BY_PREVIOUS:
                try {
                    Damageable last = alreadyTargeted.get(alreadyTargeted.size() - 1);
                    Set<Damageable> toReturn = getVisibleTargetsBy(last, allTargets, board);
                    toReturn.remove(last);
                    return toReturn;
                } catch (Exception e) {
                    throw new AgainstRulesException("no previous damaged");
                }
            case ALL:
            default:
                return allTargets;
        }
    }

    /**
     * Returns the targets visible by the provided damageable.
     *
     * @param subject    targets will be visible by this
     * @param allTargets all the targets on the board
     * @param board      the game board
     * @return the targets visible by the provided damageable
     */
    private Set<Damageable> getVisibleTargetsBy(Damageable subject,
                                                Set<? extends Damageable> allTargets,
                                                GameBoard board) {
        Set<Damageable> targets = new HashSet<>();
        for (Square s : subject.getPosition().listOfVisibles(board))
            targets.addAll(board.getPlayerInSquare(s, allTargets));
        return targets;
    }
}
