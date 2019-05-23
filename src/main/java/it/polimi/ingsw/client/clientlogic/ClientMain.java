package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.MessageType;
import it.polimi.ingsw.communication.SocketFromServer;

import java.io.IOException;
import java.util.Scanner;

/**
 * Mock client
 */
public class ClientMain {
    /*Socket with mock interactor*/
    ClientMain() {
        InteractionInterface mock = new MockInteractor();

        SocketFromServer fromServer = new SocketFromServer(mock);
        try {
            fromServer.startListening("localhost", 4590);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientMain();
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
        public void notifyUpdatedState() {

        }

        @Override
        public String askName() {
            return ask(MessageType.NICKNAME.getMessage());
        }

        @Override
        public void sendNotification(String message) {
            System.out.println(message);
        }

        @Override
        public String tempAsk(String message) {
            System.out.println(message);
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }

        @Override
        public int tempAsk(String message, String[][] options) {
            System.out.println(message + "Insert the number associated with " +
                    "the choice.");
            for (int i = 0; i < options.length; i++) {
                System.out.println("\n" + i);
                for (int j = 0; j < options[i].length; j++) {
                    System.out.println(options[i][j]);
                }
            }
            Scanner scanner = new Scanner(System.in);
            return Integer.parseInt(scanner.nextLine());
        }
    }
}

