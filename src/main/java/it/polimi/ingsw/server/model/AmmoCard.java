package it.polimi.ingsw.server.model;
import java.util.*;

/**
 *  This class represents an Ammo Card. It can contain ammos or powerups. If used in Turret Mode, it is associated with a Turret Square.
 */
public class AmmoCard extends AbstractCard {

    /**
     * Default constructor
     */
    public AmmoCard() {
    }

    /**
     * @return
     */
    @Override
    public String getResName() {
        return null;
    }


    /**
     * 
     */
    private PowerupCard powerup;

    /**
     * 
     */
    private AmmoCube cubes;

}