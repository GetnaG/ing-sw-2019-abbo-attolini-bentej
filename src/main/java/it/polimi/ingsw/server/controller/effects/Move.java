package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Border;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
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
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> alredyTargeted, List<Damageable> damageTargeted) throws ToClientException {
        List<Square> neighboursBy1 = neighbours(subjectPlayer.getPosition());

        Square destination = subjectPlayer.getToClient().chooseDestination(neighboursBy1);

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
     * This effect has no cost.
     *
     * @return an empty list
     */
    @Override
    public List<AmmoCube> getCost() {
        return new ArrayList<>();
    }

    /**
     * Gets walkable (i.e. there isn't a wall) neighbours of a square
     * @param square starting square
     * @return a list of walkable surrounding square
     */
    private List<Square> neighbours(Square square){
        if (square == null) return null;
        List<Square> neighbours = new ArrayList<>();

        if(square.getNorth() != null && square.getNorthBorder() != Border.WALL)
            neighbours.add(square.getNorth());
        if(square.getEast() != null && square.getEastBorder() != Border.WALL)
            neighbours.add(square.getEast());
        if(square.getSouth() != null && square.getSouthBorder() != Border.WALL)
            neighbours.add(square.getSouth());
        if(square.getWest() != null && square.getWestBorder() != Border.WALL)
            neighbours.add(square.getWest());
        neighbours.add(square);

        return neighbours;
    }
}