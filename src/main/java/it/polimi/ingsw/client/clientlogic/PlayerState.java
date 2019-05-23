package it.polimi.ingsw.client.clientlogic;

import java.util.List;

/**
 * Represents the state of a player.
 */
public class PlayerState {
    private int turnPosition;
    private int squarePosition;
    private String nickname;
    private String figureRes;
    private List<Integer> ammoCubes;
    private boolean isPlayerBoardFrenzy;
    private int skullNumber;
    private List<String> damage;
    private boolean isConnected;
    private List<String> loadedWeapons;
    private List<String> unloadedWeapons;
    private List<Boolean> areWeaponsLoaded;
    private List<String> powerups;

    public PlayerState(int turnPosition, int squarePosition, String nickname, String figureRes, List<Integer> ammoCubes, boolean isPlayerBoardFrenzy, int skullNumber, List<String> damage, boolean isConnected, List<String> loadedWeapons, List<Boolean> areWeaponsLoaded, List<String> powerups) {
        this.turnPosition = turnPosition;
        this.squarePosition = squarePosition;
        this.nickname = nickname;
        this.figureRes = figureRes;
        this.ammoCubes = ammoCubes;
        this.isPlayerBoardFrenzy = isPlayerBoardFrenzy;
        this.skullNumber = skullNumber;
        this.damage = damage;
        this.isConnected = isConnected;
        this.loadedWeapons = loadedWeapons;
        this.areWeaponsLoaded = areWeaponsLoaded;
        this.powerups = powerups;
    }

    public int getTurnPosition() {
        return turnPosition;
    }

    public void setTurnPosition(int turnPosition) {
        this.turnPosition = turnPosition;
    }

    public int getSquarePosition() {
        return squarePosition;
    }

    public void setSquarePosition(int squarePosition) {
        this.squarePosition = squarePosition;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFigureRes() {
        return figureRes;
    }

    public void setFigureRes(String figureRes) {
        this.figureRes = figureRes;
    }

    public List<Integer> getAmmoCubes() {
        return ammoCubes;
    }

    public void setAmmoCubes(List<Integer> ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    public boolean isPlayerBoardFrenzy() {
        return isPlayerBoardFrenzy;
    }

    public void setPlayerBoardFrenzy(boolean playerBoardFrenzy) {
        isPlayerBoardFrenzy = playerBoardFrenzy;
    }

    public int getSkullNumber() {
        return skullNumber;
    }

    public void setSkullNumber(int skullNumber) {
        this.skullNumber = skullNumber;
    }

    public List<String> getDamage() {
        return damage;
    }

    public void setDamage(List<String> damage) {
        this.damage = damage;
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

    public void setLoadedWeapons(List<String> loadedWeapons) {
        this.loadedWeapons = loadedWeapons;
    }

    public List<Boolean> getAreWeaponsLoaded() {
        return areWeaponsLoaded;
    }

    public void setAreWeaponsLoaded(List<Boolean> areWeaponsLoaded) {
        this.areWeaponsLoaded = areWeaponsLoaded;
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

    public void setUnloadedWeapons(List<String> unloadedWeapons) {
        this.unloadedWeapons = unloadedWeapons;
    }
}
