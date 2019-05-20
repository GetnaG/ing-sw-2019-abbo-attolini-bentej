package it.polimi.ingsw.communication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    private SocketDispatcher dispatcher;

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
            if (dispatcher.isAlive()) {
                dispatcher.stopListening();

                /*Stopping the thread*/
                new Socket("localhost", PORT);
                dispatcher.join();
            }
            socket.close();
        } catch (IOException | NullPointerException | InterruptedException ignored) {
        }
    }

    /*Testing if ServerMain is notified of an exception*/
    @Test
    void run_socketException() {

        /*Closing the socket*/
        try {
            socket.close();
            dispatcher = new SocketDispatcher(socket);
            assertThrows(UncheckedIOException.class, dispatcher::run);
        } catch (IOException ignored) {
        }
    }

    /*Testing normal behaviour*/
    @Test
    void run() {
        dispatcher = new SocketDispatcher(socket);
        dispatcher.start();

        assertDoesNotThrow(() -> new Socket("localhost", PORT));
    }

    /*Testing socket disconnected*/
    @Test
    void run_socketDisconnected() {
        try {
            dispatcher = new SocketDispatcher(socket);
            dispatcher.start();

            assertDoesNotThrow(() -> new Socket("localhost", PORT)
                    .close());

        } catch (UncheckedIOException ignored) {
        }
    }
}