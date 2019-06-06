package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.AmmoCube;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the {@link AmmoCube}s for the {@link Player}.
 * There can be at most {@link #MAX_CUBES} cubes of each color and there
 * can not be cubes of type {@link AmmoCube#ANY}.
 *
 * @author Abbo Giulio A.
 * @see AmmoCube
 */
class AmmoBox {

    /**
     * The maximum number of cubes of each color.
     */
    private static final int MAX_CUBES = 3;

    /**
     * The number of blue cubes.
     */
    private int blue;

    /**
     * The number of red cubes.
     */
    private int red;

    /**
     * The number of yellow cubes.
     */
    private int yellow;

    /**
     * Instantiates an empty ammo box.
     */
    AmmoBox() {
        blue = 0;
        red = 0;
        yellow = 0;
    }

    /**
     * The number of blue {@linkplain AmmoCube}s.
     *
     * @return the number of blue {@linkplain AmmoCube}s
     */
    int getBlue() {
        return blue;
    }

    /**
     * The number of red {@linkplain AmmoCube}s.
     *
     * @return the number of red {@linkplain AmmoCube}s
     */
    int getRed() {
        return red;
    }

    /**
     * The number of yellow {@linkplain AmmoCube}s.
     *
     * @return the number of yellow {@linkplain AmmoCube}s
     */
    int getYellow() {
        return yellow;
    }

    /**
     * Adds the {@code cube} {@linkplain AmmoCube} to this ammo box.
     * There can be only {@link #MAX_CUBES} cubes for each color.
     * The {@code cube} must be of type {@code BLUE}, {@code RED} or
     * {@code YELLOW}; an {@code ANY} type can not be added.
     *
     * @param cube the cube to be added to this ammo box
     * @throws IllegalArgumentException if {@code cube} is of type {@code ANY}
     */
    void addAmmo(AmmoCube cube) {
        switch (cube) {
            case BLUE:
                if (checkApplicable(blue))
                    blue++;
                break;
            case RED:
                if (checkApplicable(red))
                    red++;
                break;
            case YELLOW:
                if (checkApplicable(yellow))
                    yellow++;
                break;
            default:
                throw new IllegalArgumentException("Only Blue, Red and Yellow" +
                        " AmmoCube are accepted");
        }
    }

    /**
     * Adds all the {@linkplain AmmoCube}s in {@code cubes} to this ammo box.
     * There can be only {@link #MAX_CUBES} cubes for each color.
     * All cubes must be of type {@code BLUE}, {@code RED} or
     * {@code YELLOW}; an {@code ANY} type can not be added.
     *
     * @param cubes the {@linkplain List} of {@linkplain AmmoCube}s to be added
     * @throws IllegalArgumentException if a {@code cube} is of type {@code ANY}
     */
    void addAmmo(List<AmmoCube> cubes) {
        for (AmmoCube cube : cubes) {
            addAmmo(cube);
        }
    }

    /**
     * Checks if there are enough {@linkplain AmmoCube}s to pay the {@code
     * price}.
     * The price is a {@linkplain List} of cubes, each element counts as one
     * cube to be payed.
     *
     * @param price a {@linkplain List} of cubes to be checked
     * @return true if there are more cubes than in the {@code price} list
     */
    boolean checkPrice(List<AmmoCube> price) {
        int[] left = getLeft(price);
        return (left[0] >= 0 && left[1] >= 0 && left[2] >= 0 && left[3] >= 0);
    }

    /**
     * Subtracts the {@linkplain AmmoCube}s in the {@code price} from the total.
     * A cube of type {@code ANY} can not be payed, first it is necessary to
     * choose what color to use.
     * This first checks if there are enough cubes to pay the {@code price}.
     *
     * @param price a {@linkplain List} of cubes to be payed
     * @throws IllegalArgumentException if a {@code cube} is of type {@code
     *                                  ANY} or if there are not enough cubes
     */
    void pay(List<AmmoCube> price) {
        if (price.contains(AmmoCube.ANY))
            throw new IllegalArgumentException("Must specify how to pay ANY");
        if (checkPrice(price)) {
            blue -= AmmoCube.countBlue(price);
            red -= AmmoCube.countRed(price);
            yellow -= AmmoCube.countYellow(price);
        } else
            throw new IllegalArgumentException("Can not afford price");
    }

    /**
     * Returns the cubes left to be payed, if there are not enough cubes to
     * pay the {@code price}.
     *
     * @param price a {@linkplain List} of cubes to be payed
     * @return a {@linkplain Map} with the cube colors and the amount left to be
     * payed
     */
    Map<AmmoCube, Integer> getMissing(List<AmmoCube> price) {
        int[] left = getLeft(price);
        int temp;
        Map<AmmoCube, Integer> remaining = new EnumMap<>(AmmoCube.class);

        /*Using temp to store the amount and adding it if positive, 0
        otherwise*/
        temp = AmmoCube.countBlue(price) - blue;
        remaining.put(AmmoCube.BLUE, temp > 0 ? temp : 0);
        temp = AmmoCube.countRed(price) - red;
        remaining.put(AmmoCube.RED, temp > 0 ? temp : 0);
        temp = AmmoCube.countYellow(price) - yellow;
        remaining.put(AmmoCube.YELLOW, temp > 0 ? temp : 0);
        temp = AmmoCube.countAny(price) - (
                (left[0] > 0 ? left[0] : 0) +
                        (left[1] > 0 ? left[1] : 0) +
                        (left[2] > 0 ? left[2] : 0));
        remaining.put(AmmoCube.ANY, temp > 0 ? temp : 0);
        return remaining;
    }

    /**
     * Checks if a cube can be added.
     *
     * @param toBeIncreased the number of {@linkplain AmmoCube} already present
     * @return true if another {@linkplain AmmoCube} of that color can be added
     */
    private boolean checkApplicable(int toBeIncreased) {
        return toBeIncreased < MAX_CUBES;
    }

    /**
     * The number of cubes that would be left in the box if the price was payed.
     * This returns an array with the number of (in order) BLUE, RED, YELLOW,
     * ANY cubes left. The cost of a cube of type ANY is subtracted from the
     * remaining cubes of the other colors.
     * The array will contain negative numbers if the cubes in this ammo box
     * are not sufficient for paying the {@code price}.
     *
     * @param price a {@linkplain List} of cubes to be payed
     * @return an array with the BLUE, RED, YELLOW and ANY cubes left
     */
    private int[] getLeft(List<AmmoCube> price) {
        int blueLeft = blue - AmmoCube.countBlue(price);
        int redLeft = red - AmmoCube.countRed(price);
        int yellowLeft = yellow - AmmoCube.countYellow(price);
        int anyLeft = ((blueLeft > 0 ? blueLeft : 0) +
                (redLeft > 0 ? redLeft : 0) +
                (yellowLeft > 0 ? yellowLeft : 0) - AmmoCube.countAny(price));
        return new int[]{blueLeft, redLeft, yellowLeft, anyLeft};
    }
}