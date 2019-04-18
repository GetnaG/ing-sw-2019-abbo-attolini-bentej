package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Abbo Giulio A.
 * Testing: normal use, not valid arguments, null
 */
class HandManagerTest {
    private HandManager normalHand;
    private WeaponCard AWeapon;
    private WeaponCard BWeapon;
    private WeaponCard CWeapon;
    private WeaponCard DWeapon;
    private PowerupCard APowerup;
    private PowerupCard BPowerup;
    private PowerupCard CPowerup;
    private PowerupCard DPowerup;


    @BeforeEach
    void setUp() {
        APowerup = new PowerupCard("Ap", "Effect", AmmoCube.BLUE);
        BPowerup = new PowerupCard("Bp", "Effect", AmmoCube.RED);
        CPowerup = new PowerupCard("Cp", "Effect", AmmoCube.RED);
        DPowerup = new PowerupCard("Dp", "Effect", AmmoCube.YELLOW);
        AWeapon = new WeaponCard("A", Collections.emptyList());
        BWeapon = new WeaponCard("B", Collections.emptyList());
        CWeapon = new WeaponCard("C", Collections.emptyList());
        DWeapon = new WeaponCard("D", Collections.emptyList());
        normalHand = new HandManager();

        normalHand.addPowerup(APowerup);
        normalHand.addPowerup(BPowerup);

        normalHand.addWeaponCard(AWeapon);
        normalHand.addWeaponCard(BWeapon);

        normalHand.unload(BWeapon);
    }

    /*Testing normal usage*/
    @Test
    void addPowerup_normal() {
        /*Can be added*/
        assertTrue(normalHand.addPowerup(CPowerup));

        /*Check it was added*/
        assertTrue(normalHand.getPowerups().contains(CPowerup));

        /*Can not be added*/
        assertFalse(normalHand.addPowerup(DPowerup));

        /*Check it was not added*/
        assertFalse(normalHand.getPowerups().contains(DPowerup));
    }

    /*Testing null parameter*/
    @Test
    void addPowerup_null() {
        assertThrows(NullPointerException.class,
                () -> normalHand.addPowerup(null));

        /*Check status*/
        assertEquals(2, normalHand.getPowerups().size());
    }

    /*Testing normal behaviour*/
    @Test
    void addWeaponCard_normal() {
        assertTrue(normalHand.addWeaponCard(CWeapon));

        /*Check it was added*/
        assertTrue(normalHand.getLoadedWeapons().contains(CWeapon));

        /*Can not be added*/
        assertFalse(normalHand.addWeaponCard(DWeapon));

        /*Check it was not added*/
        assertFalse(normalHand.getLoadedWeapons().contains(DWeapon));
    }

    /*Testing null parameter*/
    @Test
    void addWeaponCard_null() {
        assertThrows(NullPointerException.class,
                () -> normalHand.addWeaponCard(null));

        /*Check status*/
        assertEquals(1, normalHand.getLoadedWeapons().size());
    }

    /*Testing normal*/
    @Test
    void removePowerup_normal() {
        normalHand.removePowerup(BPowerup);

        assertFalse(normalHand.getPowerups().contains(BPowerup));
        assertTrue(normalHand.getPowerups().contains(APowerup));
    }

    /*Testing null*/
    @Test
    void removePowerup_null() {
        assertThrows(NullPointerException.class,
                () -> normalHand.removePowerup(null));

        assertEquals(2, normalHand.getPowerups().size());
    }

    /*Testing if the card is not in the hand*/
    @Test
    void removePowerup_notPresent() {
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.removePowerup(CPowerup));

        assertEquals(2, normalHand.getPowerups().size());
    }

    /*Testing normal*/
    @Test
    void removeWeapon_normal() {
        /*Remove loaded weapon*/
        normalHand.removeWeapon(AWeapon);
        assertFalse(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));

        /*Remove unloaded weapon*/
        normalHand.removeWeapon(BWeapon);
        assertFalse(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing null*/
    @Test
    void removeWeapon_null() {
        assertThrows(NullPointerException.class,
                () -> normalHand.removeWeapon(null));

        assertEquals(1, normalHand.getLoadedWeapons().size());
        assertEquals(1, normalHand.getUnloadedWeapons().size());
    }

    /*Testing if the card is not in the hand*/
    @Test
    void removeWeapon_notPresent() {
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.removeWeapon(CWeapon));

        assertEquals(1, normalHand.getLoadedWeapons().size());
        assertEquals(1, normalHand.getUnloadedWeapons().size());
    }

    /*Testing getters*/
    @Test
    void getPowerups() {
        List<PowerupCard> myPowerups = normalHand.getPowerups();

        assertTrue(myPowerups.contains(APowerup));
        assertTrue(myPowerups.contains(BPowerup));
        assertEquals(2, myPowerups.size());
    }

    @Test
    void getLoadedWeapons() {
        List<WeaponCard> myWeapons = normalHand.getLoadedWeapons();

        assertTrue(myWeapons.contains(AWeapon));
        assertEquals(1, myWeapons.size());
    }

    @Test
    void getUnloadedWeapons() {
        List<WeaponCard> myWeapons = normalHand.getUnloadedWeapons();

        assertTrue(myWeapons.contains(BWeapon));
        assertEquals(1, myWeapons.size());
    }

    /*Testing normal unloading*/
    @Test
    void unload_normal() {
        normalHand.unload(AWeapon);

        assertFalse(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(AWeapon));
    }

    /*Testing with null parameter*/
    @Test
    void unload_null() {
        assertThrows(NullPointerException.class, () -> normalHand.unload(null));

        assertTrue(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing card already unloaded or not in hand*/
    @Test
    void unload_alreadyUnloaded() {
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.unload(BWeapon));
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.unload(CWeapon));

        assertTrue(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing normal reloading*/
    @Test
    void reload_normal() {
        normalHand.reload(BWeapon);

        assertFalse(normalHand.getUnloadedWeapons().contains(BWeapon));
        assertTrue(normalHand.getLoadedWeapons().contains(BWeapon));
    }

    /*Testing with null parameter*/
    @Test
    void reload_null() {
        assertThrows(NullPointerException.class, () -> normalHand.reload(null));

        assertTrue(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing card already loaded or not in hand*/
    @Test
    void reload_alreadyLoaded() {
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.reload(AWeapon));
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.unload(CWeapon));

        assertTrue(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing null parameter*/
    @Test
    void getPowerupsForPaying_null() {
        assertThrows(NullPointerException.class,
                () -> normalHand.getPowerupForPaying(null));

        assertTrue(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing with an empty cost*/
    @Test
    void getPowerupsForPaying_empty() {
        assertThrows(IllegalArgumentException.class,
                () -> normalHand.getPowerupForPaying(new HashMap<>()));

        assertTrue(normalHand.getLoadedWeapons().contains(AWeapon));
        assertTrue(normalHand.getUnloadedWeapons().contains(BWeapon));
    }

    /*Testing normal behaviour*/
    @Test
    void getPowerupsForPaying_normal() {
        Map<AmmoCube, Integer> cost = new EnumMap<>(AmmoCube.class);

        /*Cube not present*/
        cost.put(AmmoCube.YELLOW, 1);
        assertTrue(normalHand.getPowerupForPaying(cost).isEmpty());

        /*One of the cubes not present*/
        cost.put(AmmoCube.BLUE, 1);
        assertTrue(normalHand.getPowerupForPaying(cost).isEmpty());

        /*Cubes ok*/
        cost.clear();
        cost.put(AmmoCube.BLUE, 1);

        assertEquals(1, normalHand.getPowerupForPaying(cost).size());
        assertTrue(normalHand.getPowerupForPaying(cost).contains(APowerup));

        /*Type ANY*/
        cost.put(AmmoCube.ANY, 1);

        assertEquals(2, normalHand.getPowerupForPaying(cost).size());
        assertTrue(normalHand.getPowerupForPaying(cost).contains(APowerup));
        assertTrue(normalHand.getPowerupForPaying(cost).contains(BPowerup));
    }
}