package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.AbstractCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles the cards for the {@link Player}.
 * The cards can be {@link WeaponCard} or {@link PowerupCard}, and there can
 * be at most {@link #MAX_CARDS} of each type. Weapons can be loaded or
 * unloaded.
 *
 * @author giubots
 * @see AbstractCard
 * @see WeaponCard
 * @see PowerupCard
 */
class HandManager {
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
     * Number of powerups with a blue cube.
     */
    private int bluePowerups;

    /**
     * Number of powerups with a red cube.
     */
    private int redPowerups;

    /**
     * Number of powerups with a yellow cube.
     */
    private int yellowPowerups;

    /**
     * Instantiates a hand manager with no cards.
     */
    HandManager() {
        loadedWeapons = new ArrayList<>();
        unloadedWeapons = new ArrayList<>();
        powerups = new ArrayList<>();
        bluePowerups = 0;
        redPowerups = 0;
        yellowPowerups = 0;
    }

    /**
     * Adds a {@linkplain PowerupCard} to this hand manager.
     * The card can be added only if the total amount does not exceed
     * {@link #MAX_CARDS}.
     *
     * @param card the card to be added
     * @return true if the card was added, false if it could not be added
     * @throws NullPointerException if {@code card} is null
     */
    boolean addPowerup(PowerupCard card) {
        checkNotNull(card);
        if (powerups.size() < MAX_CARDS) {
            powerups.add(card);
            increaseCount(card.getCube());
            return true;
        }
        return false;
    }

    /**
     * Adds a {@linkplain WeaponCard} to the HandManager.
     * The card can be added only if the total amount does not exceed
     * {@link #MAX_CARDS}.
     * When a weapon is added, it is considered loaded and ready to be used.
     *
     * @param card the card to be added
     * @return true if the card was added, false if it could not be added
     * @throws NullPointerException if {@code card} is null
     */
    boolean addWeaponCard(WeaponCard card) {
        checkNotNull(card);
        if (loadedWeapons.size() + unloadedWeapons.size() < MAX_CARDS) {
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
    void removePowerup(PowerupCard card) {
        checkNotNull(card);
        if (powerups.contains(card)) {
            powerups.remove(card);
            decreaseCount(card.getCube());
        } else
            throw new IllegalArgumentException("card not present");
    }

    /**
     * Removes the {@code weapon} from the hand, loaded or unloaded.
     *
     * @param weapon the weapon to be removed
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if {@code weapon} was not in the hand
     *                                  in the first place
     */
    void removeWeapon(WeaponCard weapon) {
        checkNotNull(weapon);
        if (unloadedWeapons.contains(weapon))
            unloadedWeapons.remove(weapon);
        else if (loadedWeapons.contains(weapon))
            loadedWeapons.remove(weapon);
        else
            throw new IllegalArgumentException("card not present");
    }

    /**
     * Returns a list of all the {@linkplain PowerupCard} in the hand.
     * Any change to this list will not affect the HandManager.
     *
     * @return a {@linkplain List} of the {@linkplain PowerupCard} in the hand
     */
    List<PowerupCard> getPowerups() {
        return new ArrayList<>(powerups);
    }

    /**
     * Returns a list of all the loaded {@linkplain WeaponCard} in the hand.
     * Any change to this list will not affect the HandManager.
     *
     * @return a {@linkplain List} of the loaded {@linkplain WeaponCard} in the
     * hand
     */
    List<WeaponCard> getLoadedWeapons() {
        return new ArrayList<>(loadedWeapons);
    }

    /**
     * Returns a list of all the unloaded {@linkplain WeaponCard} in the hand.
     * Any change to this list will not affect the HandManager.
     *
     * @return a {@linkplain List} of the unloaded {@linkplain WeaponCard} in
     * the hand
     */
    List<WeaponCard> getUnloadedWeapons() {
        return new ArrayList<>(unloadedWeapons);
    }

    /**
     * Unloads the specified weapon.
     * An unloaded weapon can not be used.
     *
     * @param weapon the weapon to be unloaded
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if the {@code weapon} was not in the
     *                                  hand or is already unloaded
     */
    void unload(WeaponCard weapon) {
        checkNotNull(weapon);
        if (loadedWeapons.contains(weapon)) {
            loadedWeapons.remove(weapon);
            unloadedWeapons.add(weapon);
        } else
            throw new IllegalArgumentException("card not present or not " +
                    "loaded");
    }

    /**
     * Reloads the specified weapon.
     * A loaded weapon can be used to shoot.
     *
     * @param weapon the weapon to be reloaded
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if the {@code weapon} was not in the
     *                                  hand or is already loaded
     */
    void reload(WeaponCard weapon) {
        checkNotNull(weapon);
        if (unloadedWeapons.contains(weapon)) {
            unloadedWeapons.remove(weapon);
            loadedWeapons.add(weapon);
        } else
            throw new IllegalArgumentException("card not present or not " +
                    "unloaded");
    }

    /**
     * Returns a {@linkplain List} of the cards that can be used for paying.
     * The returned list will contain only the cards that are in the hand and
     * have a cube that could be used for paying the {@code price}.
     * The returned list will be empty if there are not or not enough cards
     * to pay the {@code price}.
     *
     * @param price a {@linkplain List} of cubes to be payed with cards
     * @return the cards that could be used to pay
     * @throws IllegalArgumentException if the {@code price} is empty or
     *                                  contains zero for all its elements
     */
    List<PowerupCard> getPowerupForPaying(Map<AmmoCube, Integer> price) {
        checkEmptyPriceMap(price);
        List<PowerupCard> usable = new ArrayList<>();
        try {

            /*Calculating the remaining cards and adding the usable ones*/
            int blueLeft = getRemainingAndAddPowerups(AmmoCube.BLUE, bluePowerups,
                    price, usable);
            int redLeft = getRemainingAndAddPowerups(AmmoCube.RED, redPowerups,
                    price, usable);
            int yellowLeft = getRemainingAndAddPowerups(AmmoCube.YELLOW,
                    yellowPowerups, price, usable);

            /*If there are cubes of type ANY, and enough cards, returns all
            the cards (any card could be used) else returns an empty list*/
            if (price.containsKey(AmmoCube.ANY) && price.get(AmmoCube.ANY) > 0) {
                if ((blueLeft + redLeft + yellowLeft) < price.get(AmmoCube.ANY))
                    return new ArrayList<>();
                return new ArrayList<>(powerups);
            }
        } catch (IllegalArgumentException e) {

            /*There are not enough cards: returning an empty list*/
            return new ArrayList<>();
        }

        /*Returning only the usable cards*/
        return usable;
    }

    /**
     * Increases the counter for the powerup cards with the specified cube.
     *
     * @param cube the color of the counter to be increased
     */
    private void increaseCount(AmmoCube cube) {
        switch (cube) {
            case BLUE:
                bluePowerups++;
                break;
            case RED:
                redPowerups++;
                break;
            case YELLOW:
                yellowPowerups++;
                break;
            default:
                throw new IllegalArgumentException("Cube type not supported");
        }
    }

    /**
     * Decreases the counter for the powerup cards with the specified cube.
     *
     * @param cube the color of the counter to be decreased
     */
    private void decreaseCount(AmmoCube cube) {
        switch (cube) {
            case BLUE:
                bluePowerups--;
                break;
            case RED:
                redPowerups--;
                break;
            case YELLOW:
                yellowPowerups--;
                break;
            default:
                throw new IllegalArgumentException("Cube type not supported");
        }
    }

    /**
     * Checks that an {@linkplain AbstractCard} is not null and handles that
     * case.
     *
     * @param card the parameter to be tested for null
     */
    private void checkNotNull(AbstractCard card) {
        if (card == null)
            throw new NullPointerException("Passing null card as argument");
    }

    /**
     * Returns the number of cards with the specified color that would remain
     * once the price has been payed. Adds the usable cards to the provided
     * list.
     *
     * @param color        the color of the cards needed
     * @param colorPowerup the number of cards in hand with this {@code color}
     * @param price        a {@linkplain List} of cubes to be payed with cards
     * @param usable       the list where the cards usable for paying are added
     * @return the number of cards the specified color that would be left
     * @throws IllegalArgumentException if there are not enough cards
     */
    private int getRemainingAndAddPowerups(AmmoCube color, int colorPowerup,
                                           Map<AmmoCube, Integer> price,
                                           List<? super PowerupCard> usable) {
        if (price.containsKey(color) && price.get(color) > 0) {
            if (colorPowerup < price.get(color))
                throw new IllegalArgumentException("Not enough powerups");
            for (PowerupCard card : powerups)
                if (card.getCube() == color)
                    usable.add(card);
            return colorPowerup - price.get(color);
        }
        return colorPowerup;
    }

    /**
     * Checks if a price is valid: not null, not empty, non negative values.
     * A price must contain at least one element with a positive amount.
     *
     * @param price the price to be checked
     * @throws NullPointerException     if {@code price} is null
     * @throws IllegalArgumentException if {@code price} can not be accepted
     */
    private void checkEmptyPriceMap(Map<AmmoCube, Integer> price) {
        if (price == null)
            throw new NullPointerException("Null price");
        if (price.isEmpty())
            throw new IllegalArgumentException("Empty price");
        boolean containsPositive = false;
        for (Map.Entry<AmmoCube, Integer> entry : price.entrySet()) {
            if (entry.getValue() < 0)
                throw new IllegalArgumentException("Price can not contain " +
                        "negative values");
            if (entry.getValue() > 0)
                containsPositive = true;
        }
        if (!containsPositive)
            throw new IllegalArgumentException("Price must contain at least " +
                    "one positive amount");
    }
}