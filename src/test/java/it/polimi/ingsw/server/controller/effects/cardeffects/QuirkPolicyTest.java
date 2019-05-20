package it.polimi.ingsw.server.controller.effects.cardeffects;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: Abbo Giulio A.
 * Testing: if an element is in the array.
 */
class QuirkPolicyTest {

    @Test
    void isIn() {
        QuirkPolicy[] policies = {QuirkPolicy.SINGLE_DIRECTION,
                QuirkPolicy.MAX_TWO_HITS};
        List<QuirkPolicy> asList = Arrays.asList(policies);
        for (QuirkPolicy p : QuirkPolicy.values()) {
            assertEquals(asList.contains(p), p.isIn(policies));
        }
    }
}