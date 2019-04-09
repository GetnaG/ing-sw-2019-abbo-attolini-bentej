package it.polimi.ingsw.server.controller.effects;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

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