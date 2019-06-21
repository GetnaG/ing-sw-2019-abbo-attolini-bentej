package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionFactory;
import it.polimi.ingsw.client.interaction.InteractionInterface;

import java.io.IOException;
import java.rmi.RemoteException;
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
    /**
     * The logger for the client.
     */
    public static final Logger LOG = Logger.getLogger(ClientMain.class.getName());

    /**
     * Starts the client with the specified interface.
     *
     * @param args Usage: java ClientMain <interface type>
     */
    public static void main(String[] args) {
        try {
            InteractionInterface view = InteractionFactory.getInteractionInterface(args);
            MatchState model = new MatchState();
            view.setModel(model);
            view.setController(new ClientController(model, view));
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception in main", e);
            System.exit(-1);
        }
    }
}


