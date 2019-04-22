package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: empty iteration, one element, multiple elements
 */
class EffectIteratorTest {
    private EffectIterator empty;
    private EffectIterator oneElement;
    private EffectIterator multipleElements;
    private String name1 = "One";
    private String name2 = "Two";
    private String name3 = "Three";

    @BeforeEach
    void setUp() {
        empty = new EffectIterator(null);
        oneElement = new EffectIterator(new MockEffect(name1, null));
        multipleElements = new EffectIterator(
                new MockEffect(name1,
                        new MockEffect(name2,
                                new MockEffect(name3, null))));

    }

    /*Testing an empty iterator*/
    @Test
    void hasNext_empty() {
        assertFalse(empty.hasNext());
    }

    /*Testing an iterator with only one element*/
    @Test
    void hasNext_oneElement() {
        assertTrue(oneElement.hasNext());
        oneElement.next();
        assertFalse(oneElement.hasNext());
    }

    /*Testing with normal usage*/
    @Test
    void hasNext_multipleElements() {
        assertTrue(multipleElements.hasNext());
        multipleElements.next();
        assertTrue(multipleElements.hasNext());
        multipleElements.next();
        assertTrue(multipleElements.hasNext());
        multipleElements.next();
        assertFalse(multipleElements.hasNext());
    }

    /*Testing an empty iterator*/
    @Test
    void next_empty() {
        assertThrows(NoSuchElementException.class, () -> empty.next());
    }

    /*Testing an iterator with only one element*/
    @Test
    void next_oneElement() {
        assertEquals(name1, oneElement.next().getName());
        assertThrows(NoSuchElementException.class, () -> oneElement.next());
    }

    /*Testing with normal usage*/
    @Test
    void next_multipleElements() {
        assertEquals(name1, multipleElements.next().getName());
        assertEquals(name2, multipleElements.next().getName());
        assertEquals(name3, multipleElements.next().getName());
        assertThrows(NoSuchElementException.class, () -> multipleElements.next());
    }

    /*Mock Effect class for testing*/
    private class MockEffect implements EffectInterface {
        private String name;
        private EffectInterface decorated;

        private MockEffect(String name, EffectInterface decorated) {
            this.name = name;
            this.decorated = decorated;
        }

        @Override
        public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public EffectInterface getDecorated() {
            return decorated;
        }

        @Override
        public Iterator<EffectInterface> iterator() {
            return new EffectIterator(decorated);
        }
    }
}