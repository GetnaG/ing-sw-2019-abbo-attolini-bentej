package it.polimi.ingsw.communication.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.communication.protocol.ProtocolMessage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class handles the communication through socket, client side.
 *
 * @author Abbo Giulio A.
 */
public class SocketFromServer {
    /**
     * The class that will handle questions, updates and notifications.
     */
    private ClientController controller;
    /**
     * The socket used for communicating.
     */
    private Socket socket;

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
        try (Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            while (in.hasNextLine()) {

                /*Parsing each input*/
                String input = in.nextLine();
                if (input == null)
                    continue;
                ProtocolMessage message = new Gson().fromJson(input, ProtocolMessage.class);

                /*Handling the message and returning the answer*/
                ProtocolMessage answer;
                switch (message.getCommand()) {
                    case UPDATE:
                        controller.handleUpdates(message.getUpdates());
                        answer = new ProtocolMessage(message.getCommand());
                        break;
                    case NOTIFICATION:
                        controller.handleNotifications(message.getNotifications());
                        answer = new ProtocolMessage(message.getCommand());
                        break;
                    default:
                        if (message.getCommand().hasOptions())
                            answer = new ProtocolMessage(message.getCommand(),
                                    Integer.toString(controller.handleQuestion(
                                            message.getCommand(),
                                            message.getOptions())));
                        else
                            answer = new ProtocolMessage(message.getCommand(),
                                    controller.handleQuestion(message.getCommand()));
                }
                out.println(new Gson().toJson(answer));
            }
        }
    }

    /**
     * Stops listening to the socket.
     */
    public void stopListening() {
        try {
            socket.close();
        } catch (IOException e) {
            /*Socket.close() was intentional: ignoring errors*/
        }
    }
}
