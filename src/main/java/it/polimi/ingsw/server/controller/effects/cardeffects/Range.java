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

    /**
     * Return the minimum value of this.
     *
     * @return the minimum value
     */
    int getMin() {
        return min;
    }

    /**
     * Return the maximum value of this.
     *
     * @return the maximum value
     */
    int getMax() {
        return max;
    }

    /**
     * Whether this has a valid minimum value.
     *
     * @return true if this has a minimum
     */
    boolean hasMinimum() {
        return min > -1;
    }

    /**
     * Whether this has a valid maximum value.
     *
     * @return true if this has a maximum
     */
    boolean hasMaximum() {
        return max > -1;
    }

    /**
     * Returns true if this range allows only one element.
     *
     * @return true if this range allows only one element
     */
    boolean isSingleValue() {
        return min == 1 && max == 1;
    }
}
