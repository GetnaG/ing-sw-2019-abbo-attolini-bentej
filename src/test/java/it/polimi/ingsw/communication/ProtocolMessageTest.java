package it.polimi.ingsw.communication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: normal behaviour and with null.
 */
class ProtocolMessageTest {
    /*Values used for testing.*/
    private final MessageType typeNoOptions = MessageType.NICKNAME;
    private final MessageType typeWithOptions = MessageType.WEAPON;
    private final List<List<String>> options = Arrays.asList(
            Arrays.asList("a"), Arrays.asList("b", "c"));
    private final Notification[] notifications = {new Notification(Notification.NotificationType.QUIT)};
    private final Update[] updates = {new Update()};
    private final String answer = "1";

    /*Tested objects.*/
    private ProtocolMessage noOptions;
    private ProtocolMessage withOptions;
    private ProtocolMessage withNotifications;
    private ProtocolMessage withUpdates;
    private ProtocolMessage withAnswer;

    @BeforeEach
    void setUp() {
        noOptions = new ProtocolMessage(typeNoOptions);
        withOptions = new ProtocolMessage(typeWithOptions, options);
        withNotifications = new ProtocolMessage(notifications);
        withUpdates = new ProtocolMessage((updates));
        withAnswer = new ProtocolMessage(typeWithOptions, answer);
    }

    /*Testing that objects have the right command.*/
    @Test
    void getCommand() {
        assertEquals(typeNoOptions, noOptions.getCommand());
        assertEquals(typeWithOptions, withOptions.getCommand());
        assertEquals(MessageType.NOTIFICATION, withNotifications.getCommand());
        assertEquals(MessageType.UPDATE, withUpdates.getCommand());
        assertEquals(typeWithOptions, withAnswer.getCommand());
    }

    /*Testing notifications*/
    @Test
    void getNotifications() {
        assertNull(noOptions.getNotifications());
        assertNull(withOptions.getNotifications());
        assertEquals(notifications, withNotifications.getNotifications());
        assertNull(withUpdates.getNotifications());
        assertNull(withAnswer.getNotifications());
    }

    /*Testing updates*/
    @Test
    void getUpdates() {
        assertNull(noOptions.getUpdates());
        assertNull(withOptions.getUpdates());
        assertNull(withNotifications.getUpdates());
        assertEquals(updates, withUpdates.getUpdates());
        assertNull(withAnswer.getUpdates());
    }

    /*Testing options*/
    @Test
    void getOptions() {
        assertNull(noOptions.getOptions());
        assertNull(withNotifications.getOptions());
        assertNull(withUpdates.getOptions());
        assertNull(withAnswer.getOptions());

        String[][] actual = withOptions.getOptions();
        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                assertEquals(options.get(i).get(j), actual[i][j]);
            }
        }
    }

    /*Testing if user choice is set*/
    @Test
    void getUserChoice() {
        assertNull(noOptions.getUserChoice());
        assertNull(withOptions.getUserChoice());
        assertNull(withNotifications.getUserChoice());
        assertNull(withUpdates.getUserChoice());
        assertEquals(answer, withAnswer.getUserChoice());
    }
}