package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;

/**
 * This class is used in Domination Mode. It represents a spawn/flag which a player can control.
 */
public class Spawn implements Damageable {

    /**
     * Tokens of Players that hit the spawn in Domination mode.
     */
    private List<Player> tokens;
    /**
     *
     */
    private AmmoCube color;

    /**
     * Default constructor
     */
    public Spawn() {
        tokens = new ArrayList<>();
    }




    /**
     * This function
     * @param shooters represents a player token(s)
     */
    public void giveDamage(List<Player> shooters) {
        tokens.addAll(new ArrayList<>(shooters));
    }

    /**
     * @param shooters
     */
    public void giveMark(List<Player> shooters) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Square getPosition() {
        // TODO implement here
        return null;
    }

    /**
     * @param newPosition
     */
    public void setPosition(Square newPosition) {
        // TODO implement here
    }

    /**
     * 
     */
    public void scoreAndResetDamage() {
        // TODO implement here
    }

    /**
     * @return
     */
    public Player getKillshotPlayer() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Player getOverkillPlayer() {
        // TODO implement here
        return null;
    }

    public List<Player> getTokens() {
        return tokens;
    }

}