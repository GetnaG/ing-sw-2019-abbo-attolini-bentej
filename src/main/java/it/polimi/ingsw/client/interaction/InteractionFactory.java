package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.interaction.GUI.GUI;
import it.polimi.ingsw.client.interaction.GUI.SyncGUI;
import it.polimi.ingsw.client.resources.R;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;


/**
 * Chooses between GUI and CLI, based on a parameter if present, or a
 * property file.
 *
 * @author Abbo Giulio A.
 * @see GUI
 * @see CLI
 */
public class InteractionFactory {
    /**
     * The argument or property value to select a GUI.
     */
    private static final String GUI_ARG = "gui";
    /**
     * The argument or property value to select a CLI.
     */
    private static final String CLI_ARG = "cli";

    /**
     * Returns the appropriate interaction interface.
     *
     * @param lineArgs the arguments from the command line
     * @return the appropriate interaction interface
     * @throws IOException              if there are no command line
     *                                  arguments and the properties file
     *                                  could not be loaded
     * @throws NullPointerException     if there are no command line arguments
     *                                  and the property key could not be loaded
     * @throws IllegalArgumentException if the line argument or property
     *                                  value is not valid
     */
    public static InteractionInterface getInteractionInterface(String[] lineArgs)
            throws IOException {

        String preference;
        if (lineArgs.length < 1) {

            /*No arguments: defaulting to properties file*/
            try {
                Properties properties = R.properties("interface");
                preference = properties.getProperty("preferredInterface");
                if (preference == null)
                    throw new NullPointerException("Could not find the " +
                            "following key in the properties: interface");
            } catch (MissingResourceException e) {
                throw new IOException("There are no command line" +
                        " arguments and the interaction preferences could not" +
                        " be loaded");
            }
        } else {

            /*There are arguments: setting preference*/
            preference = lineArgs[0];
        }

        /*Handling preference*/
        switch (preference) {
            case GUI_ARG:
                return new SyncGUI();
            case CLI_ARG:
                return new CLI(System.out, System.in);
            default:
                throw new IllegalArgumentException("Expected argument: " +
                        "\"" + GUI_ARG + "\" or \"" + CLI_ARG + "\"" +
                        " (no quotation marks)");
        }
    }
}
