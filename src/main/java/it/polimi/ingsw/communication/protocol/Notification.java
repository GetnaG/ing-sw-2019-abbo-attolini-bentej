package it.polimi.ingsw.communication.protocol;

import java.io.Serializable;

/**
 * This represents a notification element.
 * Notifications are messages that do not expect an answer and do not modify
 * the game status.
 *
 * @author giubots
 * @see ProtocolMessage
 */
public class Notification implements Serializable {
    /**
     * The type of this notification.
     */
    private NotificationType type;

    /**
     * Constructs a notification with the specified type.
     *
     * @param type the type of this notification.
     */
    public Notification(NotificationType type) {
        this.type = type;
    }

    /**
     * Returns the type of this notification.
     *
     * @return the type of this notification
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * The possible types of notification.
     */
    public enum NotificationType {
        /**
         * Asks the client to close the connection.
         */
        QUIT,
        /**
         * Sends a test message through the socket.
         */
        GREET,
        /**
         * Notifies the client that the answer was not valid.
         */
        ERROR,
        /**
         * Notifies the client that the answer is valid.
         */
        OK,
        /**
         * Notifies that the username is available.
         */
        USERNAME_AVAILABLE,
        /**
         * Notifies that the username is taken and offline.
         */
        USERNAME_TAKEN_AND_OFFLINE,
        /**
         * Notifies that the username is taken and online.
         */
        USERNAME_TAKEN_AND_ONLINE,
        /**
         * Notifies that a user will skip his turn.
         */
        USER_WILL_SKIP,
        /**
         * Notifies that the timer for the match is starting.
         */
        TIMER_STARTING,
        /**
         * Notifies that the timer for the match has stopped.
         */
        TIMER_STOPPED,
        /**
         * Notifies that the match is starting.
         */
        GAME_STARTING,
        /**
         * Notifies all the players that the game is over.
         */
        GAME_OVER,
    }
}
