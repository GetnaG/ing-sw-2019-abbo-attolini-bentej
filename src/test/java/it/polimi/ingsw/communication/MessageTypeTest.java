package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Author: Abbo Giulio A.
 * Testing: just the getter for hasOptions.
 */
class MessageTypeTest {

    /*Testing that just the following types do not have options*/
    @Test
    void hasOptions() {
        for (MessageType type : MessageType.values())
            assertTrue(type.hasOptions() ||
                    type == MessageType.NICKNAME ||
                    type == MessageType.NOTIFICATION ||
                    type == MessageType.UPDATE);
    }
}