package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Author: Abbo Giulio A.
 * Testing: the getter for the type.
 */
class NotificationTest {

    /*Testing that the return value is right*/
    @Test
    void getType_normal() {
        Notification notification =
                new Notification(Notification.NotificationType.QUIT);
        assertEquals(Notification.NotificationType.QUIT, notification.getType());
    }

    /*Testing that the return value is null*/
    @Test
    void getType_null() {
        Notification notification = new Notification(null);
        assertNull(notification.getType());
    }
}