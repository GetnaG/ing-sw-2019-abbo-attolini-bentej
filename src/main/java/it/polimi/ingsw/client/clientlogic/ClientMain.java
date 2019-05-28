package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionFactory;
import it.polimi.ingsw.client.interaction.InteractionInterface;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sets up the client with model, view and controller.
 *
 * @author Abbo Giulio A.
 * @see InteractionInterface
 * @see MatchState
 * @see ClientController
 */
public class ClientMain {
    private static final Logger LOG = Logger.getLogger(ClientMain.class.getName());

    public static void main(String[] args) {
        ClientController controller;
        InteractionInterface view;
        try {
            view = InteractionFactory.getInteractionInterface(args);
            controller = new ClientController(new MatchState(), view);
            view.setController(controller);

        } catch (IOException | NullPointerException | IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Socket error",e);
        }
    }
}


