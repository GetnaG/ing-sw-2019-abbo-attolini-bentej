package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionFactory;
import it.polimi.ingsw.client.interaction.InteractionInterface;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Sets up the client.
 *
 * @author Abbo Giulio A.
 * @see InteractionInterface
 * @see MatchState
 * @see ClientController
 */
public class ClientMain {
    private static final Logger LOG = Logger.getLogger(ClientMain.class.getName());

    public static void main(String[] args) {
        try {
            InteractionInterface interaction =
                    InteractionFactory.getInteractionInterface(args);
        } catch (IOException | NullPointerException | IllegalArgumentException e) {
            LOG.severe(e.getMessage());
            return;
        }

        /*
        TODO: set the model for the view
        TODO: choose the connection mode
        TODO: Connect
        TODO: wait for commands
         */
    }
}


