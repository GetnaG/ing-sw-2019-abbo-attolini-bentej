package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.controller.*;
import java.util.List;

/**
 * 
 */
public class AbstractEffect implements EffectInterface {

    /**
     * Default constructor
     */
    public AbstractEffect() {
    }

    /**
     * 
     */
    private int damageQty;

    /**
     * 
     */
    private int marksQty;

    /**
     * 
     */
    private boolean allowSameTarget;


    /**
     * @return
     */
    public List<AmmoCube> getAddictionalCost() {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    private void checkIfTargetCanUseTagBack() {
        // TODO implement here
    }

    /**
     * @param subjectPlayer 
     * @param board 
     * @param alredyTargeted 
     * @return
     */
    public List<Damageable> runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String getName() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public EffectInterface getDecorated() {
        // TODO implement here
        return null;
    }

}