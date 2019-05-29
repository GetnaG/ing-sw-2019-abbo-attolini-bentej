package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.client.clientlogic.ClientMain;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.communication.socket.SocketDispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * Represents the class which starts the server.
 * <p>
 * It contains a {@linkplain ServerHall} which is a lobby for players.
 *
 * @author Fahed B. Tej
 * @author Abbo Giulio A.
 * @see ServerHall
 */
public class ServerMain {
    private static final Logger LOG = Logger.getLogger(ClientMain.class.getName());

    /**
     * ServerHall used to manage the connected users.
     */
    private static ServerHall hall;

    /**
     * Seconds before a game of at least 3 starts
     */
    private static int secondsWaitingRoom;

    /**
     * Thread that listens to the port for incoming connections.
     */
    private static Thread connectionDispatcher;

    /**
     * The port for the socket.
     */
    private static int PORT;

    /**
     * Starts the Server.
     *
     * @param args Usage: java ServerMain <port> <seconds for waiting room>
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            LOG.severe("Usage: java ServerMain <port> <seconds for waiting room>");
            System.exit(1);
        }
        PORT = Integer.parseInt(args[0]);
        secondsWaitingRoom = Integer.parseInt(args[1]);

        User.setWaitingTime(30);//TODO: set from commandline

        /*Setting up Socket*/
        try {
            connectionDispatcher = new SocketDispatcher(new ServerSocket(PORT));
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            System.exit(-1);
        }
        connectionDispatcher.start();

        //TODO setup RMI

        /*Setting up hall*/
        hall = new ServerHall(secondsWaitingRoom, connectionDispatcher);
        hall.run();
    }

    public static ServerHall getServerHall() {
        if (hall == null)
            hall = new ServerHall(secondsWaitingRoom, connectionDispatcher);
        return hall;
    }

    public static Logger getLog() {
        return LOG;
    }
}
