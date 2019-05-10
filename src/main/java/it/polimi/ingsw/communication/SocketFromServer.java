package it.polimi.ingsw.communication;

import it.polimi.ingsw.client.interaction.InteractionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class SocketFromServer {
    private InteractionInterface interaction;
    private Socket socket;

    public SocketFromServer(InteractionInterface interaction) {
        this.interaction = interaction;
    }

    public void startListening(String ip, int port) throws IOException {
        String input;
        interaction.SendNotification("Socket connecting");
        socket = new Socket(ip, port);
        interaction.SendNotification("Socket connected");
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),
                     true)
        ) {
            while (!(input = in.readLine()).equals(SocketProtocol.QUIT.getCommand())) {
                try {
                    SocketProtocol command = SocketProtocol.with(input);
                    switch (command) {
                        case EFFECTS_SEQUENCE:
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
                        case TARGET:
                            interaction.SendNotification(input);
                            break;
                        case NICKNAME:
                            out.println(interaction.askName());
                            break;
                        case PROTOCOL_LIST:
                        case PROTOCOL_END_LIST:
                        case PROTOCOL_MULTI:
                        case PROTOCOL_END_MULTI:
                        case PROTOCOL_ERR_CHOICE:
                        case QUIT:
                        case PROTOCOL_GREET:
                        default:
                            interaction.SendNotification(input);
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    interaction.SendNotification("error " + input);//TODO localization
                }
            }
            out.println(SocketProtocol.QUIT);
        }
    }
}
