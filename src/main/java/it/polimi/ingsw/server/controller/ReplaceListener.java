
import java.util.*;

/**
 * 
 */
public interface ReplaceListener {

    /**
     * @param toBeReplaced
     */
    public void addTurretSquare(TurretSquare toBeReplaced);

    /**
     * @param toBeReplaced
     */
    public void addSpawnSquare(SpawnSquare toBeReplaced);

    /**
     * @param location 
     * @param weapons
     */
    public void replaceDiscardedWeapons(AbstractSquare location, List<WeaponCard> weapons);

    /**
     * 
     */
    public void replaceAll();

}