
import java.util.*;

/**
 * 
 */
public interface Damageable {

    /**
     * @param shooters
     */
    public void giveDamage(List<Player> shooters);

    /**
     * @param shooters
     */
    public void giveMark(List<Player> shooters);

    /**
     * @return
     */
    public AbstractSquare getPosition();

    /**
     * @param newPosition
     */
    public void setPosition(AbstractSquare newPosition);

    /**
     * 
     */
    public void scoreAndResetDamage();

    /**
     * @return
     */
    public Player getKillshotPlayer();

    /**
     * @return
     */
    public Player getOverkillPlayer();

}