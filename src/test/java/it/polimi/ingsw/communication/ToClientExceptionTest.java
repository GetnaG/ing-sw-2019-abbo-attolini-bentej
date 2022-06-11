package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Author: giubots
 * Testing: correct behaviour of the constructors.
 */
class ToClientExceptionTest {
    private static final String MESSAGE = "Test message";

    /*Testing constructor with only message*/
    @Test
    void constructor_message() {
        ToClientException e = new ToClientException("test message");

        assertTrue(("Communication exception, " + MESSAGE).equalsIgnoreCase(e.getMessage()));
    }

    /*Testing constructor with message and cause*/
    @Test
    void constructor_cause() {
        Exception cause = new Exception();
        ToClientException e = new ToClientException("test message", cause);

        assertTrue(("Communication exception, " + MESSAGE).equalsIgnoreCase(e.getMessage()));
        assertEquals(cause, e.getCause());
    }
}