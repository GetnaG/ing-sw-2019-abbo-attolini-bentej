package it.polimi.ingsw.server.controller.effects;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over a sequence of effects.
 * Every effect contains a reference to the following one, this class
 * iterates over all the effects in the chain.
 *
 * @author Abbo Giulio A.
 * @see EffectInterface
 */
public class EffectIterator implements Iterator<EffectInterface> {
    /**
     * The next element of the iteration.
     */
    private EffectInterface nextUp;

    /**
     * Creates an iterator from the provided element.
     * The element {@code first} will be the first element returned by
     * {@linkplain #next()};
     *
     * @param first the first element of the iteration
     */
    public EffectIterator(EffectInterface first) {
        nextUp = first;
    }

    /**
     * Returns true if the iteration has more elements.
     * (In other words, returns true if {@linkplain #next} would
     * return an element rather than throwing an exception.)
     *
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return nextUp != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public EffectInterface next() {
        if (hasNext()) {
            EffectInterface current = nextUp;
            nextUp = nextUp.getDecorated();
            return current;
        }
        throw new NoSuchElementException();
    }
}
