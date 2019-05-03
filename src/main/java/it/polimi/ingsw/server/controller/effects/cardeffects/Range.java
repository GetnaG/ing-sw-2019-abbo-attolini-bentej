package it.polimi.ingsw.server.controller.effects.cardeffects;

/**
 * This class represents a range of values.
 */
class Range {
    /**
     * The minimum allowed.
     */
    private int min;
    /**
     * The maximum allowed.
     */
    private int max;

    /**
     * Creates a range with the provided bounds.
     *
     * @param min the minimum allowed
     * @param max the maximum allowed
     */
    Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    int getMin() {
        return min;
    }

    int getMax() {
        return max;
    }

    boolean hasMinimum() {
        return min > -1;
    }

    boolean hasMaximum() {
        return max > -1;
    }

    boolean isSingleValue() {
        return min == 1 && max == 1;
    }
}
