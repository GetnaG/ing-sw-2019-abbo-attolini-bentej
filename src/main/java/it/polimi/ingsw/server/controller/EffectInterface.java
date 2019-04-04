package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.*;
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
    public List<Damageable> runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted);

    /**
     * @return
     */
    public String getName();

    /**
     * @return
     */
    public EffectInterface getDecorated();

}