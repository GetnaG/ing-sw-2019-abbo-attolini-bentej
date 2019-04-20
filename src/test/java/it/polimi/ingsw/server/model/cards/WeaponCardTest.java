package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: Abbo Giulio A.
 * Testing correct behaviour with normal usage
 */
class WeaponCardTest {
    private WeaponCard card;
    private String id;
    private List<AmmoCube> cubes;
    private String[][] effects;

    @BeforeEach
    void setUp() {
        id = "Weapon id";
        cubes = Arrays.asList(AmmoCube.BLUE, AmmoCube.RED, AmmoCube.RED);
        effects = new String[][]{{"00", "01"}, {"10", "11", "12"}, {"20"}};

        card = new WeaponCard(id, cubes, effects, true);
    }

    /*Testing that the sequences are in the right order*/
    @Test
    void getPossibleSequences_ordered() {
        List<EffectInterface> sequences = card.getPossibleSequences();
        int i = 0, j = 0;
        for (EffectInterface e : sequences) {
            while (e.getDecorated() != null) {
                assertEquals(effects[i][j], e.getName());
                e = e.getDecorated();
                j++;
            }
            j = 0;
            i++;
        }
    }

    /*Testing that the sequences are in the right order*/
    @Test
    void getPossibleSequences_unOrdered() {
        WeaponCard card = new WeaponCard(id, cubes, new String[][]
                {{"A"}, {"B"}, {"A", "D"}}, false);

        String[][] expected = {
                {"A", "B", "A"},
                {"A", "A", "B"},
                {"B", "A", "A"}
        };

        List<EffectInterface> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (EffectInterface e : sequences) {
            while (e.getDecorated() != null) {
                assertEquals(expected[i][j], e.getName());
                e = e.getDecorated();
                j++;
            }
            j = 0;
            i++;
        }
    }

    /*Testing the returned object*/
    @Test
    void getCost() {
        assertEquals(cubes, card.getCost());
    }
}