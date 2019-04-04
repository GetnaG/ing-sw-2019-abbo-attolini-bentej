
import java.util.*;

/**
 * 
 */
public class Player implements Damageable {

    /**
     * Default constructor
     */
    public Player() {
    }

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private int score;

    /**
     * 
     */
    private boolean firstPlayer;

    /**
     * 
     */
    private String figureRes;

    /**
     * 
     */
    private boolean respawning;

    /**
     * 
     */
    private boolean suspended;


    /**
     * 
     */
    private ScoreListener scorer;

    /**
     * 
     */
    private AmmoBox ammoBox;

    /**
     * 
     */
    private PlayerBoardInterface playerBoard;

    /**
     * 
     */
    private ToClientInterface toClient;

    /**
     * 
     */
    private HandManager hand;

    /**
     * @return
     */
    public List<Action> getAdrenalineActions() {
        // TODO implement here
        return null;
    }

    /**
     * @param cubes
     */
    public void addAmmo(List<AmmoCube> cubes) {
        // TODO implement here
    }

    /**
     * @param card
     */
    public void addPowerup(PowerupCard card) {
        // TODO implement here
    }

    /**
     * @param card
     */
    public void removePowerup(PowerupCard card) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<PowerupCard> getAllPowerup() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getNumOfPowerups() {
        // TODO implement here
        return 0;
    }

    /**
     * @param card 
     * @param buying 
     * @return
     */
    public boolean canAfford(WeaponCard card, boolean buying) {
        // TODO implement here
        return false;
    }

    /**
     * @param card 
     * @param buying 
     * @return
     */
    public List<PowerupCard> canAffordWithPowerup(WeaponCard card, boolean buying) {
        // TODO implement here
        return null;
    }

    /**
     * @param card 
     * @param asCubes
     */
    public void buy(WeaponCard card, List<PowerupCard> asCubes) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<WeaponCard> getAllWeapons() {
        // TODO implement here
        return null;
    }

    /**
     * @param card
     */
    public void discard(WeaponCard card) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<WeaponCard> getLoadedWeapons() {
        // TODO implement here
        return null;
    }

    /**
     * @param weapon
     */
    public void unload(WeaponCard weapon) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<WeaponCard> getReloadableWeapons() {
        // TODO implement here
        return null;
    }

    /**
     * @param weapon
     */
    public void reload(WeaponCards weapon) {
        // TODO implement here
    }

    /**
     * @param effect 
     * @return
     */
    public boolean canAfford(EffectInterface effect) {
        // TODO implement here
        return false;
    }

    /**
     * @param effect 
     * @return
     */
    public List<PowerupCard> canAffordWithPowerup(EffectInterface effect) {
        // TODO implement here
        return null;
    }

    /**
     * @param shooters
     */
    public void giveDamage(List<Player> shooters) {
        // TODO implement here
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

}