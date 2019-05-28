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

    private int configurationID;
    private List<String> ammoCardsID;
    private List<String> weaponCardsID;
    private boolean isWeaponDeckDrawable;
    private List<List<String>> killshotTrack;
    private boolean isActionTileFrenzy;
    private List<String> connectedPlayers;


    public BoardState() {
        this.connectedPlayers = new ArrayList<>();
    }

    public BoardState(int configurationID, List<String> ammoCardsID, List<String> weaponCardsID, boolean isWeaponDeckDrawable, List<List<String>> killshotTrack, boolean isActionTileFrenzy) {
        this.configurationID = configurationID;
        this.ammoCardsID = ammoCardsID;
        this.weaponCardsID = weaponCardsID;
        this.isWeaponDeckDrawable = isWeaponDeckDrawable;
        this.killshotTrack = killshotTrack;
        this.isActionTileFrenzy = isActionTileFrenzy;
    }

    public int getConfigurationID() {
        return configurationID;
    }

    public void setConfigurationID(int configurationID) {
        this.configurationID = configurationID;
    }

    public List<String> getAmmoCardsID() {
        return ammoCardsID;
    }

    public void setAmmoCardsID(List<String> ammoCardsID) {
        this.ammoCardsID = ammoCardsID;
    }

    public List<String> getWeaponCardID() {
        return weaponCardsID;
    }

    public void setWeaponCardID(List<String> weaponCardID) {
        weaponCardsID = weaponCardID;
    }

    public boolean isIsWeaponDeckDrawable() {
        return isWeaponDeckDrawable;
    }

    public void setIsWeaponDeckDrawable(boolean isWeaponDeckDrawable) {
        this.isWeaponDeckDrawable = isWeaponDeckDrawable;
    }

    public List<List<String>> getKillshotTrack() {
        return killshotTrack;
    }

    public void setKillshotTrack(List<List<String>> killshotTrack) {
        this.killshotTrack = killshotTrack;
    }

    public boolean isIsActionTileFrenzy() {
        return isActionTileFrenzy;
    }

    public void setIsActionTileFrenzy(boolean isActionTileFrenzy) {
        this.isActionTileFrenzy = isActionTileFrenzy;
    }

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }
}
