package it.polimi.ingsw.communication;

/**
 * This class defines the messages exchanged when using a socket connection.
 * <p>
 * Values of this should be used in the classes that send and receive to and
 * from the socket, instead of the string of the messages.
 * This is done to avoid errors caused by misspelling or changes in the
 * strings, relying on the functionalities of enums.
 * <p>
 * On the beginning of a new communication, the server starts by sending one
 * of the following messages to the client, excluded those which start with
 * {@code PROTOCOL}, then if necessary he can list the elements to choose from,
 * starting with {@linkplain #PROTOCOL_LIST} and ending with
 * {@linkplain #PROTOCOL_END_LIST}. The elements sent must be strings that the
 * client will use to retrieve some resources if necessary.
 * The client will answer with one of the provided strings.
 * <p>
 * If a choice contains more than one element, then the server will put the
 * choices between {@linkplain #PROTOCOL_MULTI} and
 * {@linkplain #PROTOCOL_END_MULTI}, and the elements of a single option
 * between {@linkplain #PROTOCOL_LIST} and {@linkplain #PROTOCOL_END_LIST};
 * the client will answer with the index of the selected option.
 * For example if the client has to choose between {a, b} and {c, b}, the
 * server will send:<ul>
 * <li>{@linkplain #PROTOCOL_MULTI}</li>
 * <li>{@linkplain #PROTOCOL_LIST}</li>
 * <li>"a"</li>
 * <li>"b"</li>
 * <li>{@linkplain #PROTOCOL_END_LIST}</li>
 * <li>{@linkplain #PROTOCOL_LIST}</li>
 * <li>"c"</li>
 * <li>"b"</li>
 * <li>{@linkplain #PROTOCOL_END_LIST}</li>
 * <li>{@linkplain #PROTOCOL_END_MULTI}</li>
 * </ul> And the client will answer with "1" to choose {c, b};
 *
 * @author Abbo Giulio A.
 */
public enum Type {
    /**
     * Choose between sequences of effects.
     */
    EFFECTS_SEQUENCE("Please choose an effect sequence."),
    /**
     * Choose where to spawn.
     */
    SPAWN("Please choose a card to use for spawning."),
    /**
     * Choose what powerup to use.
     */
    POWERUP("Please choose a powerup"),
    /**
     * Choose a square.
     */
    DESTINATION("Please choose a square."),
    /**
     * Choose a weapon.
     */
    WEAPON("Please choose a weapon."),
    /**
     * Choose which weapon to buy.
     */
    WEAPON_TO_BUY("Please choose a weapon to be bought."),
    /**
     * Choose which weapon to discard.
     */
    WEAPON_TO_DISCARD("Please choose a weapon to be discarded."),
    /**
     * Choose which weapon to reload.
     */
    WEAPON_TO_RELOAD("Please choose a weapon to be reloaded."),
    /**
     * Choose an action.
     */
    ACTION("Please choose an action."),
    /**
     * Choose which powerup to use for paying.
     */
    POWERUP_FOR_PAYING("Please choose a powerup to cover the cost."),
    /**
     * Choose which tagback card to use.
     */
    USE_TAGBACK("Please choose which tagback to use."),
    /**
     * Choose a target.
     */
    TARGET("Please select a target."),
    /**
     * Choose a nickname.
     */
    NICKNAME("Please choose a nickname."),
    /**
     * Elements sent after this are part of a list.
     */
    PROTOCOL_LIST("Choose one of the following:"),
    /**
     * The last element sent was the last in the list.
     */
    PROTOCOL_END_LIST("End of the list."),
    /**
     * Elements sent after this are part of a list of list.
     * The client must return the index of the choice.
     */
    PROTOCOL_MULTI("Element:"),
    /**
     * The last element sent was the last list in the list.
     */
    PROTOCOL_END_MULTI("End of the element."),
    /**
     * The provided choice was not in the options or was not valid.
     */
    PROTOCOL_ERR_CHOICE("Choice not valid!"),
    /**
     * Asks the client to close the connection.
     */
    QUIT("Quit"),
    /**
     * Sends a test message through the socket.
     */
    PROTOCOL_GREET("Hello! You are successfully connected.");

    /**
     * The string that will be sent through the socket.
     */
    private final String command;

    /**
     * This constructor allows to specify fields for the enum.
     *
     * @param command the string that will be sent.
     */
    Type(String command) {
        this.command = command;
    }

    /**
     * Returns the instance of this class with the provided command.
     *
     * @param command the command required
     * @return the instance of this class with the provided command
     * @throws IllegalArgumentException if no elements have the provided command
     */
    public static Type with(String command) {
        for (Type p : Type.values())
            if (p.getCommand().equals(command))
                return p;
        throw new IllegalArgumentException("Can not find Type: " + command);
    }

    /**
     * Returns the string associated with this constant.
     *
     * @return the string associated with this
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the string associated with this constant
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return getCommand();
    }
}
