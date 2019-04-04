
import java.util.*;

/**
 * 
 */
public interface ScoreListener {

    /**
     * @param killed
     */
    public void addKilled(Damageable killed);

    /**
     * 
     */
    public void scoreAllKilled();

    /**
     * 
     */
    public void scoreBoard();

    /**
     * @return
     */
    public List<Damageable> getKilled();

    /**
     * 
     */
    public void emptyKilledList();

}