package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.*;
import java.util.*;

/**
 * This class is used in Domination Mode. It represents a spawn/flag which a player can control.
 */
public class Spawn implements Damageable {

    /**
     * Tokens of Players that hitted the spawn in Domination mode.
     */
    private List<Player> tokens;

    /**
     * Default constructor
     */
    public Spawn() {
        tokens = new ArrayList<>();
    }


    /**
     * 
     */
    private AmmoCube color;

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
    public AbstractSquare getPosition() {
        // TODO implement here
        return null;
    }

    /**
     * @param newPosition
     */
    public void setPosition(AbstractSquare newPosition) {
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