package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.client.clientlogic.ClientMain;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.communication.rmi.RmiInversion;
import it.polimi.ingsw.communication.socket.SocketDispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the class which starts the server.
 * Sets the port for the socket connections, the time for the waiting room
 * and the time a user has to take a decision; the values are taken from
 * command line.
 * <p>
 * It contains a {@link ServerHall} which is a lobby for players.
 * If multiple types of game will be implemented, this will contain a
 * {@link ServerHall} for each type.
 *
 * @author Fahed B. Tej
 * @author Abbo Giulio A.
 * @see ServerHall
 */
public class ServerMain {
    /**
     * The logger for the server.
     */
    private static final Logger LOG = Logger.getLogger(ClientMain.class.getName());
    /**
     * Seconds before a game of at least 3 starts
     */
    private static int secondsWaitingRoom;
    /**
     * ServerHall used to manage the connected users.
     */
    private static ServerHall deathMatchHall;

    /**
     * This constructor will fire two thread, for socket and RMI.
     * After this call the server will be ready to handle connections.
     *
     * @param socketPort the port to which socket connection are accepted
     * @param rmiPort    the port for rmi
     */
    private ServerMain(int socketPort, int rmiPort) {
        /*Setting up Socket*/
        try {
            new SocketDispatcher(new ServerSocket(socketPort)).start();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception when setting up socket", e);
            System.exit(-1);
        }

        /*Setting up RMI*/
        try {
            LocateRegistry.createRegistry(1099);
            LocateRegistry.getRegistry().bind("inversion",
                    UnicastRemoteObject.exportObject(new RmiInversion(), rmiPort));
        } catch (RemoteException | AlreadyBoundException e) {
            LOG.log(Level.SEVERE, "Exception when setting up RMI", e);
            System.exit(-1);
        }
    }

    /**
     * Starts the Server with command line arguments.
     *
     * @param args Usage: java ServerMain <socketPort> <rmiPort> <seconds for
     *             waiting room> <seconds for user choice>
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            LOG.severe("Usage: java ServerMain <socketPort> <rmiPort> <seconds for waiting room> <seconds for user choice>");
            System.exit(1);
        }

        secondsWaitingRoom = Integer.parseInt(args[2]);
        User.setWaitingTime(Integer.parseInt(args[3]));
        new ServerMain(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }

    /**
     * Returns the hall for the standard match.
     * If the hall does not exist yet, it is created and returned, ready to
     * accept new connections.
     *
     * @return the hall for the standard match
     */
    public static ServerHall getDeathMatchHall() {
        if (deathMatchHall == null) {
            deathMatchHall = new ServerHall(secondsWaitingRoom);
            new Thread(deathMatchHall).start();
        }
        return deathMatchHall;
    }

    /**
     * Returns the logger for the server.
     *
     * @return the logger for the server
     */
    public static Logger getLog() {
        return LOG;
    }
}