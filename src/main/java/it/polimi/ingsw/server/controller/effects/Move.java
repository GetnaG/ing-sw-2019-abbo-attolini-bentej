package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Border;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a Move in the game.
 *
 * @author Fahed Ben Tej
 */
public class Move implements EffectInterface {
    /**
     * Moves a player by one cell
     * @param subjectPlayer     the player who is moving
     * @param allTargets
     * @param board             board of the game
     * @param alredyTargeted    not used
     * @param damageTargeted
     */
    @Override
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> alredyTargeted, List<Damageable> damageTargeted) {
        List<Square> neighboursBy1 = neighbours(subjectPlayer.getPosition());
        Square destination = null;
        try {
            destination = subjectPlayer.getToClient().chooseDestination(
                    neighboursBy1);
        } catch (ToClientException e) {
            //TODO Handle if the user is disconnected
        }


        subjectPlayer.setPosition(destination);
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return "Move";
    }

    /**
     * @return
     */
    @Override
    public EffectInterface getDecorated() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToChain(EffectInterface last) {
        //TODO does this need fixing?
    }


    @Override
    public Iterator<EffectInterface> iterator() {
        return null;
    }

    /**
     * Gets walkable (i.e. there isn't a wall) neighbours of a square
     * @param square starting square
     * @return a list of walkable surrounding square
     */
    private List<Square> neighbours(Square square){
        List<Square> neighbours = new ArrayList<>();

        if(square.getNorthBorder() != Border.WALL)
            neighbours.add(square.getNorth());
        if(square.getEastBorder() != Border.WALL)
            neighbours.add(square.getEast());
        if(square.getSouthBorder() != Border.WALL)
            neighbours.add(square.getSouth());
        if(square.getWestBorder() != Border.WALL)
            neighbours.add(square.getWest());

        return neighbours;
    }
}