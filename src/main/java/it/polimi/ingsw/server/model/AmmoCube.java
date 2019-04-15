package it.polimi.ingsw.server.model;

import java.util.Collections;
import java.util.List;

/**
 * Enum class used for representing the in-game ammo cube.
 * This can have the three values used in the game or an {@code ANY} value.
 * <p>
 * A price is represented by a list of {@code AmmoCube}.
 * This class provides a set of static methods to count the number of ammo
 * cubes of each color.
 * The {@code ANY} value is used when the price is "a cube of any color".
 *
 * @author Abbo Giulio A.
 */
public enum AmmoCube {
    /**
     * A blue ammo cube
     */
    BLUE,

    /**
     * A yellow ammo cube
     */
    YELLOW,

    /**
     * A red ammo cube
     */
    RED,

    /**
     * A cube of any color
     */
    ANY;

    /**
     * Counts the number of blue ammo cubes in the provided list.
     * That is, returns the number of elements {@linkplain AmmoCube#BLUE} in the
     * {@code toCount} list.
     *
     * @param toCount the {@linkplain List} containing the elements to count
     * @return the number of {@code AmmoCube.BLUE} elements in the list
     * @throws NullPointerException if {@code toCount} is null
     */
    public static int countBlue(List<AmmoCube> toCount) {
        return Collections.frequency(toCount, BLUE);
    }

    /**
     * Counts the number of yellow ammo cubes in the provided list.
     * That is, returns the number of elements {@linkplain AmmoCube#YELLOW} in the
     * {@code toCount} list.
     *
     * @param toCount the {@linkplain List} containing the elements to count
     * @return the number of {@code AmmoCube.YELLOW} elements in the list
     * @throws NullPointerException if {@code toCount} is null
     */
    public static int countYellow(List<AmmoCube> toCount) {
        return Collections.frequency(toCount, YELLOW);
    }

    /**
     * Counts the number of red ammo cubes in the provided list.
     * That is, returns the number of elements {@linkplain AmmoCube#RED} in the
     * {@code toCount} list.
     *
     * @param toCount the {@linkplain List} containing the elements to count
     * @return the number of {@code AmmoCube.RED} elements in the list
     * @throws NullPointerException if {@code toCount} is null
     */
    public static int countRed(List<AmmoCube> toCount) {
        return Collections.frequency(toCount, RED);
    }

    /**
     * Counts the number of {@code ANY} ammo cubes in the provided list.
     * That is, returns the number of elements {@linkplain AmmoCube#ANY} in the
     * {@code toCount} list. Blue, red and yellow cubes do not count as
     * {@code ANY} cubes.
     *
     * @param toCount the {@linkplain List} containing the elements to count
     * @return the number of {@code AmmoCube.ANY} elements in the list
     * @throws NullPointerException if {@code toCount} is null
     */
    public static int countAny(List<AmmoCube> toCount) {
        return Collections.frequency(toCount, ANY);
    }
}
