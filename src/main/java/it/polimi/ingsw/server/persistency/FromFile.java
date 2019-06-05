package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.board.Room;
import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.List;

/**
 * Ensures that the resources from file are loaded in the right order.
 * This class uses classes implementing {@link BasicLoader} as singletons,
 * and ensures that, if necessary, they are instantiated in the right order.
 * <p>
 * The singleton functionality is implemented here instead of in each class,
 * this was done to allow, if necessary, to reuse a loader class for multiple
 * files.
 *
 * @author Abbo Giulio A.
 * @see BasicLoader
 */
public class FromFile {
    /**
     * The position of the json for ammo cards.
     */
    private static final String AMMO_CARDS = "ammocards.json";
    /**
     * The position of the json for powerup cards.
     */
    private static final String POWERUP_CARDS = "powerupcards.json";
    /**
     * The position of the json for weapon cards.
     */
    private static final String WEAPON_CARDS = "weaponcards.json";
    /**
     * The position of the json for the effects.
     */
    private static final String EFFECTS = "effects.json";
    /**
     * The position of the json for the maps.
     */
    private static final String MAPS = "maps.json";

    /**
     * The only instance of an ammo card loader.
     */
    private static AmmoCardLoader ammoCardLoader;
    /**
     * The only instance of a powerup card loader.
     */
    private static PowerupLoader powerupLoader;
    /**
     * The only instance of a weapon card loader.
     */
    private static WeaponLoader weaponLoader;
    /**
     * The only instance of an effects loader.
     */
    private static EffectLoader effectLoader;
    /**
     * The only instance of an effects loader.
     */
    private static MapLoader mapLoader;

    /**
     * Private constructor: this class can not be instantiated.
     */
    private FromFile() {
    }

    /**
     * Returns a basic loader for ammo cards.
     *
     * @return a basic loader for ammo cards
     */
    public static synchronized BasicLoader<AmmoCard> ammoCards() {
        if (ammoCardLoader == null)
            setup();
        return ammoCardLoader;
    }

    /**
     * Returns a basic loader for powerups cards.
     *
     * @return a basic loader for powerup cards
     */
    public static synchronized BasicLoader<PowerupCard> powerups() {
        if (powerupLoader == null)
            setup();
        return powerupLoader;
    }

    /**
     * Returns a basic loader for weapon cards.
     *
     * @return a basic loader for weapon cards
     */
    public static synchronized BasicLoader<WeaponCard> weapons() {
        if (weaponLoader == null)
            setup();
        return weaponLoader;
    }

    /**
     * Returns a basic loader for the effects.
     *
     * @return a basic loader for the effects
     */
    public static synchronized BasicLoader<EffectInterface> effects() {
        if (effectLoader == null)
            setup();
        return effectLoader;
    }

    /**
     * Returns a basic loader for the effects.
     *
     * @return a basic loader for the effects
     */
    public static synchronized BasicLoader<List<Room>> maps() {
        if (mapLoader == null)
            setup();
        return mapLoader;
    }

    /**
     * Loads the  resources from file in the right order.
     */
    private static void setup() {
        effectLoader =
                new EffectLoader(FromFile.class.getResourceAsStream(EFFECTS));
        weaponLoader =
                new WeaponLoader(FromFile.class.getResourceAsStream(WEAPON_CARDS));
        powerupLoader =
                new PowerupLoader(FromFile.class.getResourceAsStream(POWERUP_CARDS));
        ammoCardLoader =
                new AmmoCardLoader(FromFile.class.getResourceAsStream(AMMO_CARDS));
        mapLoader =
                new MapLoader(FromFile.class.getResourceAsStream(MAPS));
    }
}
