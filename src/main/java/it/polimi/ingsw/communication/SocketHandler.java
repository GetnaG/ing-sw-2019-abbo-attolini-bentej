package it.polimi.ingsw.communication;

import java.net.Socket;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class SocketHandler extends Thread {
    Socket socket;
    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {


    }
}
