
import java.util.*;

/**
 * 
 */
public class GameBoard implements ReplaceListener {

    /**
     * Default constructor
     */
    public GameBoard() {
    }


    /**
     * 
     */
    private Set<Room> configuration;

    /**
     * 
     */
    private PowerupDeck powerupDeck;


    /**
     * 
     */
    private WeaponDeck weaponDeck;

    /**
     * 
     */
    private AmmoDeck ammoDeck;

    /**
     * 
     */
    private AbstractTrack track;

    /**
     * @return
     */
    public AmmoCard getAmmoCard() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public PowerupCard getPowerupCard() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public WeaponCard getWeaponCard() {
        // TODO implement here
        return null;
    }

    /**
     * @param color 
     * @return
     */
    public AbstractSquare findSpawn(AmmoCube color) {
        // TODO implement here
        return null;
    }

    /**
     * @param start 
     * @param maxDistance 
     * @param sameDirection 
     * @return
     */
    public List<AbstractSquares> getValidDestinations(AbstractSquare start, int maxDistance, boolean sameDirection) {
        // TODO implement here
        return null;
    }

    /**
     * @param tokens
     */
    public void addTokensAndRemoveSkull(List<Player> tokens) {
        // TODO implement here
    }

    /**
     * @return
     */
    public boolean checkFinalFrenzy() {
        // TODO implement here
        return false;
    }

    /**
     * 
     */
    public void scoreBoard() {
        // TODO implement here
    }

    /**
     * @param usedCard
     */
    public void putAmmoCard(AmmoCard usedCard) {
        // TODO implement here
    }

    /**
     * @param usedCard
     */
    public void putPowerupCard(PowerupCard usedCard) {
        // TODO implement here
    }

    /**
     * @param toBeReplaced
     */
    public void addTurretSquare(TurretSquare toBeReplaced) {
        // TODO implement here
    }

    /**
     * @param toBeReplaced
     */
    public void addSpawnSquare(SpawnSquare toBeReplaced) {
        // TODO implement here
    }

    /**
     * @param location 
     * @param weapons
     */
    public void replaceDiscardedWeapons(AbstractSquare location, List<WeaponCard> weapons) {
        // TODO implement here
    }

    /**
     * 
     */
    public void replaceAll() {
        // TODO implement here
    }

}