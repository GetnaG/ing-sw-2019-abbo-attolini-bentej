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
     * The socket that will receive the connections.
     */
    private final ServerSocket serverSocket;
    /**
     * The class which will be notified if the socket can not start.
     */
    private ServerMain serverMain;
    /**
     * Whether this is running; when false the thread ends its execution.
     */
    private boolean listening;

    /**
     * Constructor that sets the attributes for this.
     *
     * @param serverSocket the socket that will receive the connections
     */
    public SocketDispatcher(ServerSocket serverSocket, ServerMain serverMain) {
        this.serverSocket = serverSocket;
        this.serverMain = serverMain;
        listening = true;
    }

    /**
     * Constructor that sets the attributes for this.
     *
     * @param serverSocket the socket that will receive the connections
     */
    public SocketDispatcher(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        listening = true;
    }

    /**
     * Starts listening on the socket, on new connection a thread is created.
     * The child thread creates a {@linkplain User} with a
     * {@linkplain SocketToClient} and calls {@linkplain User#init()}.
     */
    @Override
    public void run() {
        try (serverSocket) {
            while (listening) {
                System.out.println("Socket is ready and listening.");
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        new User(new SocketToClient(socket)).init();
                    } catch (ToClientException e) {
                        System.out.println("Socket died.");
                    }
                }).start();
            }
        } catch (IOException | NullPointerException e) {
            ServerMain.notifyException(e);
        }
    }

    /**
     * Stops listening through the socket.
     */
    public void stopListening() {
        listening = false;
    }
}
