package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.AmmoCube;

import java.util.List;

/**
 * This class handles the {@link AmmoCube}s for the {@link Player}.
 * There can be at most three cubes of each color and there can not be cubes
 * of type {@code ANY}.
 *
 * @author Abbo Giulio A.
 * @see AmmoCube
 */
public class AmmoBox {

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
     * Instantiates an empty {@code AmmoBox}.
     */
    public AmmoBox() {
        blue = 0;
        red = 0;
        yellow = 0;
    }

    /**
     * The number of blue {@linkplain AmmoCube}s.
     *
     * @return the number of blue {@linkplain AmmoCube}s
     */
    public int getBlue() {
        return blue;
    }

    /**
     * The number of red {@linkplain AmmoCube}s.
     *
     * @return the number of red {@linkplain AmmoCube}s
     */
    public int getRed() {
        return red;
    }

    /**
     * The number of yellow {@linkplain AmmoCube}s.
     *
     * @return the number of yellow {@linkplain AmmoCube}s
     */
    public int getYellow() {
        return yellow;
    }

    /**
     * Adds the {@code cube} {@linkplain AmmoCube} to the {@code AmmoBox}.
     * There can be only three cubes for each color.
     * AmmoCube {@code cube} must be of type {@code BLUE}, {@code RED} or
     * {@code YELLOW}, an {@code ANY} type can not be added.
     *
     * @param cube the cube to be added to the {@code AmmoBox}
     * @throws IllegalArgumentException if {@code cube} is of type {@code ANY}
     */
    public void addAmmo(AmmoCube cube) {
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
     * Adds all the {@linkplain AmmoCube} in the List to the {@code AmmoBox}.
     * There can be only three cubes for each color.
     * All cubes must be of type {@code BLUE}, {@code RED} or {@code YELLOW},
     * an {@code ANY} type can not be added.
     *
     * @param cubes the {@linkplain List} of {@linkplain AmmoCube}s to be added
     * @throws IllegalArgumentException if a {@code cube} is of type {@code ANY}
     */
    public void addAmmo(List<AmmoCube> cubes) {
        for (AmmoCube cube : cubes) {
            addAmmo(cube);
        }
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
     * Checks if there are enough {@linkplain AmmoCube}s to pay the {@code
     * price}.
     *
     * @param price a {@linkplain List} of cubes to be checked
     * @return true if there are more cubes than in the {@code price} list
     */
    public boolean checkPrice(List<AmmoCube> price) {
        int blueLeft = blue - AmmoCube.countBlue(price);
        int redLeft = red - AmmoCube.countRed(price);
        int yellowLeft = yellow - AmmoCube.countYellow(price);
        int anyLeft = (blueLeft + redLeft + yellowLeft) - AmmoCube.countAny(price);
        return (blueLeft >= 0 && redLeft >= 0 && yellowLeft >= 0 && anyLeft >= 0);
    }

    /**
     * Subtracts the {@linkplain AmmoCube}s in the {@code price} from the total.
     * A cube of type {@code ANY} can not be payed, it is necessary to choose
     * what color to use.
     * This first checks if there are enough cubes to pay the {@code price}.
     *
     * @param price a {@linkplain List} of cubes to be payed
     * @throws IllegalArgumentException if a {@code cube} is of type {@code
     * ANY} or there are not enough cubes
     */
    public void pay(List<AmmoCube> price) {
        if (price.contains(AmmoCube.ANY))
            throw new IllegalArgumentException("Must specify how to pay ANY");
        if (checkPrice(price)) {
            blue -= AmmoCube.countBlue(price);
            red -= AmmoCube.countRed(price);
            yellow -= AmmoCube.countYellow(price);
        } else
            throw new IllegalArgumentException("Can not afford price");
    }
}