package it.polimi.ingsw.client.clientlogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a player.
 */
public class PlayerState {
    /**
     * represents the players's position on the board aka on witch square is located
     */
    private int squarePosition;
    /**
     * represents the name choosen by the user to give to his character
     */
    private String nickname;
    /**
     * represents the ammo cubes held by the player, ordered in alphabetical order
     */
    private List<Integer> ammoCubes;
    /**
     * is set to true when a player enters the frenzy mode as a result of the damage suffered
     */
    private boolean isPlayerBoardFrenzy;
    /**
     * represents the number of times the player has been killed
     */
    private int skullNumber;
    /**
     * lists the players that inflicted damage to the player
     */
    private List<String> damage;
    /**
     * lists the players that gave a mark to the player
     */
    private List<String> marks;
    /**
     * is set to true when the players connects to the server
     */
    private boolean isConnected;
    /**
     * lists the loaded weapons held by the the player
     */
    private List<String> loadedWeapons;
    /**
     * lists the unloaded weapons held by the the player
     * it will be asked to reload them during the end of the turn
     */
    private List<String> unloadedWeapons;
    /**
     * lists the powerup cards in the player's hand
     */
    private List<String> powerups;
    /**
     * it's true when it becomes the player's turn
     */
    private boolean isCurrent;

    PlayerState(int turnPosition, String nickname) {
        squarePosition = -1;
        this.nickname = nickname;
        ammoCubes = new ArrayList<>();
        isPlayerBoardFrenzy = false;
        skullNumber = -1;
        damage = new ArrayList<>();
        marks = new ArrayList<>();
        isConnected = true;
        loadedWeapons = new ArrayList<>();
        unloadedWeapons = new ArrayList<>();
        powerups = new ArrayList<>();
        isCurrent = false;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    void setCurrent(boolean current) {
        isCurrent = current;
    }

    public int getSquarePosition() {
        return squarePosition;
    }

    void setSquarePosition(int squarePosition) {
        this.squarePosition = squarePosition;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Integer> getAmmoCubes() {
        return ammoCubes;
    }

    void setAmmoCubes(List<Integer> ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    public boolean isPlayerBoardFrenzy() {
        return isPlayerBoardFrenzy;
    }

    void setPlayerBoardFrenzy(boolean playerBoardFrenzy) {
        isPlayerBoardFrenzy = playerBoardFrenzy;
    }

    public int getSkullNumber() {
        return skullNumber;
    }

    void setSkullNumber(int skullNumber) {
        this.skullNumber = skullNumber;
    }

    public List<String> getDamage() {
        return damage;
    }

    public void setDamage(List<String> damage) {
        this.damage = damage;
    }

    public List<String> getMarks() {
        return marks;
    }

    public void setMarks(List<String> damage) {
        marks = damage;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public List<String> getLoadedWeapons() {
        return loadedWeapons;
    }

    void setLoadedWeapons(List<String> loadedWeapons) {
        this.loadedWeapons = loadedWeapons;
    }

    public List<String> getPowerups() {
        return powerups;
    }

    public void setPowerups(List<String> powerups) {
        this.powerups = powerups;
    }

    public List<String> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    void setUnloadedWeapons(List<String> unloadedWeapons) {
        this.unloadedWeapons = unloadedWeapons;
    }
}
