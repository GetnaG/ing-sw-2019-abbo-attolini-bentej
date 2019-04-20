package it.polimi.ingsw.server.persistency;

import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

/**
 * Descrizione.
 * <p>
 * Implement checks on the loaded cards?
 *
 * @author Abbo Giulio A.
 */
public class FromFile {
    private static final String AMMO_CARDS =
            "./resources/cards/jsons/ammocards.json";
    private static final String POWERUP_CARDS =
            "./resources/cards/jsons/powerupcards.json";
    private static final String WEAPON_CARDS =
            "./resources/cards/jsons/weaponcards.json";

    private static AmmoCardLoader ammoCardLoader;
    private static PowerupLoader powerupLoader;
    private static WeaponLoader weaponLoader;

    private FromFile() {
    }

    public static synchronized BasicLoader<AmmoCard> ammoCards() {
        if (ammoCardLoader == null)
            ammoCardLoader = new AmmoCardLoader(AMMO_CARDS);
        return ammoCardLoader;
    }

    public static synchronized BasicLoader<PowerupCard> powerups() {
        if (powerupLoader == null)
            powerupLoader = new PowerupLoader(POWERUP_CARDS);
        return powerupLoader;
    }

    public static synchronized BasicLoader<WeaponCard> weapons() {
        if (weaponLoader == null)
            weaponLoader = new WeaponLoader(WEAPON_CARDS);
        return weaponLoader;
    }
}
