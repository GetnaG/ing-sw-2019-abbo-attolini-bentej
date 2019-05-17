package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.client.interaction.InteractionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class handles the communication through socket, client side.
 *
 * @author Abbo Giulio A.
 */
public class SocketFromServer {
    /**
     * The class that will interact with the user.
     */
    private InteractionInterface interactor;

    private boolean listening;

    /**
     * Constructs a class with the provided {@linkplain InteractionInterface}.
     *
     * @param interactor the class that will interact with the user
     */
    public SocketFromServer(InteractionInterface interactor) {
        this.interactor = interactor;
    }

    /**
     * Starts the socket and waits for commands.
     * On new commands, calls the appropriate methods of the interactor and
     * sends to the server the user choice.
     *
     * @param ip   the ip of the server
     * @param port the port of the server
     * @throws IOException if the socket can not be opened or connection is lost
     */
    public void startListening(String ip, int port) throws IOException {
        String input;
        listening = true;

        /*Opening the socket and initializing the I/O*/
        try (Socket socket = new Socket(ip, port);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),
                     true)
        ) {
            /*Listening until the QUIT command*/
            while (listening) {
                input = in.readLine();
                try {
                    ProtocolMessage message = new Gson().fromJson(input,
                            ProtocolMessage.class);
                    ProtocolMessage answer;
                    switch (message.getCommand()) {
                        case UPDATE:
                            handleUpdates(message.getUpdates());
                            answer = new ProtocolMessage(message.getCommand());
                            break;
                        case NOTIFICATION:
                            handleNotifications(message.getNotifications());
                            answer = new ProtocolMessage(message.getCommand());
                            break;
                        default:
                            if (message.getCommand().hasOptions())
                                answer = new ProtocolMessage(message.getCommand(),
                                        handleQuestion(message.getCommand(), message.getOptions()));
                            else
                                answer = new ProtocolMessage(message.getCommand(),
                                        handleQuestion(message.getCommand()));
                    }
                    out.println(new Gson().toJson(answer));
                } catch (RuntimeException e) {
                    interactor.sendNotification("Could not parse " + input);
                }
            }
        }
    }

    private void handleNotifications(Notification[] notifications) {
        interactor.sendNotification("Notification: ");
        for (Notification n : notifications) {
            interactor.sendNotification(n.getType().toString());
            if (n.getType() == Notification.NotificationType.QUIT)
                listening = false;
        }
    }

    private void handleUpdates(Update[] updates) {
        interactor.sendNotification("Update: ");
        for (Update u : updates)
            interactor.sendNotification(u.type.toString());
    }

    private String handleQuestion(MessageType command) {
        return interactor.tempAsk(command.getMessage());
    }

    private String handleQuestion(MessageType command, String[][] options) {
        return Integer.toString(interactor.tempAsk(command.getMessage(),
                options));
    }
}
