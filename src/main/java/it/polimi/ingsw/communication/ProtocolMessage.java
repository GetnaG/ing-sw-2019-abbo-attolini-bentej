package it.polimi.ingsw.communication;

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
 * <p>
 * This can also have an optional {@linkplain #message}, a string with
 * information on the command.
 *
 * @author Abbo Giulio A.
 * @see MessageType
 * @see Notification
 * @see Update
 */
class ProtocolMessage {
    /**
     * The type of this message.
     */
    private MessageType command;
    /**
     * Whether this has an attached message.
     */
    private boolean hasMessage;
    /**
     * The attached message if present.
     */
    private String message;
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
    ProtocolMessage(MessageType command) {
        this(command, null, null, null, null);
    }

    /**
     * Creates a message with the provided command and options.
     *
     * @param command the command for this message
     * @param options the available options
     */
    ProtocolMessage(MessageType command, List<? extends List<String>> options) {
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
    ProtocolMessage(MessageType command, String userChoice) {
        this(command, userChoice, null, null, null);
    }

    /**
     * Creates a message with the provided notifications.
     *
     * @param notifications the notifications for this message
     */
    ProtocolMessage(Notification[] notifications) {
        this(MessageType.NOTIFICATION, null, notifications, null, null);
    }

    /**
     * Creates a message with the provided updates.
     *
     * @param updates the updates for this message
     */
    ProtocolMessage(Update[] updates) {
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

        hasMessage = false;
        message = null;
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
    MessageType getCommand() {
        return command;
    }

    /**
     * Returns whether this has a message attached.
     *
     * @return whether this has a message attached
     */
    boolean hasMessage() {
        return hasMessage;
    }

    /**
     * Returns the message attached, null if not present.
     *
     * @return the message attached, null if not present
     */
    String getMessage() {
        return message;
    }

    /**
     * Sets a message for this.
     *
     * @param message the optional message for this.
     */
    void setMessage(String message) {
        hasMessage = true;
        this.message = message;
    }

    /**
     * Returns the notifications, null if not present.
     *
     * @return the notifications, null if not present
     */
    Notification[] getNotifications() {
        return notifications;
    }

    /**
     * Returns the updates, null if not present.
     *
     * @return the updates, null if not present
     */
    Update[] getUpdates() {
        return updates;
    }

    /**
     * Returns the options, null if not present.
     *
     * @return the options, null if not present
     */
    String[][] getOptions() {
        return options;
    }

    /**
     * Returns the user choice.
     * If there are options, this should return the index of the choosen option.
     * Otherwise the user's input is returned.
     * This can return null if no choice was made.
     *
     * @return Returns the user choice
     */
    String getUserChoice() {
        return userChoice;
    }
}