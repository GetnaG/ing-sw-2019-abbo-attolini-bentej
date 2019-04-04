
import java.util.*;

/**
 * 
 */
public interface EffectInterface {

    /**
     * @param subjectPlayer 
     * @param board 
     * @param alredyTargeted 
     * @return
     */
    public List<Damageable> runEffect(void subjectPlayer, GameBoard board, List<Damageable> alredyTargeted);

    /**
     * @return
     */
    public String getName();

    /**
     * @return
     */
    public EffectInterface getDecorated();

}