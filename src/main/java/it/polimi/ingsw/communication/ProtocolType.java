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
public enum ProtocolType {
    /**
     * Choose between sequences of effects.
     */
    EFFECTS_SEQUENCE("Please choose an effect sequence.", true),
    /**
     * Choose where to spawn.
     */
    SPAWN("Please choose a card to use for spawning.", true),
    /**
     * Choose what powerup to use.
     */
    POWERUP("Please choose a powerup", true),
    /**
     * Choose a square.
     */
    DESTINATION("Please choose a square.", true),
    /**
     * Choose a weapon.
     */
    WEAPON("Please choose a weapon.", true),
    /**
     * Choose which weapon to buy.
     */
    WEAPON_TO_BUY("Please choose a weapon to be bought.", true),
    /**
     * Choose which weapon to discard.
     */
    WEAPON_TO_DISCARD("Please choose a weapon to be discarded.", true),
    /**
     * Choose which weapon to reload.
     */
    WEAPON_TO_RELOAD("Please choose a weapon to be reloaded.", true),
    /**
     * Choose an action.
     */
    ACTION("Please choose an action.", true),
    /**
     * Choose which powerup to use for paying.
     */
    POWERUP_FOR_PAYING("Please choose a powerup to cover the cost.", true),
    /**
     * Choose which tagback card to use.
     */
    USE_TAGBACK("Please choose which tagback to use.", true),
    /**
     * Choose a target.
     */
    TARGET("Please select a target.", true),
    /**
     * Choose a nickname.
     */
    NICKNAME("Please choose a nickname.", false),

    UPDATE("Update", false),

    NOTIFICATION("Notification", false);
    /**
     * The string that will be sent through the socket.
     */
    private final String command;

    private final boolean hasOptions;

    /**
     * This constructor allows to specify fields for the enum.
     *
     * @param command the string that will be sent.
     */
    ProtocolType(String command, boolean hasOptions) {
        this.command = command;
        this.hasOptions = hasOptions;
    }

    /**
     * Returns the instance of this class with the provided command.
     *
     * @param command the command required
     * @return the instance of this class with the provided command
     * @throws IllegalArgumentException if no elements have the provided command
     */
    public static ProtocolType with(String command) {
        for (ProtocolType p : ProtocolType.values())
            if (p.getCommand().equals(command))
                return p;
        throw new IllegalArgumentException("Can not find ProtocolType: " + command);
    }

    /**
     * Returns the string associated with this constant.
     *
     * @return the string associated with this
     */
    public String getCommand() {
        return command;
    }

    public boolean hasOptions() {
        return hasOptions;
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
