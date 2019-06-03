package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.User;

/**
 * This class is published by the server and used by the client to invert the
 * communication.
 */
public class RmiInversion implements RmiInversionInterface {
    /**
     * {@inheritDoc}
     */
    @Override
    public void invert(RmiFromClientInterface fromClient) {
        new Thread(() -> new User(new RmiToClient(fromClient)).init()).start();
    }
}
