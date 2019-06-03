package it.polimi.ingsw.communication.protocol;

import java.io.Serializable;

/**
 * This represents a notification element.
 * Notifications are messages that do not expect an answer and do not modify
 * the game status.
 *
 * @author Abbo Giulio A.
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
        USERNAME_TAKEN_AND_ONLINE
    }
}
