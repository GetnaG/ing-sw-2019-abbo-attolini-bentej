package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This effect moves the subject to a destination of his choice.
 * A maximum distance can be set, in this case only <i>valid</i> moved are
 * allowed.
 *
 * @author giubots
 * @see Square
 * @see GameBoard
 */
public class MoveSelfEffect implements EffectInterface {
    /**
     * The name of this effect.
     */
    private String id;
    /**
     * The cost of this effect.
     */
    private List<AmmoCube> cost;
    /**
     * The maximum number of moves or -1 if not set.
     */
    private int maxDistance;

    /**
     * Creates an effect with the provided name that moves the subject to a
     * valid destination, eventually with a maximum distance.
     *
     * @param id          the name of this effect
     * @param maxDistance -1 or the maximum distance from the position of the
     *                    subject
     * @param cost        the cost of this effect
     */
    public MoveSelfEffect(String id, int maxDistance, List<AmmoCube> cost) {
        this.id = id;
        this.maxDistance = maxDistance;
        this.cost = cost;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The subject is asked to choose a valid destination.
     */
    @Override
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets,
                          GameBoard board, List<Damageable> allTargeted,
                          List<Damageable> damageTargeted) throws ToClientException {

        /*Filtering the squares*/
        List<Square> available = (maxDistance == -1) ?
                new ArrayList<>(board.getAllSquares()) :
                board.getValidDestinations(subjectPlayer.getPosition(), maxDistance, false);

        /*Choosing the destination*/
        Square destination = subjectPlayer.getToClient().chooseDestination(available);

        /*Setting the destination*/
        subjectPlayer.setPosition(destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getCost() {
        return new ArrayList<>(cost);
    }
}
