package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.serverlogic.ServerMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This thread listens for socket connections and accepts them.
 *
 * @author Abbo Giulio A.
 */
public class SocketDispatcher extends Thread {
    /**
     * The port this listens to.
     */
    private int port;
    /**
     * Whether this is running; when false the thread ends its execution.
     */
    private boolean listening;
    /**
     * The class which will be notified if the socket can not start.
     */
    private ServerMain serverMain;

    /**
     * Constructor that sets the attributes for this.
     *
     * @param serverMain the class that will be notified if the socket can
     *                   not start
     * @param port       the listening port
     */
    public SocketDispatcher(ServerMain serverMain, int port) {
        this.port = port;
        this.serverMain = serverMain;
        listening = true;
    }

    /**
     * Starts listening on the port, on new connection a thread is created.
     * The child thread creates a {@linkplain User} with a
     * {@linkplain SocketToClient} and calls {@linkplain User#init()}.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (listening) {
                Socket socket = serverSocket.accept();
                new Thread(() -> new User(new SocketToClient(socket)).init());
            }
        } catch (IOException e) {
            serverMain.notifyException(e);
        }
    }
}
