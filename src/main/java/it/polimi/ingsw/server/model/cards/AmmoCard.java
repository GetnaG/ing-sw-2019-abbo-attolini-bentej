package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.AmmoCube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This represents an ammo card, with ammo cubes and a powerup.
 * Objects of this class are instantiated by an
 * {@link it.polimi.ingsw.server.persistency.AmmoCardLoader}.
 *
 * @author giubots
 * @see it.polimi.ingsw.server.persistency.AmmoCardLoader
 * @see AmmoCube
 * @see PowerupCard
 */
public class AmmoCard extends AbstractCard {
    /**
     * The cubes this instance allows to pick up.
     */
    private AmmoCube[] cubes;

    /**
     * Whether this instance allows to draw a {@linkplain PowerupCard}.
     */
    private boolean powerup;

    /**
     * Constructor used for testing purposes.
     *
     * @param id      the id to locate the resources for this object
     * @param cubes   the cubes this instance allows to pick up
     * @param powerup whether this instance allows to draw a {@linkplain PowerupCard}
     */
    public AmmoCard(String id, AmmoCube[] cubes, boolean powerup) {
        super(id);
        this.cubes = cubes;
        this.powerup = powerup;
    }

    /**
     * Returns a {@linkplain List} of the cubes that this allows to pick up.
     *
     * @return the cubes that this allows to pick up
     */
    public synchronized List<AmmoCube> getCubes() {
        return new ArrayList<>(Arrays.asList(cubes));
    }

    /**
     * Returns whether this instance allows to draw a {@linkplain PowerupCard}.
     *
     * @return true if a {@linkplain PowerupCard} can be picked up
     */
    public boolean hasPowerup() {
        return powerup;
    }
}