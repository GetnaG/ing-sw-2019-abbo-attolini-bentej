package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for representing the Turret Track used in Turret Mode.
 */
public class TurretTrack implements AbstractTrack {
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
    TurretTrack(List<AmmoCard> ammoCards) {
        skullsLeft = 8;
        this.ammoCards = ammoCards;

    }

    /**
     * @return the number of skulls left.
     */
    @Override
    public int getSkullsLeft() {
        return skullsLeft;
    }

    /**
     * In Turret Mode the Track does not have any points.
     */
    @Override
    public void score() {
        // does nothing
    }

    /**
     * In Turret Mode we do not need to keep track of tokens.
     *
     * @param tokens Player who did the kill. If overkilled, the list must contain the same player two times.
     */
    @Override
    public void addTokens(List<Player> tokens) {
        // does nothing
    }

    /**
     * Removing the leftmost skull.
     */
    @Override
    public void removeSkull() {
        skullsLeft = skullsLeft - 1;
    }

    /**
     * Gets game mode.
     */
    @Override
    public String getGameMode() {
        return "Turret";
    }

    @Override
    public List<List<Player>> getTokens() {
        return new ArrayList<>();
    }


}