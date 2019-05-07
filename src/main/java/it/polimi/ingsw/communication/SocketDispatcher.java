package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.serverlogic.ServerMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                Socket socket = serverSocket.accept();
                new Thread(() -> new User(new SocketToClient(socket)).init());
            }

        } catch (IOException e) {
            serverMain.notifyException(e);
        }
    }
}
