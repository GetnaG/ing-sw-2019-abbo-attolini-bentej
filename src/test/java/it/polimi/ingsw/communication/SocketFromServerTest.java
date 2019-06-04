package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.ProtocolMessage;
import it.polimi.ingsw.communication.protocol.Update;
import it.polimi.ingsw.communication.socket.SocketFromServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/*
 * Author: Abbo Giulio A.
 * Testing: that the controller is notified correctly and the server receives
 *  the intended answer.
 */
class SocketFromServerTest {
    private static final int DEFAULT_CHOICE = 0;
    private SocketFromServer fromServer;
    private MockClientController controller;
    private MockSocket server;

    @BeforeEach
    void setUp() {
        try {
            controller = new MockClientController();
        } catch (RemoteException e) {
            fail(e);
        }
        server = new MockSocket();
        fromServer = new SocketFromServer(controller, server);
    }

    /*Testing that it receives and handle an update*/
    @Test
    void startListening_update() {
        Update[] updates = {
                new Update(Update.UpdateType.DAMAGE_ARRAY, Arrays.asList("A",
                        "B"), "BOB"),
                new Update(Update.UpdateType.LOADED_WEAPONS, Arrays.asList("A",
                        "B"))
        };
        send(new ProtocolMessage(updates));
        Update[] controllerReceived = controller.updates;
        for (int i = 0; i < updates.length; i++) {
            assertEquals(updates[i].getNickname(), controllerReceived[i].getNickname());
            assertEquals(updates[i].getType(), controllerReceived[i].getType());
            assertEquals(updates[i].getNewValue(), controllerReceived[i].getNewValue());
        }
    }

    /*Testing that it receives and handle a notification*/
    @Test
    void startListening_notification() {
        Notification[] notifications = {
                new Notification(Notification.NotificationType.QUIT),
                new Notification(Notification.NotificationType.GREET)
        };
        send(new ProtocolMessage(notifications));
        Notification[] controllerReceived = controller.notifications;
        for (int i = 0; i < notifications.length; i++) {
            assertEquals(notifications[i].getType(), controllerReceived[i].getType());
        }
    }

    /*Testing that it receives and handle a question with options*/
    @Test
    void startListening_question() {
        List<List<String>> options = Arrays.asList(
                Arrays.asList("aa", "ab"),
                Arrays.asList("bb")
        );
        MessageType type = MessageType.ACTION;
        send(new ProtocolMessage(type, options));
        String[][] controllerReceived = controller.options;
        MessageType typeReceived = controller.message;
        for (int i = 0; i < options.size(); i++) {
            for (int j = 0; j < options.get(i).size(); j++) {
                assertEquals(options.get(i).get(j), controllerReceived[i][j]);
            }
        }
        assertEquals(type, typeReceived);
        int choice = Integer.parseInt(server.contents().getUserChoice());
        for (int i = 0; i < options.get(DEFAULT_CHOICE).size(); i++) {
            assertEquals(options.get(DEFAULT_CHOICE).get(i),
                    options.get(choice).get(i));
        }
    }

    /*Sends the message to the client, after this the controller and the
    server socket can be queried for data*/
    private void send(ProtocolMessage message) {
        server.put(message);
        try {
            fromServer.startListening();
        } catch (IOException e) {
            fail(e);
        }
    }

    private class MockClientController extends ClientController {
        private Notification[] notifications;
        private Update[] updates;
        private MessageType message;
        private String[][] options;

        MockClientController() throws RemoteException {
            super(null, null);
        }

        @Override
        public void handleNotifications(Notification[] notifications) {
            this.notifications = notifications;
            fromServer.stopListening();
        }

        @Override
        public void handleUpdates(Update[] updates) {
            this.updates = updates;
            fromServer.stopListening();
        }

        @Override
        public int handleQuestion(MessageType message, String[][] options) {
            this.message = message;
            this.options = options;
            fromServer.stopListening();
            return DEFAULT_CHOICE;
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