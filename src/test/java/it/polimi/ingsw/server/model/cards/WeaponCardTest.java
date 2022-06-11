package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: giubots
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
        effects = new String[][]{{"shotgun", "powerGlove"}, {"cyberblade",
                "rocketLauncher", "hellion"}, {"heatseeker"}};

        card = new WeaponCard(id, cubes, effects, true);
    }

    /*Testing that the sequences are in the right order*/
    @Test
    void getPossibleSequences_ordered() {
        List<Action> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (Action action : sequences) {
            for (EffectInterface e : action) {
                assertEquals(effects[i][j], e.getName());
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
                {{"railgun"}, {"zx-2"}, {"flamethrower", "powerGlove"}}, false);
        card.runPermutation();

        String[][] expected = {
                {"railgun"},
                {"railgun", "zx-2"},
                {"zx-2", "railgun"},
                {"railgun", "flamethrower"},
                {"flamethrower", "railgun"},
                {"railgun", "zx-2", "flamethrower"},
                {"railgun", "flamethrower", "zx-2"},
                {"zx-2", "railgun", "flamethrower"},
                {"zx-2", "flamethrower", "railgun"},
                {"flamethrower", "railgun", "zx-2"},
                {"flamethrower", "zx-2", "railgun"}
        };

        List<Action> sequences = card.getPossibleSequences();
        int i = 0;
        int j = 0;
        for (Action action : sequences) {
            for (EffectInterface e : action) {
                assertEquals(expected[i][j], e.getName());
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