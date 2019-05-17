package it.polimi.ingsw.communication;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
class Notification {
    NotificationType type;

    public Notification(NotificationType type) {
        this.type = type;
    }

    enum NotificationType {
        /**
         * Asks the client to close the connection.
         */
        QUIT,
        /**
         * Sends a test message through the socket.
         */
        GREET,

        ERROR,

    }
}
