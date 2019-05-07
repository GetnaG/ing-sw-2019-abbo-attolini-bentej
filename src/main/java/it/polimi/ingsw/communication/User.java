package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.ToClientInterface;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class User {
    ToClientInterface toClient;

    public User(ToClientInterface toClient) {
        this.toClient = toClient;
    }

    public void init() {

    }
}
