package it.polimi.ingsw.client.interaction;

import java.util.List;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public interface InteractionInterface {

    public void notifyUpdatedState();

    String askName();

    //TODO: the argument will be a reference to a string
    void sendNotification(String message);

    //TODO: To replace when user interaction will be implemented
    String tempAsk(String message);
    int tempAsk(String message, String[][] options);


}
