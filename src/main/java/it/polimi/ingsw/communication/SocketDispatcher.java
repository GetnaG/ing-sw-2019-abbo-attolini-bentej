package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.serverlogic.ServerMain;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class SocketDispatcher extends Thread {
    private int port;
    private boolean listening;
    private ServerMain serverMain;

    public SocketDispatcher(ServerMain serverMain, int port) {
        this.port = port;
        listening = true;
        this.serverMain = serverMain;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (listening) {
                new SocketHandler(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            serverMain.notifyException(e);
        }
    }
}
