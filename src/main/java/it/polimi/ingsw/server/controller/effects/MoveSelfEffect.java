package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This effect moves the subject to a destination of his choice.
 * A maximum distance can be set, in this case only <i>valid</i> moved are
 * allowed.
 *
 * @author Abbo Giulio A.
 * @see Square
 * @see GameBoard
 */
public class MoveSelfEffect implements EffectInterface {
    /**
     * The name of this effect.
     */
    private String id;
    /**
     * The effect that could follow this.
     */
    private EffectInterface decorated;
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
     */
    public MoveSelfEffect(String id, int maxDistance) {
        this.id = id;
        this.maxDistance = maxDistance;
        decorated = null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The subject is asked to choose a valid destination.
     */
    @Override
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets,
                          GameBoard board, List<Damageable> allTargeted,
                          List<Damageable> damageTargeted) {

        /*Filtering the squares*/
        List<Square> available = (maxDistance == -1) ?
                board.getValidDestinations(subjectPlayer.getPosition(),
                        maxDistance, false) :
                new ArrayList<>(board.getAllSquares());

        /*Choosing the destination*/
        Square destination;
        try {
            destination = subjectPlayer.getToClient().chooseDestination(available);
        } catch (ToClientException e) {

            /*In case of problems the destination is unchanged*/
            destination = subjectPlayer.getPosition();
        }

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
    public EffectInterface getDecorated() {
        return decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToChain(EffectInterface last) {
        if (decorated == null)
            decorated = last;
        else
            decorated.addToChain(last);
    }

    /**
     * Returns an iterator over the elements of this chain of effects.
     *
     * @return an iterator over the elements of this chain of effects
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return new EffectIterator(this);
    }
}
