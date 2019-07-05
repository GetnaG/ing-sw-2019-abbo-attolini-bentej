package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used in Domination Mode. It represents a spawn/flag which a player can control.
 */
public class Spawn implements Damageable {
    /**
     * Tokens of Players that hit the spawn in Domination mode.
     */
    private List<Player> tokens;
    private AmmoCube color;

    /**
     * Default constructor
     */
    Spawn() {
        tokens = new ArrayList<>();
    }

    /**
     * This function
     *
     * @param shooters represents a player token(s)
     */
    public void giveDamage(List<Player> shooters) {
        tokens.addAll(new ArrayList<>(shooters));
    }

    public void giveMark(List<Player> shooters) {
        throw new UnsupportedOperationException();
    }

    public Square getPosition() {
        throw new UnsupportedOperationException();

    }


    public void setPosition(Square newPosition) {
        throw new UnsupportedOperationException();
    }

    public void scoreAndResetDamage() {
        throw new UnsupportedOperationException();
    }

    public Player getKillshotPlayer() {
        throw new UnsupportedOperationException();

    }

    public Player getOverkillPlayer() {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    List<Player> getTokens() {
        return tokens;
    }
}