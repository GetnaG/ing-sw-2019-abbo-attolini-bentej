package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters the availableTargets based on their visibility.
 */
enum TargetsPolicy {
    /**
     * Only availableTargets visible by the subject.
     */
    VISIBLE,
    /**
     * Only availableTargets not visible by the subject.
     */
    NOT_VISIBLE,
    /**
     * Only availableTargets visible by the last targeted in the chain.
     */
    VISIBLE_BY_PREVIOUS,
    /**
     * All the availableTargets are allowed.
     */
    ALL;

    List<Damageable> getDamageable(Player subject,
                                   List<Damageable> allTargets,
                                   Damageable lastTargeted,
                                   GameBoard board) {
        switch (this) {
            case VISIBLE:
                return getVisibleTargetsBy(subject, allTargets, board);
            case NOT_VISIBLE:
                return getNotVisibleTargets(subject, allTargets, board);
            case VISIBLE_BY_PREVIOUS:
                return getVisibleTargetsBy(lastTargeted, allTargets, board);
            case ALL:
            default:
                return allTargets;
        }
    }

    private List<Damageable> getNotVisibleTargets(Damageable player,
                                                  List<Damageable> allTargets,
                                                  GameBoard board) {
        allTargets.removeAll(getVisibleTargetsBy(player, allTargets, board));
        return allTargets;
    }

    private List<Damageable> getVisibleTargetsBy(Damageable player,
                                                 List<Damageable> allTargets,
                                                 GameBoard board) {
        List<Damageable> targets = new ArrayList<>();
        for (Square s : player.getPosition().listOfVisibles(board))
            targets.addAll(board.getPlayerInSquare(s, allTargets));
        return targets;
    }
}
