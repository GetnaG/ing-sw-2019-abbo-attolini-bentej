package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientlogic.ClientController;

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
     * The class that will handle questions, updated and notifications.
     */
    private ClientController controller;
    /**
     * The socket used for communicating.
     */
    private Socket socket;
    /**
     * Whether this should be listening for new commands from the socket.
     */
    private boolean listening;

    /**
     * Creates a new instance with the provided controller and socket.
     *
     * @param controller the controller for the new instance
     * @param socket     the socket connected with the server
     */
    public SocketFromServer(ClientController controller, Socket socket) {
        this.controller = controller;
        this.socket = socket;
    }

    /**
     * Waits for commands.
     * On new commands, calls the appropriate methods of the controller and
     * sends back to the server the user choice.
     *
     * @throws IOException if the socket can not be opened or connection is lost
     */
    public void startListening() throws IOException {
        String input;
        listening = true;

        /*Try-with-resources will automatically close the resources*/
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            while (listening) {

                /*Parsing each input*/
                input = in.readLine();
                ProtocolMessage message = new Gson().fromJson(input, ProtocolMessage.class);

                /*Handling the message and returning the answer*/
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
            }
        }
    }

    /**
     * Stops listening to the socket.
     * <p>
     * Note: if this is not called when handling a message from the server,
     * then the execution could get stuck listening for one last command.
     */
    public void stopListening() {
        listening = false;
        //TODO: check if socket is still listening.
    }

    private void handleNotifications(Notification[] notifications) {
        controller.handleNotifications(notifications);
    }

    private void handleUpdates(Update[] updates) {
        controller.handleUpdates(updates);
    }

    private String handleQuestion(MessageType command) {
        return Integer.toString(controller.handleQuestion(command, null));
    }

    private String handleQuestion(MessageType command, String[][] options) {
        return Integer.toString(controller.handleQuestion(command, options));
    }
}
