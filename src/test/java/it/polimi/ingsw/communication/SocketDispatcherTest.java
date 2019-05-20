package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.serverlogic.ServerMain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    private MockServerMain mockServerMain;

    @BeforeEach
    void setUp() {
        mockServerMain = new MockServerMain();
        try {
            socket = new ServerSocket(PORT);
        } catch (IOException ignored) {
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
    @Disabled
    void run_socketException() {

        /*Closing the socket*/
        try {
            socket.close();
            SocketDispatcher dispatcher = new SocketDispatcher(socket,
                    mockServerMain);
            dispatcher.run();//On same thread
        } catch (IOException ignored) {
        }

        assertTrue(mockServerMain.wasNotified);
    }

    /*Testing normal behaviour*/
    @Test
    @Disabled
    void run() {
        SocketDispatcher dispatcher = new SocketDispatcher(socket, mockServerMain);
        dispatcher.start();

        assertFalse(mockServerMain.wasNotified);
        assertDoesNotThrow(() -> new Socket("localhost", PORT));

        dispatcher.stopListening();
    }

    /*Testing socket disconnected*/
    @Test
    @Disabled
    void run_socketDisconnected() {
        SocketDispatcher dispatcher = new SocketDispatcher(socket, mockServerMain);
        dispatcher.start();//Same thread

        assertFalse(mockServerMain.wasNotified);
        assertDoesNotThrow(() -> new Socket("localhost", PORT).close());

        dispatcher.stopListening();
    }

    class MockServerMain extends ServerMain {
        private boolean wasNotified;

        /**
         *  Sorry non compilava perché notify in main é statica.
         * @Override
         *         public void notifyException(Exception e) {
         *             wasNotified = true;
         *         }
         */

    }
}