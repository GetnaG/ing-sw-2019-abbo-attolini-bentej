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

    String askName();

    //TODO: the argument will be a reference to a string
    void SendNotification(String message);

    //TODO: To replace when user interaction will be implemented
    String tempAsk(String message, List<String> options);
    String tempAskList(String message, List<List<String>> options);
}
