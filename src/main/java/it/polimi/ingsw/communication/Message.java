package it.polimi.ingsw.communication;

import java.util.List;

/**
 *
 */
public class Message {
    private Type command;
    private boolean hasMessage;
    private String message;
    private int answer;
    private Notification[] notifications;
    private Update[] updates;
    private String[][] options;

    Message(Type command, List<? extends List<String>> options) {
        this.command = command;
        this.options = (String[][]) options.stream().map(List::toArray).toArray();
    }

    public Message(Type command, boolean hasMessage, String message,
                   Notification[] notifications, Update[] updates, String[][] options) {
        this.command = command;
        this.hasMessage = hasMessage;
        this.message = message;
        this.notifications = notifications;
        this.updates = updates;
        this.options = options;
    }

    public Type getCommand() {
        return command;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public String getMessage() {
        return message;
    }

    public Notification[] getNotifications() {
        return notifications;
    }

    public Update[] getUpdates() {
        return updates;
    }

    public String[][] getOptions() {
        return options;
    }

    public int getAnswer() {
        return answer;
    }

    private class Notification {
        NotificationType type;
    }

    private class Update{
        NotificationUpdate type;
        String newValue;
    }

    private enum NotificationType {
    }

    private enum NotificationUpdate {
    }

}
