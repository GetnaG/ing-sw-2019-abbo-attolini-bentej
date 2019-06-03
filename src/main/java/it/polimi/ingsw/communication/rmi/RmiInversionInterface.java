package it.polimi.ingsw.communication.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Classes implementing this will allow the client to invert the
 * communication with the server.
 *
 * @author Abbo Giulio A.
 */
public interface RmiInversionInterface extends Remote {
    /**
     * The client provides a remote object to the server.
     *
     * @param fromClient the object from the client
     * @throws RemoteException if there are problems with RMI
     */
    void invert(RmiFromClientInterface fromClient) throws RemoteException;
}
