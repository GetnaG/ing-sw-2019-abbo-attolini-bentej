package it.polimi.ingsw.server.controller.effects.cardeffects;

/**
 * Quirks specify a particular behaviour of the effect.
 */
enum QuirkPolicy {
    /**
     * The availableTargets must be all on different squares.
     */
    DIFFERENT_SQUARES,
    /**
     * The wall must be ignored when counting moves.
     */
    IGNORE_WALLS,
    /**
     * The subject is moved to the furthest target affected.
     */
    MOVE_TO_TARGET,
    /**
     * Targets which have already been targeted at least two times in
     * this chain are not valid availableTargets.
     */
    MAX_TWO_HITS,
    /**
     * Can be used with secondary damage and marks to hit all the players
     * in a room instead of a square.
     */
    ROOM,
    /**
     * Restricts movement to a single cardinal direction.
     * This can be used with zero to two availableTargets.
     */
    SINGLE_DIRECTION;

    boolean isIn(QuirkPolicy[] quirks) {
        for (QuirkPolicy p : quirks)
            if (p == this)
                return true;
        return false;
    }
}
