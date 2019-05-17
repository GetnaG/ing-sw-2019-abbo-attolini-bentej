package it.polimi.ingsw.communication;

import java.util.List;

/**
 *
 */
public class ProtocolMessage {
    private ProtocolType command;
    private boolean hasMessage;
    private String message;
    private String userChoice;
    private Notification[] notifications;
    private Update[] updates;
    private String[][] options;


    public ProtocolMessage(ProtocolType command) {
        this(command, null, null, null, null);
    }

    public ProtocolMessage(ProtocolType command, List<List<String>> options) {
        this(command, null, null, null,
                (String[][]) options.stream().map(List::toArray).toArray());
    }

    public ProtocolMessage(ProtocolType command, String userChoice) {
        this(command, userChoice, null, null, null);
    }

    public ProtocolMessage(Notification[] notifications) {
        this(ProtocolType.NOTIFICATION, null, notifications, null, null);
    }

    public ProtocolMessage(Update[] updates) {
        this(ProtocolType.UPDATE, null, null, updates, null);
    }

    private ProtocolMessage(ProtocolType command,
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
        if (command.hasOptions() && (options == null || options.length<=0))
            throw new IllegalArgumentException("Command has options but none " +
                    "provided.");
    }

    public void setMessage(String message) {
        hasMessage = true;
        this.message = message;
    }

    public ProtocolType getCommand() {
        return command;
    }

    public boolean hasMessage() {
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

    public String getUserChoice() {
        return userChoice;
    }
}
