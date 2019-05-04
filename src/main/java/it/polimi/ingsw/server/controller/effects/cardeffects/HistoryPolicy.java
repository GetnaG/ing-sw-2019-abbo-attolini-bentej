package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.Damageable;

import java.util.List;

/**
 * Filters the targets based on the previous targets in the chain.
 */
enum HistoryPolicy {
    /**
     * Only targets that have already been affected in this chain.
     */
    ONLY_TARGETED,
    /**
     * Only targets that have not already been affected in this chain.
     */
    NOT_TARGETED,
    /**
     * Only targets that have received a damage token in this chain.
     */
    ONLY_DAMAGED,
    /**
     * All the targets are valid.
     */
    ALL;

    /**
     * Checks if the provided target is acceptable according tho this policy.
     *
     * @param damageable      the target to be evaluated
     * @param alreadyTargeted the targets that have already been affected in
     *                        this chain
     * @param alreadyDamaged  the targets that have already been damaged in
     *                        this chain
     * @return true if the provided target is acceptable
     */
    boolean filterTarget(Damageable damageable,
                         List<Damageable> alreadyTargeted,
                         List<Damageable> alreadyDamaged) {
        switch (this) {
            case NOT_TARGETED:
                return !alreadyTargeted.contains(damageable);
            case ONLY_TARGETED:
                return alreadyTargeted.contains(damageable);
            case ONLY_DAMAGED:
                return alreadyDamaged.contains(damageable);
            case ALL:
            default:
                return true;
        }
    }
}
