package it.polimi.ingsw.communication;

/**
 *
 */
public class Message {
    private Type command;
    private boolean hasMessage;
    private String message;
    private Notification[] notifications;
    private Update[] updates;
    private String[][] options;

    class Notification {
        NotificationType type;
    }

    class Update{
        NotificationUpdate type;
        String newValue;
    }

    enum NotificationType {
    }

    enum NotificationUpdate {
    }
}
