package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.client.clientlogic.ClientMain;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.communication.socket.SocketDispatcher;

import java.io.IOException;
import java.net.ServerSocket;
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
     * @param port the port to which socket connection are accepted.
     */
    private ServerMain(int port) {
        /*Setting up Socket*/
        try {
            new SocketDispatcher(new ServerSocket(port)).start();
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            System.exit(-1);
        }

        //TODO: setup RMI
    }

    /**
     * Starts the Server with command line arguments.
     *
     * @param args Usage: java ServerMain <port> <seconds for waiting room> <seconds for user choice>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            LOG.severe("Usage: java ServerMain <port> <seconds for waiting room> <seconds for user choice>");
            System.exit(1);
        }

        secondsWaitingRoom = Integer.parseInt(args[0]);
        User.setWaitingTime(Integer.parseInt(args[2]));
        new ServerMain(Integer.parseInt(args[1]));
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