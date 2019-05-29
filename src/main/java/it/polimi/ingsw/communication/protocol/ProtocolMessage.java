package it.polimi.ingsw.communication.protocol;

import java.util.List;

/**
 * This is used in the communication between the client and server.
 * <p>
 * Every message has a {@linkplain #command}:<ul>
 * <li>if it is of type {@link MessageType#NOTIFICATION} this will
 * contain an array of {@link Notification} objects,
 * {@linkplain #notifications}</li>
 * <li>if it is of type {@link MessageType#UPDATE} this will
 * contain an array of {@link Update} objects,
 * {@linkplain #updates}</li>
 * <li>for other values, {@link MessageType#hasOptions()} can be used
 * to determinate whether {@linkplain #options} should contain the
 * available options</li>
 * </ul>
 *
 * @author Abbo Giulio A.
 * @see MessageType
 * @see Notification
 * @see Update
 */
public class ProtocolMessage {
    /**
     * The type of this message.
     */
    private MessageType command;
    /**
     * The user's choice; if there are options this will be the index of the
     * chosen one, otherwise this will be a string containing the user input.
     */
    private String userChoice;
    /**
     * The notifications, if present.
     */
    private Notification[] notifications;
    /**
     * The updates, if present.
     */
    private Update[] updates;
    /**
     * The options, if present.
     */
    private String[][] options;

    /**
     * Creates a message with the provided command and no options.
     *
     * @param command the command for this message
     */
    public ProtocolMessage(MessageType command) {
        this(command, null, null, null, null);
    }

    /**
     * Creates a message with the provided command and options.
     *
     * @param command the command for this message
     * @param options the available options
     */
    public ProtocolMessage(MessageType command, List<? extends List<String>> options) {
        this(command, null, null, null,
                options.stream().map(l -> l.toArray(new String[0]))
                        .toArray(String[][]::new));
    }

    /**
     * Creates a message with the provided command and user choice.
     *
     * @param command    the command for this message
     * @param userChoice the choice of the user
     */
    public ProtocolMessage(MessageType command, String userChoice) {
        this(command, userChoice, null, null, null);
    }

    /**
     * Creates a message with the provided notifications.
     *
     * @param notifications the notifications for this message
     */
    public ProtocolMessage(Notification[] notifications) {
        this(MessageType.NOTIFICATION, null, notifications, null, null);
    }

    /**
     * Creates a message with the provided updates.
     *
     * @param updates the updates for this message
     */
    public ProtocolMessage(Update[] updates) {
        this(MessageType.UPDATE, null, null, updates, null);
    }

    /**
     * A generic constructor, used by the specific ones.
     *
     * @param command       the command for this message
     * @param userChoice    the choice of the user
     * @param notifications the notifications for this message
     * @param updates       the updates for this message
     * @param options       the available options
     */
    private ProtocolMessage(MessageType command,
                            String userChoice, Notification[] notifications,
                            Update[] updates,
                            String[][] options) {
        this.command = command;
        this.userChoice = userChoice;
        this.notifications = notifications;
        this.updates = updates;
        this.options = options;

        if (command.hasOptions() && (options == null || options.length <= 0)
                && userChoice == null)
            throw new IllegalArgumentException("Command has options but none " +
                    "provided or no answer.");
    }

    /**
     * Returns the command of this.
     *
     * @return the command of this
     */
    public MessageType getCommand() {
        return command;
    }

    /**
     * Returns the notifications, null if not present.
     *
     * @return the notifications, null if not present
     */
    public Notification[] getNotifications() {
        return notifications;
    }

    /**
     * Returns the updates, null if not present.
     *
     * @return the updates, null if not present
     */
    public Update[] getUpdates() {
        return updates;
    }

    /**
     * Returns the options, null if not present.
     *
     * @return the options, null if not present
     */
    public String[][] getOptions() {
        return options;
    }

    /**
     * Returns the user choice.
     * If there are options, this should return the index of the chosen option.
     * Otherwise the user's input is returned.
     * This can return null if no choice was made.
     *
     * @return Returns the user choice
     */
    public String getUserChoice() {
        return userChoice;
    }
}
