package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.SocketFromServer;
import it.polimi.ingsw.communication.SocketProtocol;

import java.io.IOException;
import java.util.Scanner;

/**
 * Mock client
 */
public class clientMain {
    /*Socket with mock interactor*/
    clientMain() {
        InteractionInterface mock = new MockInteractor();

        SocketFromServer fromServer = new SocketFromServer(mock);
        try {
            fromServer.startListening("localhost", 4590);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new clientMain();
    }

    class MockInteractor implements InteractionInterface {
        MockInteractor() {
            System.out.println("This is a mock interactor; you will interact " +
                    "directly with resources IDs and not with their parsed " +
                    "value.");
        }

        private String ask(String question) {
            System.out.println(question);
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }

        @Override
        public String askName() {
            return ask(SocketProtocol.NICKNAME.getCommand());
        }

        @Override
        public void SendNotification(String message) {
            System.out.println(message);
        }
    }
}

