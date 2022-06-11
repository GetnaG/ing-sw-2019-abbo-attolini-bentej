package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * Author: giubots
 * Testing: null, empty, no desired elements, normal.
 */
class AmmoCubeTest {
    private List<AmmoCube> noBlue;
    private List<AmmoCube> noYellow;
    private List<AmmoCube> noRed;
    private List<AmmoCube> noAny;
    private List<AmmoCube> normal;

    @BeforeEach
    void setUp() {
        noBlue = new ArrayList<>();
        noYellow = new ArrayList<>();
        noRed = new ArrayList<>();
        noAny = new ArrayList<>();
        normal = new ArrayList<>();

        Collections.addAll(noBlue,
                AmmoCube.RED,
                AmmoCube.YELLOW,
                AmmoCube.YELLOW);
        Collections.addAll(noYellow,
                AmmoCube.BLUE,
                AmmoCube.BLUE,
                AmmoCube.BLUE);
        Collections.addAll(noRed,
                AmmoCube.YELLOW,
                AmmoCube.ANY);
        Collections.addAll(noAny,
                AmmoCube.RED);
        Collections.addAll(normal,
                AmmoCube.RED,
                AmmoCube.RED,
                AmmoCube.YELLOW,
                AmmoCube.RED,
                AmmoCube.BLUE,
                AmmoCube.RED,
                AmmoCube.BLUE,
                AmmoCube.YELLOW,
                AmmoCube.ANY,
                AmmoCube.RED,
                AmmoCube.ANY,
                AmmoCube.YELLOW);
    }

    /*countBlue*/
    @Test
    void countBlue_null() {
        assertThrows(NullPointerException.class,
                () -> AmmoCube.countBlue(null));
    }

    @Test
    void countBlue_empty() {
        assertEquals(0, AmmoCube.countBlue(Collections.emptyList()));
    }

    @Test
    void countBlue_none() {
        assertEquals(0, AmmoCube.countBlue(noBlue));
    }

    @Test
    void countBlue_normal() {
        assertEquals(2, AmmoCube.countBlue(normal));
        assertEquals(3, AmmoCube.countBlue(noYellow));
        assertEquals(0, AmmoCube.countBlue(noRed));
        assertEquals(0, AmmoCube.countBlue(noAny));
    }

    /*countYellow*/
    @Test
    void countYellow_null() {
        assertThrows(NullPointerException.class,
                () -> AmmoCube.countYellow(null));
    }

    @Test
    void countYellow_empty() {
        assertEquals(0, AmmoCube.countYellow(Collections.emptyList()));
    }

    @Test
    void countYellow_none() {
        assertEquals(0, AmmoCube.countYellow(noYellow));
    }

    @Test
    void countYellow_normal() {
        assertEquals(3, AmmoCube.countYellow(normal));
        assertEquals(2, AmmoCube.countYellow(noBlue));
        assertEquals(1, AmmoCube.countYellow(noRed));
        assertEquals(0, AmmoCube.countYellow(noAny));
    }

    /*countRed*/
    @Test
    void countRed_null() {
        assertThrows(NullPointerException.class,
                () -> AmmoCube.countRed(null));
    }

    @Test
    void countRed_empty() {
        assertEquals(0, AmmoCube.countRed(Collections.emptyList()));
    }

    @Test
    void countRed_none() {
        assertEquals(0, AmmoCube.countRed(noRed));
    }

    @Test
    void countRed_normal() {
        assertEquals(5, AmmoCube.countRed(normal));
        assertEquals(0, AmmoCube.countRed(noYellow));
        assertEquals(1, AmmoCube.countRed(noBlue));
        assertEquals(1, AmmoCube.countRed(noAny));
    }

    /*countAny*/
    @Test
    void countAny_null() {
        assertThrows(NullPointerException.class,
                () -> AmmoCube.countAny(null));
    }

    @Test
    void countAny_empty() {
        assertEquals(0, AmmoCube.countAny(Collections.emptyList()));
    }

    @Test
    void countAny_none() {
        assertEquals(0, AmmoCube.countAny(noAny));
    }

    @Test
    void countAny_normal() {
        assertEquals(2, AmmoCube.countAny(normal));
        assertEquals(0, AmmoCube.countAny(noYellow));
        assertEquals(1, AmmoCube.countAny(noRed));
        assertEquals(0, AmmoCube.countAny(noBlue));
    }
}