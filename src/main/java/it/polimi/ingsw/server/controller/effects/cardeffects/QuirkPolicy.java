package it.polimi.ingsw.server.controller.effects.cardeffects;

/**
 * Quirks specify a particular behaviour of the effect.
 */
enum QuirkPolicy {
    /**
     * The targets must be all on different squares.
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
     * this chain are not valid targets.
     */
    MAX_TWO_HITS,
    /**
     * Can be used with secondary damage and marks to hit all the players
     * in a room instead of a square, does not include the subject's room.
     */
    ROOM,
    /**
     * Restricts movement to a single cardinal direction.
     * This can be used with zero to two targets.
     */
    SINGLE_DIRECTION;

    /**
     * Checks whether this is in the provided array.
     *
     * @param quirks the array to be checked
     * @return true if this instance is in the array
     */
    boolean isIn(QuirkPolicy[] quirks) {
        for (QuirkPolicy p : quirks)
            if (p == this)
                return true;
        return false;
    }
}
