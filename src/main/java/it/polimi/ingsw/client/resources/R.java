package it.polimi.ingsw.client.resources;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * This allows to use localized resources and retrieve properties.
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
 * <p>
 * This allows classes to load {@link Properties}, the files must be located
 * in a folder at {@linkplain #PROPERTIES_PATH}.
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
     * The folder containing the properties file.
     */
    private static final String PROPERTIES_PATH = "/properties/";
    /**
     * The extension of the image files.
     */
    private static final String IMAGE_EXTENSION = ".png";
    /**
     * The extension of the properties files.
     */
    private static final String PROPERTIES_EXTENSION = ".properties";
    /**
     * The folder containing the fonts.
     */
    private static final String FONTS_PATH = "/fonts/";
    /**
     * The bundle for in-game strings.
     */
    private static ResourceBundle gameBundle;
    /**
     * The bundle for interface labels.
     */
    private static ResourceBundle uiBundle;

    /**
     * Hides the implicit public constructor with a package one.
     */
    R() {
    }

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
     * @throws MissingResourceException if the image is not found
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

    /**
     * Returns the {@linkplain Properties} with the provided name.
     * Callers will handle this files themselves.
     *
     * @param fileName the name of the file without extension
     * @return the properties with the provided name
     * @throws MissingResourceException if the file is not found
     */
    public static synchronized Properties properties(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(R.class.getResourceAsStream(
                    PROPERTIES_PATH + fileName + PROPERTIES_EXTENSION));
        } catch (IOException e) {
            throw new MissingResourceException("Could not find file: " + fileName,
                    R.class.getName(), fileName);
        }
        return properties;
    }

    /**
     * Returns the {@linkplain Font} associated with the provided {@code key}.
     *
     * @param key the identifier for the desired value
     * @return the font associated with the provided key
     * @throws MissingResourceException if the font is not found
     */
    public static synchronized Font font(String key, double fontSize) {
        /*
        Put here code to add localization for images.
        The code should parse the key, replacing the language token.
         */
        try {
            return Font.loadFont(R.class.getResourceAsStream(FONTS_PATH + key), fontSize);
        } catch (NullPointerException e) {
            throw new MissingResourceException("Could not find font " +
                    "resource: " + key, R.class.getName(), key);
        }
    }
}
