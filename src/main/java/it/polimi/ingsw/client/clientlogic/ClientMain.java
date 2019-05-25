package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.MessageType;
import it.polimi.ingsw.communication.SocketFromServer;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Mock client
 */
public class ClientMain {
    /*Socket with mock interactor*/
    ClientMain() {
/*        InteractionInterface mock = new MockInteractor();
        try {
            SocketFromServer fromServer =
                    new SocketFromServer(new ClientController(), new Socket(
                            "localhost", 4590));
            fromServer.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void main(String[] args) {
        new ClientMain();
    }
}

