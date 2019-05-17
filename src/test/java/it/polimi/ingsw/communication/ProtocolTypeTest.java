package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: Abbo Giulio A.
 */
class ProtocolTypeTest {

    /*Testing that the right element is returned*/
    @Test
    void with() {
        for (ProtocolType element : ProtocolType.values())
            assertEquals(element, ProtocolType.with(element.getCommand()));
    }

    /*Testing that getCommand returns the same as toString*/
    @Test
    void getCommand() {
        for (ProtocolType element : ProtocolType.values())
            assertEquals(element.toString(), element.getCommand());
    }

    /*Testing that toStrings returns the command*/
    @Test
    void toString1() {
        for (ProtocolType element : ProtocolType.values())
            assertEquals(element.getCommand(), element.toString());
    }
}