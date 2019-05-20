package it.polimi.ingsw.communication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: if the sockets are handled correctly.
 */
class SocketDispatcherTest {
    private static final int PORT = 8374;

    private ServerSocket socket;

    @BeforeEach
    void setUp() {
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            socket.close();
        } catch (IOException | NullPointerException ignored) {
        }
    }

    /*Testing if ServerMain is notified of an exception*/
    @Test
    void run_socketException() {

        /*Closing the socket*/
        try {
            socket.close();
            SocketDispatcher dispatcher = new SocketDispatcher(socket);
            assertThrows(UncheckedIOException.class, dispatcher::run);
        } catch (IOException e) {
            fail(e);
        }
    }

    /*Testing normal behaviour*/
    @Test
    void run() {
        SocketDispatcher dispatcher = new SocketDispatcher(socket);
        dispatcher.start();

        assertDoesNotThrow(() -> new Socket("localhost", PORT));

        dispatcher.stopListening();
    }

    /*Testing socket disconnected*/
    @Test
    void run_socketDisconnected() {
        SocketDispatcher dispatcher = new SocketDispatcher(socket);
        dispatcher.start();//Same thread

        assertDoesNotThrow(() -> new Socket("localhost", PORT)
                .close());

        dispatcher.stopListening();
    }
}