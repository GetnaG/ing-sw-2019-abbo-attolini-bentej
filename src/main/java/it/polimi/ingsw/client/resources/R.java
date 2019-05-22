package it.polimi.ingsw.client.resources;

import javafx.scene.image.Image;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This allows to use localized resources.
 * Resources are organized in key - value pairs; the keys are always
 * strings, the values can be strings or {@link Image}s.
 * <p>
 * This expects to find in the root folder two properties files, localized
 * the same way as expected by a {@link ResourceBundle}:
 * {@linkplain #GAME_STRINGS} for in-game strings and
 * {@linkplain #UI_STRINGS} for the labels used by the interfaces.
 * This also expects to find another folder, {@linkplain #IMAGES_PATH},
 * containing the images with their key as name and the extension as
 * {@linkplain #IMAGE_EXTENSION}.
 *
 * @author Abbo Giulio A.
 * @see Image
 * @see ResourceBundle
 */
public class R {
    /**
     * The position and name of the properties file for in-game strings.
     */
    private static final String GAME_STRINGS = "strings/GameStrings";
    /**
     * The position and name of the properties file for interface labels.
     */
    private static final String UI_STRINGS = "strings/LabelsBundle";
    /**
     * The folder containing the images.
     */
    private static final String IMAGES_PATH = "/images/";
    /**
     * The extension of the image files.
     */
    private static final String IMAGE_EXTENSION = ".png";
    /**
     * The bundle for in-game strings.
     */
    private static ResourceBundle gameBundle;
    /**
     * The bundle for interface labels.
     */
    private static ResourceBundle uiBundle;

    /**
     * Returns the localized value associated with the provided {@code key}.
     * The values are localized using the system settings.
     *
     * @param key the identifier for the desired value
     * @return the localized value associated with the provided key
     * @throws MissingResourceException if the key is not found in neither of
     *                                  the properties files
     */
    public static synchronized String string(String key) {
        /*Initializing*/
        if (gameBundle == null || uiBundle == null) {
            gameBundle = ResourceBundle.getBundle(GAME_STRINGS);
            uiBundle = ResourceBundle.getBundle(UI_STRINGS);
        }

        /*Retrieving*/
        if (gameBundle.containsKey(key))
            return gameBundle.getString(key);
        if (uiBundle.containsKey(key))
            return uiBundle.getString(key);
        throw new MissingResourceException("Could not find string resource: " + key,
                R.class.getName(), key);
    }

    /**
     * Returns the {@linkplain Image} associated with the provided {@code key}.
     *
     * @param key the identifier for the desired value
     * @return the image associated with the provided key
     */
    public static synchronized Image image(String key) {
        /*
        Put here code to add localization for images.
        The code should parse the key, replacing the language token.
         */
        try {
            return new Image(R.class.getResourceAsStream(
                    IMAGES_PATH + key + IMAGE_EXTENSION));
        } catch (NullPointerException e) {
            throw new MissingResourceException("Could not find image " +
                    "resource: " + key, R.class.getName(), key);
        }
    }
}
