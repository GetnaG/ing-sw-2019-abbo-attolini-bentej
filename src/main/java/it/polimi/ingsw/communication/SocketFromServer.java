package it.polimi.ingsw.communication;

import it.polimi.ingsw.client.interaction.InteractionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
     * returns the user choice if necessary.
     *
     * @param ip   the ip of the server
     * @param port the port of the server
     * @throws IOException if the socket can not be opened or connection is lost
     */
    public void startListening(String ip, int port) throws IOException {
        String input;

        /*Opening the socket and initializing the I/O*/
        try (Socket socket = new Socket(ip, port);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),
                     true)
        ) {
            /*Listening until the QUIT command*/
            while (!(input = in.readLine()).equals(SocketProtocol.QUIT.getCommand())) {

                /*Trying to convert the string to enum and handling the command*/
                try {
                    SocketProtocol command = SocketProtocol.with(input);
                    switch (command) {
                        case EFFECTS_SEQUENCE:
                        case TARGET:
                            out.println(interactor.tempAskList(
                                    command.getCommand(), multiOptions(in)));
                            break;
                        case SPAWN:
                        case POWERUP:
                        case DESTINATION:
                        case WEAPON:
                        case WEAPON_TO_BUY:
                        case WEAPON_TO_DISCARD:
                        case WEAPON_TO_RELOAD:
                        case ACTION:
                        case POWERUP_FOR_PAYING:
                        case USE_TAGBACK:
                            out.println(interactor.tempAsk(
                                    command.getCommand(), options(in)));
                            break;
                        case NICKNAME:
                            out.println(interactor.askName());
                            break;
                        case PROTOCOL_LIST:
                        case PROTOCOL_END_LIST:
                        case PROTOCOL_MULTI:
                        case PROTOCOL_END_MULTI:
                        case PROTOCOL_ERR_CHOICE:
                        case QUIT:
                        case PROTOCOL_GREET:
                        default:
                            interactor.SendNotification(input);
                    }
                } catch (IllegalArgumentException e) {

                    /*The input could not be converted: sending a message*/
                    interactor.SendNotification("error " + input);//TODO localization
                }
            }
            out.println(SocketProtocol.QUIT);
        }
    }

    /**
     * Reads from the socket the available options and returns them into a list.
     *
     * @param in the input from the socket
     * @return a list of the possible options
     * @throws IOException if there are problems with the socket
     */
    private List<String> options(BufferedReader in) throws IOException {
        String input;
        List<String> options = new ArrayList<>();
        while ((input = in.readLine()).equals(SocketProtocol.PROTOCOL_LIST.getCommand())) {
            while (!(input = in.readLine()).equals(SocketProtocol.PROTOCOL_END_LIST.getCommand()))
                options.add(input);
        }
        return options;
    }

    /**
     * Reads from the socket a list of possible sequences.
     *
     * @param in the input from the socket
     * @return a list of the possible option sequences
     * @throws IOException if there are problems with the socket
     */
    private List<List<String>> multiOptions(BufferedReader in) throws IOException {
        String input;
        List<List<String>> multiOptions = new ArrayList<>();
        in.readLine();//TODO: skipping list head (to be fixed)
        while ((input = in.readLine()).equals(SocketProtocol.PROTOCOL_LIST.getCommand())) {
            List<String> o = new ArrayList<>();
            while (!(input = in.readLine()).equals(SocketProtocol.PROTOCOL_END_LIST.getCommand()))
                o.add(input);
        }
        in.readLine();//TODO: skipping list tail (to be fixed)
        return multiOptions;
    }
}
