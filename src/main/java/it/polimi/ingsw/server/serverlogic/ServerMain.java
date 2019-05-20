package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.SocketDispatcher;
import it.polimi.ingsw.server.controller.DeathmatchController;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Represents the class which starts the server.
 * <p>
 * It contains a {@linkplain ServerHall} which is a lobby for players.
 *
 * @see ServerHall
 * @author Fahed B. Tej
 */
public class ServerMain {

    /**
     * ServerHall used to manage the connected users.
     */
    private static ServerHall hall;

    /**
     * Seconds before a game of at least 3 starts
     */
    private static int secondsWaitingRoom;

    private static Thread connectionDispatcher;

    private static int PORT;

    /**
     * Starts the Server.
     *
     * @param args Usage: java ServerMain <socket/rmi> <port> <seconds for waiting room>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(
                    "Usage: java ServerMain <socket/rmi> <port> <seconds for waiting room>");
            System.exit(1);
        }
        PORT = Integer.parseInt(args[1]);
        secondsWaitingRoom = Integer.parseInt(args[2]);

        if (args[0].equals("socket")) {
            connectionDispatcher = getSocketDispatcher();
        } else {
            // connectionDispatcher = getRMIDispatcher();
        }
        hall = new ServerHall(secondsWaitingRoom, connectionDispatcher);
        hall.run();
    }

    /**
     *
     * @param e
     */
    public static void notifyException(Exception e) {

    }

    /**
     * Creates a new {@linkplain SocketDispatcher}
     *
     * @return a new SocketDispatcher
     */
    private static SocketDispatcher getSocketDispatcher() {
        try {
            return new SocketDispatcher(new ServerSocket(PORT));
        } catch (IOException ignored) {
        }
        return null;
    }



    public static ServerHall getServerHall(){
        if (hall == null)
            hall = new ServerHall(secondsWaitingRoom, connectionDispatcher);
        return hall;
    }
}
