package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.AmmoCube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: giubots
 * Testing: empty, normal, maximum cubes, ANY
 */
class AmmoBoxTest {
    private AmmoBox emptyBox;
    private AmmoBox normalBox; /*contains the elements in normalList*/
    private AmmoBox testBox;
    private List<AmmoCube> normalList;

    @BeforeEach
    void setUp() {
        emptyBox = new AmmoBox();
        normalBox = new AmmoBox();
        testBox = new AmmoBox();

        normalList = new ArrayList<>();
        normalList.add(AmmoCube.BLUE);
        normalList.add(AmmoCube.BLUE);
        normalList.add(AmmoCube.RED);
        normalList.add(AmmoCube.YELLOW);
        normalList.add(AmmoCube.RED);
        normalList.add(AmmoCube.BLUE);
        normalList.add(AmmoCube.RED);

        normalBox.addAmmo(normalList);
    }

    /*Testing getters: empty and normal*/
    @Test
    void getBlue() {
        assertEquals(0, emptyBox.getBlue());
        assertEquals(3, normalBox.getBlue());
    }

    @Test
    void getRed() {
        assertEquals(0, emptyBox.getRed());
        assertEquals(3, normalBox.getRed());
    }

    @Test
    void getYellow() {
        assertEquals(0, emptyBox.getYellow());
        assertEquals(1, normalBox.getYellow());
    }

    /*Testing addAmmo with single cube: normal(RED, YELLOW, BLUE)*/
    @Test
    void addAmmo_normal() {
        testBox.addAmmo(AmmoCube.RED);
        testBox.addAmmo(AmmoCube.BLUE);
        testBox.addAmmo(AmmoCube.YELLOW);
        testBox.addAmmo(AmmoCube.RED);
        testBox.addAmmo(AmmoCube.YELLOW);
        testBox.addAmmo(AmmoCube.YELLOW);

        assertEquals(1, testBox.getBlue());
        assertEquals(2, testBox.getRed());
        assertEquals(3, testBox.getYellow());
    }

    /*Testing addAmmo with single cube: type ANY*/
    @Test
    void addAmmo_any() {
        /*from empty AmmoBox*/
        assertThrows(IllegalArgumentException.class,
                () -> testBox.addAmmo(AmmoCube.ANY));

        /*no action is taken*/
        assertEquals(0, testBox.getBlue());
        assertEquals(0, testBox.getRed());
        assertEquals(0, testBox.getYellow());

        /*from normal AmmoBox*/
        assertThrows(IllegalArgumentException.class,
                () -> normalBox.addAmmo(AmmoCube.ANY));

        /*no action is taken*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(1, normalBox.getYellow());
    }

    /*Testing addAmmo with single cube: max cubes*/
    @Test
    void addAmmo_max() {
        normalBox.addAmmo(AmmoCube.BLUE);
        normalBox.addAmmo(AmmoCube.BLUE);
        normalBox.addAmmo(AmmoCube.RED);
        normalBox.addAmmo(AmmoCube.YELLOW);
        normalBox.addAmmo(AmmoCube.YELLOW);
        normalBox.addAmmo(AmmoCube.YELLOW);

        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(3, normalBox.getYellow());
    }

    /*Testing addAmmo with list: normal(RED, YELLOW, BLUE)*/
    @Test
    void addAmmo_list_normal() {
        List<AmmoCube> list = new ArrayList<>();
        list.add(AmmoCube.RED);
        list.add(AmmoCube.BLUE);
        list.add(AmmoCube.YELLOW);
        list.add(AmmoCube.RED);
        list.add(AmmoCube.YELLOW);
        list.add(AmmoCube.YELLOW);

        testBox.addAmmo(list);

        assertEquals(1, testBox.getBlue());
        assertEquals(2, testBox.getRed());
        assertEquals(3, testBox.getYellow());
    }

    /*Testing addAmmo with list: type ANY*/
    @Test
    void addAmmo_list_any() {
        List<AmmoCube> list = new ArrayList<>();
        list.add(AmmoCube.ANY);

        /*From empty AmmoBox*/
        assertThrows(IllegalArgumentException.class,
                () -> testBox.addAmmo(list));

        /*No action is taken*/
        assertEquals(0, testBox.getBlue());
        assertEquals(0, testBox.getRed());
        assertEquals(0, testBox.getYellow());

        /*If list and AmmoBox contain other elements*/
        list.add(AmmoCube.YELLOW);

        assertThrows(IllegalArgumentException.class,
                () -> normalBox.addAmmo(list));

        /*No action is taken*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(1, normalBox.getYellow());
    }

    /*Testing addAmmo with list: max cubes*/
    @Test
    void addAmmo_list_max() {
        List<AmmoCube> list = new ArrayList<>();
        list.add(AmmoCube.BLUE);
        list.add(AmmoCube.BLUE);
        list.add(AmmoCube.RED);
        list.add(AmmoCube.YELLOW);
        list.add(AmmoCube.YELLOW);
        list.add(AmmoCube.YELLOW);

        normalBox.addAmmo(list);

        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(3, normalBox.getYellow());
    }

    /*Testing ok case, not enough case and ANY*/
    @Test
    void checkPrice_normal() {
        /*Not enough*/
        assertFalse(emptyBox.checkPrice(normalList));

        /*Just enough*/
        assertTrue(normalBox.checkPrice(normalList));

        /*Enough*/
        normalBox.addAmmo(AmmoCube.YELLOW);
        assertTrue(normalBox.checkPrice(normalList));

        /*Just enough with ANY*/
        normalList.add(AmmoCube.ANY);
        assertTrue(normalBox.checkPrice(normalList));

        /*Not enough with ANY*/
        normalList.add(AmmoCube.ANY);
        assertFalse(normalBox.checkPrice(normalList));

        /*No side effects*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(2, normalBox.getYellow());
    }

    /*Testing not enough cubes*/
    @Test
    void pay_notEnough() {
        /*Empty*/
        assertThrows(IllegalArgumentException.class,
                () -> emptyBox.pay(normalList));

        /*Not enough*/
        normalList.add(AmmoCube.YELLOW);
        assertThrows(IllegalArgumentException.class,
                () -> normalBox.pay(normalList));

        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(1, normalBox.getYellow());
    }

    /*Testing ANY*/
    @Test
    void pay_any() {
        /*Adding ANY to normalList and a cube to normalBox so that there is
        enough cubes for paying*/
        normalList.add(AmmoCube.ANY);
        normalBox.addAmmo(AmmoCube.YELLOW);

        assertThrows(IllegalArgumentException.class,
                () -> normalBox.pay(normalList));

        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(2, normalBox.getYellow());
    }

    /*Testing normal behaviour*/
    @Test
    void pay_normal() {
        List<AmmoCube> empty = new ArrayList<>();
        emptyBox.pay(empty);

        normalBox.pay(normalList);
        assertEquals(0, normalBox.getBlue());
        assertEquals(0, normalBox.getRed());
        assertEquals(0, normalBox.getYellow());
    }

    /*Testing null parameter*/
    @Test
    void getMissing_null() {
        assertThrows(NullPointerException.class,
                () -> normalBox.getMissing(null));

        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(1, normalBox.getYellow());
    }

    /*Testing empty parameter*/
    @Test
    void getMissing_empty() {
        Map<AmmoCube, Integer> missing =
                normalBox.getMissing(new ArrayList<>());
        assertEquals(0, missing.get(AmmoCube.BLUE));
        assertEquals(0, missing.get(AmmoCube.RED));
        assertEquals(0, missing.get(AmmoCube.YELLOW));
        assertEquals(0, missing.get(AmmoCube.ANY));


        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(1, normalBox.getYellow());
    }

    /*Testing when there are more cubes than required*/
    @Test
    void getMissing_enough() {
        normalBox.addAmmo(AmmoCube.YELLOW);

        Map<AmmoCube, Integer> missing =
                normalBox.getMissing(normalList);
        assertEquals(0, missing.get(AmmoCube.BLUE));
        assertEquals(0, missing.get(AmmoCube.RED));
        assertEquals(0, missing.get(AmmoCube.YELLOW));
        assertEquals(0, missing.get(AmmoCube.ANY));


        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(2, normalBox.getYellow());
    }

    /*Testing when there are as many cubes as required*/
    @Test
    void getMissing_justEnough() {
        normalBox.addAmmo(AmmoCube.YELLOW);
        normalList.add(AmmoCube.ANY);

        /*Added YELLOW to the box and ANY to the cost, should even out*/
        Map<AmmoCube, Integer> missing =
                normalBox.getMissing(normalList);
        assertEquals(0, missing.get(AmmoCube.BLUE));
        assertEquals(0, missing.get(AmmoCube.RED));
        assertEquals(0, missing.get(AmmoCube.YELLOW));
        assertEquals(0, missing.get(AmmoCube.ANY));


        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(2, normalBox.getYellow());
    }

    /*Testing when there are not enough cubes*/
    @Test
    void getMissing_notEnough() {
        normalList.add(AmmoCube.YELLOW);
        normalList.add(AmmoCube.ANY);

        /*Should be missing a YELLOW and a ANY*/
        Map<AmmoCube, Integer> missing =
                normalBox.getMissing(normalList);
        assertEquals(0, missing.get(AmmoCube.BLUE));
        assertEquals(0, missing.get(AmmoCube.RED));
        assertEquals(1, missing.get(AmmoCube.YELLOW));
        assertEquals(1, missing.get(AmmoCube.ANY));


        /*Checking that it does not modify anything*/
        assertEquals(3, normalBox.getBlue());
        assertEquals(3, normalBox.getRed());
        assertEquals(1, normalBox.getYellow());
    }
}