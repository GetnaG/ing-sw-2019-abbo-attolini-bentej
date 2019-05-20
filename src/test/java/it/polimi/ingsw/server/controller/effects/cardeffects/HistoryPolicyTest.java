package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Author: Abbo Giulio A.
 * Testing: correct behaviour for each value.
 */
class HistoryPolicyTest {
    private Player damageable;
    private List<Damageable> targeted;
    private List<Damageable> damaged;

    @BeforeEach
    void setUp() {
        damageable = new Player("subject");
        targeted = new ArrayList<>(Arrays.asList(
                new Player("A"),
                new Player("B"),
                new Player("C")
        ));
        damaged = new ArrayList<>(Arrays.asList(
                new Player("B"),
                new Player("A")
        ));
    }


    @Test
    void filterTarget_all() {
        /*damageable not targeted*/
        assertTrue(HistoryPolicy.ALL.filterTarget(damageable, targeted, damaged));

        /*damageable targeted*/
        targeted.add(damageable);
        assertTrue(HistoryPolicy.ALL.filterTarget(damageable, targeted, damaged));

        /*damageable targeted and damaged*/
        damaged.add(damageable);
        assertTrue(HistoryPolicy.ALL.filterTarget(damageable, targeted, damaged));
    }

    @Test
    void filterTarget_onlyDamaged() {
        /*damageable not targeted*/
        assertFalse(HistoryPolicy.ONLY_DAMAGED.filterTarget(damageable, targeted,
                damaged));

        /*damageable targeted*/
        targeted.add(damageable);
        assertFalse(HistoryPolicy.ONLY_DAMAGED.filterTarget(damageable, targeted,
                damaged));

        /*damageable targeted and damaged*/
        damaged.add(damageable);
        assertTrue(HistoryPolicy.ONLY_DAMAGED.filterTarget(damageable, targeted, damaged));
    }

    @Test
    void filterTarget_notTargeted() {
        /*damageable not targeted*/
        assertTrue(HistoryPolicy.NOT_TARGETED.filterTarget(damageable, targeted,
                damaged));

        /*damageable targeted*/
        targeted.add(damageable);
        assertFalse(HistoryPolicy.NOT_TARGETED.filterTarget(damageable, targeted,
                damaged));

        /*damageable targeted and damaged*/
        damaged.add(damageable);
        assertFalse(HistoryPolicy.NOT_TARGETED.filterTarget(damageable, targeted,
                damaged));
    }

    @Test
    void filterTarget_onlyTargeted() {
        /*damageable not targeted*/
        assertFalse(HistoryPolicy.ONLY_TARGETED.filterTarget(damageable, targeted,
                damaged));

        /*damageable targeted*/
        targeted.add(damageable);
        assertTrue(HistoryPolicy.ONLY_TARGETED.filterTarget(damageable, targeted,
                damaged));

        /*damageable targeted and damaged*/
        damaged.add(damageable);
        assertTrue(HistoryPolicy.ONLY_TARGETED.filterTarget(damageable, targeted,
                damaged));
    }
}