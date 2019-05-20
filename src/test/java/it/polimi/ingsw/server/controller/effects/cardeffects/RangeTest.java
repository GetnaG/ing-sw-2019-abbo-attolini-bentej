package it.polimi.ingsw.server.controller.effects.cardeffects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: normal usage as in the jsons.
 */
class RangeTest {

    @Test
    void getMin() {
        Range range = new Range(18, 49);

        assertEquals(18, range.getMin());
        assertTrue(range.hasMinimum());
    }

    @Test
    void getMax() {
        Range range = new Range(6, 234);

        assertEquals(234, range.getMax());
        assertTrue(range.hasMaximum());
    }

    @Test
    void hasMinimum() {
        Range range = new Range(56, 100);
        assertTrue(range.hasMinimum());

        range = new Range(-1, 100);
        assertFalse(range.hasMinimum());
    }

    @Test
    void hasMaximum() {
        Range range = new Range(89, 110);
        assertTrue(range.hasMaximum());

        range = new Range(-1, -1);
        assertFalse(range.hasMinimum());
    }

    @Test
    void isSingleValue() {
        Range range = new Range(89, 110);
        assertFalse(range.isSingleValue());

        range = new Range(-1, 110);
        assertFalse(range.isSingleValue());

        range = new Range(89, -1);
        assertFalse(range.isSingleValue());

        range = new Range(-1, -1);
        assertFalse(range.isSingleValue());

        range = new Range(1, 1);
        assertTrue(range.isSingleValue());
    }
}