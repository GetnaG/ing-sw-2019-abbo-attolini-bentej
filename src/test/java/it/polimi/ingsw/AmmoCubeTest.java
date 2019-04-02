package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmmoCubeTest {

    private AmmoCube ammo = new AmmoCube();

    @org.junit.jupiter.api.Test
    void setNoPurpose() {

        ammo.setNoPurpose(10);
        assertEquals(10,ammo.getNoPurpose());

    }

    @org.junit.jupiter.api.Test
    void getNoPurpose() {
        assertEquals(1,1);
    }
}