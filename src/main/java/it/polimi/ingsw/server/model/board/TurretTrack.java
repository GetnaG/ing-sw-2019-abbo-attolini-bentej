package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;

/**
 * This class is used for representing the Turret Track used in Turret Mode.
 *
 */
public class TurretTrack extends AbstractTrack {
    /**
     * The skulls left in the track.
     */
    private int skullsLeft;

    /**
     * The 5 ammo cards which a Player can choose from.
     */
    private List<AmmoCard> ammoCards;

    /**
     * Default constructor
     */
    public TurretTrack(List<AmmoCard> ammoCards) {
        skullsLeft = 8;
        this.ammoCards = ammoCards;

    }
    /**
     * Constructing a TurretTrack with the given number of initial skulls.
     */
    public TurretTrack(List<AmmoCard> ammoCards, int initialSkulls) {
        this.skullsLeft = initialSkulls;
        this.ammoCards = ammoCards;

    }

    /**
     * @return the number of skulls left.
     */
    @Override
    public int getSkullsLeft()  {
        return skullsLeft;
    }

    /**
     * In Turret Mode the Track does not have any points.
     * @return void
     */
    @Override
    public void score() {
        // does nothing
    }

    /**
     * In Turret Mode we do not need to keep track of tokens.
     *
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     *
     * @return
     */
    @Override
    public void addTokens(List<Player> tokens) {
        // does nothing
    }

    /**
     * Removing the leftmost skull.
     */
    @Override
    public void removeSkull()  {
        skullsLeft = skullsLeft -1;
    }

    public List<AmmoCard> getAmmoCards() {
        return ammoCards;
    }


}