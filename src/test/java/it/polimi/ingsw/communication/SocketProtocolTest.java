package it.polimi.ingsw.communication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * Author: Abbo Giulio A.
 */
class SocketProtocolTest {

    /*Testing that the right element is returned*/
    @Test
    void with() {
        for (SocketProtocol element : SocketProtocol.values())
            assertEquals(element, SocketProtocol.with(element.getCommand()));
    }

    /*Testing that getCommand returns the same as toString*/
    @Test
    void getCommand() {
        for (SocketProtocol element : SocketProtocol.values())
            assertEquals(element.toString(), element.getCommand());
    }

    /*Testing that toStrings returns the command*/
    @Test
    void toString1() {
        for (SocketProtocol element : SocketProtocol.values())
            assertEquals(element.getCommand(), element.toString());
    }
}