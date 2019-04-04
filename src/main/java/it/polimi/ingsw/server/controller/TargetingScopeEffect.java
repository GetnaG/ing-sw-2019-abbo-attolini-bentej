package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.*;
import java.util.*;

/**
 * 
 */
public class TargetingScopeEffect implements EffectInterface {

    /**
     * Default constructor
     */
    public TargetingScopeEffect() {
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