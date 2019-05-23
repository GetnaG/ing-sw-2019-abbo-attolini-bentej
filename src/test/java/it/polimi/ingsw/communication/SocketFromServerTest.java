package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientlogic.ClientController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Abbo Giulio A.
 * Testing: the getter for the type.
 */
@Disabled
class SocketFromServerTest {
    private static final String DEFAULT_CHOICE = "0";
    private SocketFromServer fromServer;

    @BeforeEach
    void setUp() {
    }

    /*Testing that it receives and handle an update*/
    @Test
    void startListening_update() {
        MockClientController controller = new MockClientController();
        MockSocket server = new MockSocket();
        server.put(new ProtocolMessage(new Update[] {new Update()}));
        SocketFromServer fromServer = new SocketFromServer(controller, server);
        try {
            fromServer.startListening();
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void startListening_notification() {
    }

    @Test
    void startListening_question() {
    }

    @Test
    void stopListening() {
    }

    private class MockClientController extends ClientController {
        private Notification[] notifications;
        private Update[] updates;
        private MessageType message;
        private String[][] options;

        @Override
        public void handleNotifications(Notification[] notifications) {
            this.notifications = notifications;
        }

        @Override
        public void handleUpdates(Update[] updates) {
            this.updates = updates;
        }

        //@Override
        public String handleQuestion(MessageType message, String[][] options) {
            this.message = message;
            this.options = options;
            return DEFAULT_CHOICE;
        }

        public Notification[] getNotifications() {
            return notifications;
        }

        public Update[] getUpdates() {
            return updates;
        }

        public MessageType getMessage() {
            return message;
        }

        public String[][] getOptions() {
            return options;
        }
    }

    private class MockSocket extends Socket {
        private ByteArrayOutputStream toClient;
        private ByteArrayOutputStream toServer;

        MockSocket() {
            toClient = new ByteArrayOutputStream();
            toServer = new ByteArrayOutputStream();
        }

        @Override
        public InputStream getInputStream() {
            byte[] buf = toServer.toByteArray();
            int length = toServer.size();
            toServer.reset();
            return new ByteArrayInputStream(buf, 0, length);
        }

        @Override
        public OutputStream getOutputStream() {
            return toClient;
        }

        ProtocolMessage contents() {
            byte[] buf = toClient.toByteArray();
            int length = toClient.size();
            toClient.reset();
            return new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(buf, 0, length))).lines()
                    .map(s -> new Gson().fromJson(s, ProtocolMessage.class))
                    .collect(Collectors.toList()).get(0);
        }

        void put(ProtocolMessage message) {
            new PrintWriter(toServer, true).println(new Gson().toJson(
                    message));
        }

        @Override
        public synchronized void close() {
            try {
                super.close();
                toClient.close();
                toServer.close();
            } catch (IOException ignored) {
            }
        }
    }
}