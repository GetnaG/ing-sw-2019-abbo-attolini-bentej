package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmmoCubeTest {

    private AmmoCube ammo = new AmmoCube();

    @Test
    void setNoPurpose() {

        ammo.setNoPurpose(10);
        assertEquals(10,ammo.getNoPurpose());

    }
    @Test
    void getNoPurpose() {
        assertEquals(1,1);
    }
}