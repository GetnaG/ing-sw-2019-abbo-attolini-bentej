package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.Update;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Classes implementing this can be published and used with RMI.
 *
 * @author Abbo Giulio A.
 */
public interface RmiFromClientInterface extends Remote {
    /**
     * Handles notifications.
     * Notifications are defined as events that don't change the state of the game.
     *
     * @param notifications events that don't change the state of the game
     */
    void handleNotifications(Notification[] notifications) throws RemoteException;

    /**
     * Handles updates. Updates are events that change the state of the game.
     *
     * @param updates events that change the state of the game
     */
    void handleUpdates(Update[] updates) throws RemoteException;

    /**
     * Handles a question.
     * It takes care of asking the user of the answer and returns it to the caller.
     *
     * @param message a question
     * @return the answer
     */
    String handleQuestion(MessageType message) throws RemoteException;

    /**
     * Handles a question with options.
     * It takes care of asking the user of the answer and returns it to the caller.
     *
     * @param message a question
     * @param options the options to choose from
     * @return the index of the answer
     */
    int handleQuestion(MessageType message, String[][] options) throws RemoteException;
}