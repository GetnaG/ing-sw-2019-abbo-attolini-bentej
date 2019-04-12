package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.cards.AbstractCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the cards for the {@link Player}.
 * The cards can be {@link WeaponCard} or {@link PowerupCard}, and there can
 * be at most three of each type. Weapons can be loaded or unloaded.
 *
 * @author Abbo Giulio A.
 * @see AbstractCard
 * @see WeaponCard
 * @see PowerupCard
 */
public class HandManager {
    /**
     * The maximum number of weapon and powerup cards.
     */
    private static final int MAX_CARDS = 3;

    /**
     * Weapons that are ready to be used.
     */
    private List<WeaponCard> loadedWeapons;

    /**
     * Weapons that must be reloaded before they can be used.
     */
    private List<WeaponCard> unloadedWeapons;

    /**
     * Available powerups.
     */
    private List<PowerupCard> powerups;

    /**
     * Instantiates an HandManager with no cards.
     */
    public HandManager() {
        loadedWeapons = new ArrayList<>();
        unloadedWeapons = new ArrayList<>();
        powerups = new ArrayList<>();
    }

    /**
     * Adds a {@linkplain PowerupCard} to the HandManager.
     * The card can be added only if the total amount does not exceed three.
     *
     * @param card the card to be added
     * @return true if the card was added, false if it could not be added
     * @throws NullPointerException if {@code card} is null
     */
    public boolean addPowerup(PowerupCard card) {
        if (checkNotNull(card) && powerups.size() < MAX_CARDS) {
            powerups.add(card);
            return true;
        }
        return false;
    }

    /**
     * Adds a {@linkplain WeaponCard} to the HandManager.
     * The card can be added only if the total amount does not exceed three.
     * When a weapon is added, it is considered loaded and ready to be used.
     *
     * @param card the card to be added
     * @return true if the card was added, false if it could not be added
     * @throws NullPointerException if {@code card} is null
     */
    public boolean addWeaponCard(WeaponCard card) {
        if (checkNotNull(card) &&
                loadedWeapons.size() + unloadedWeapons.size() < MAX_CARDS) {
            loadedWeapons.add(card);
            return true;
        }
        return false;
    }

    /**
     * Removes the {@code card} from the hand.
     *
     * @param card the card to be removed
     * @throws NullPointerException     if {@code card} is null
     * @throws IllegalArgumentException if {@code card} was not in the hand
     *                                  in the first place
     */
    public void removePowerup(PowerupCard card) {
        if (checkNotNull(card)) {
            if (powerups.contains(card))
                powerups.remove(card);
            else
                throw new IllegalArgumentException("card not present");
        }
    }

    /**
     * Removes the {@code weapon} from the hand, loaded or unloaded.
     *
     * @param weapon the weapon to be removed
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if {@code weapon} was not in the hand
     *                                  in the first place
     */
    public void removeWeapon(WeaponCard weapon) {
        if (checkNotNull(weapon)) {
            if (unloadedWeapons.contains(weapon))
                unloadedWeapons.remove(weapon);
            else if (loadedWeapons.contains(weapon))
                loadedWeapons.remove(weapon);
            else
                throw new IllegalArgumentException("card not present");
        }
    }

    /**
     * Returns a list of all the {@linkplain PowerupCard} in the hand.
     * Any change to this list will not affect the HandManager.
     *
     * @return a {@linkplain List} of the {@linkplain PowerupCard} in the hand
     */
    public List<PowerupCard> getPowerups() {
        return List.copyOf(powerups);
    }

    /**
     * Returns a list of all the loaded {@linkplain WeaponCard} in the hand.
     * Any change to this list will not affect the HandManager.
     *
     * @return a {@linkplain List} of the loaded {@linkplain WeaponCard} in the
     * hand
     */
    public List<WeaponCard> getLoadedWeapons() {
        return List.copyOf(loadedWeapons);
    }

    /**
     * Returns a list of all the unloaded {@linkplain WeaponCard} in the hand.
     * Any change to this list will not affect the HandManager.
     *
     * @return a {@linkplain List} of the unloaded {@linkplain WeaponCard} in
     * the hand
     */
    public List<WeaponCard> getUnloadedWeapons() {
        return List.copyOf(unloadedWeapons);
    }

    /**
     * Unloads the specified weapon.
     * An unloaded weapon can not be used.
     *
     * @param weapon the weapon to be unloaded
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if the {@code weapon} was not in the
     *                                  hand or is already unloaded.
     */
    public void unload(WeaponCard weapon) {
        if (checkNotNull(weapon)) {
            if (loadedWeapons.contains(weapon) &&
                    !unloadedWeapons.contains(weapon)) {
                loadedWeapons.remove(weapon);
                unloadedWeapons.add(weapon);
            } else
                throw new IllegalArgumentException("card not present or not " +
                        "loaded");
        }
    }

    /**
     * Reloads the specified weapon.
     * A loaded weapon can be used to shoot.
     *
     * @param weapon the weapon to be reloaded
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if the {@code weapon} was not in the
     *                                  hand or is already loaded.
     */
    public void reload(WeaponCard weapon) {
        if (checkNotNull(weapon)) {
            if (unloadedWeapons.contains(weapon) &&
                    !loadedWeapons.contains(weapon)) {
                unloadedWeapons.remove(weapon);
                loadedWeapons.add(weapon);
            } else
                throw new IllegalArgumentException("card not present or not " +
                        "unloaded");
        }
    }

    /**
     * Checks if an {@linkplain AbstractCard} is not null and handles that case.
     * <p>
     * <i> Implementation note: this implementation throws an exception if
     * the card is null, meaning that it never returns false. Warnings
     * regarding this behaviour are ignored.</i>
     *
     * @param card the parameter to be tested for null
     * @return true if {@code card} is not null
     */
    private boolean checkNotNull(AbstractCard card) {
        if (card == null) {
            throw new NullPointerException("Passing null card as argument");
        }
        return true;
    }
}