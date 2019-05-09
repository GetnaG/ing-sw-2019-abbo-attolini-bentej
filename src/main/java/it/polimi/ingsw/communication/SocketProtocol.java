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
public enum SocketProtocol {
    /**
     * Choose between sequences of effects.
     */
    EFFECTS_SEQUENCE("effect sequence"),
    /**
     * Choose where to spawn.
     */
    SPAWN("spawn"),
    /**
     * Choose what powerup to use.
     */
    POWERUP("powerup"),
    /**
     * Choose a square.
     */
    DESTINATION("destination"),
    /**
     * Choose a weapon.
     */
    WEAPON("weapon card"),
    /**
     * Choose which weapon to buy.
     */
    WEAPON_TO_BUY("weapon to buy"),
    /**
     * Choose which weapon to discard.
     */
    WEAPON_TO_DISCARD("weapon to discard"),
    /**
     * Choose which weapon to reload.
     */
    WEAPON_TO_RELOAD("weapon to reload"),
    /**
     * Choose an action.
     */
    ACTION("action"),
    /**
     * Choose which powerup to use for paying.
     */
    POWERUP_FOR_PAYING("powerup for paying"),
    /**
     * Choose which tagback card to use.
     */
    USE_TAGBACK("use tagback"),
    /**
     * Choose a target.
     */
    TARGET("target"),
    /**
     * Choose a nickname.
     */
    NICKNAME("choose name"),
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
    PROTOCOL_MULTI("List of lists"),
    /**
     * The last element sent was the last list in the list.
     */
    PROTOCOL_END_MULTI("End list of lists"),

    /**
     * The provided choice was not in the options.
     */
    PROTOCOL_ERR_CHOICE("Error choice");

    /**
     * The string that will be sent through the socket.
     */
    private final String command;

    /**
     * This constructor allows to specify fields for the enum.
     *
     * @param command the string that will be sent.
     */
    SocketProtocol(String command) {
        this.command = command;
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
