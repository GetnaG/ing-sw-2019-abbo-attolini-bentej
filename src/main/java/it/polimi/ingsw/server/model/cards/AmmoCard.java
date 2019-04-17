package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AmmoCube;

import java.util.Arrays;
import java.util.List;

/**
 *  This class represents an Ammo Card. It can contain ammos or powerups. If used in Turret Mode, it is associated with a Turret Square.
 */
public class AmmoCard extends AbstractCard {
    private AmmoCube[] cubes;
    private PowerupCard powerup;

    public List<AmmoCube> getCubes() {
        return Arrays.asList(cubes);
    }

    public PowerupCard getPowerup() {
        return powerup;
    }
}