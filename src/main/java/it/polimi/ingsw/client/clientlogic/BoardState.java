package it.polimi.ingsw.client.clientlogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of the Game Board.
 *
 * @author Fahed B. Tej
 * @see MatchState
 */
public class BoardState {
    /**
     * Configuration of the map
     */
    private int configurationID;
    /**
     * A list of 12 ammoCardID, one for each square.
     */
    private List<String> ammoCardsID;
    /**
     * A list of weaponCardsID
     */
    private List<String> weaponCardsID;
    /**
     * True if the weapon deck is not empty
     */
    private boolean isWeaponDeckDrawable;
    /**
     * List of players who occupy each position of the killshot track
     */
    private List<List<String>> killshotTrack;
    /**
     * Tells if action tile is frenzy
     */
    private boolean isActionTileFrenzy;
    /**
     * A list of connected players
     */
    private List<String> connectedPlayers;
    /**
     * A list of disconnected players. It is used to check the previous state of the conncected players.
     */
    private List<String> oldConnectedPlayer;


    BoardState() {
        connectedPlayers = new ArrayList<>();
        oldConnectedPlayer = new ArrayList<>();
    }

    int getConfigurationID() {
        return configurationID;
    }

    void setConfigurationID(int configurationID) {
        this.configurationID = configurationID;
    }

    List<String> getAmmoCardsID() {
        return ammoCardsID;
    }

    void setAmmoCardsID(List<String> ammoCardsID) {
        this.ammoCardsID = ammoCardsID;
    }

    List<String> getWeaponCardID() {
        return weaponCardsID;
    }

    void setWeaponCardID(List<String> weaponCardID) {
        weaponCardsID = weaponCardID;
    }

    boolean isIsWeaponDeckDrawable() {
        return isWeaponDeckDrawable;
    }

    void setIsWeaponDeckDrawable(boolean isWeaponDeckDrawable) {
        this.isWeaponDeckDrawable = isWeaponDeckDrawable;
    }

    List<List<String>> getKillshotTrack() {
        return killshotTrack;
    }

    void setKillshotTrack(List<List<String>> killshotTrack) {
        this.killshotTrack = killshotTrack;
    }

    boolean isIsActionTileFrenzy() {
        return isActionTileFrenzy;
    }

    void setIsActionTileFrenzy(boolean isActionTileFrenzy) {
        this.isActionTileFrenzy = isActionTileFrenzy;
    }

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(List<String> newStateConnectedPlayers) {
        oldConnectedPlayer = new ArrayList<>();
        oldConnectedPlayer.addAll(connectedPlayers);
        connectedPlayers = newStateConnectedPlayers;
    }

    public List<String> getDisconnectedPlayers() {
        List<String> disconnected = new ArrayList<>();
        for (String name : oldConnectedPlayer) {
            if (!connectedPlayers.contains(name)) {
                disconnected.add(name);
            }
        }
        return disconnected;
    }

    public List<String> getJustConnectedPlayers() {
        List<String> connected = new ArrayList<>();
        for (String name : connectedPlayers) {
            if (!oldConnectedPlayer.contains(name)) {
                connected.add(name);
            }
        }
        return connected;
    }
}
