package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.MessageType;
import it.polimi.ingsw.communication.SocketFromServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Mock client
 */
public class ClientMain {
    /*Socket with mock interactor*/
    ClientMain() {
        InteractionInterface mock = new MockInteractor();
        try {
            SocketFromServer fromServer =
                    new SocketFromServer(new ClientController(), new Socket(
                            "localhost", 4590));
            fromServer.startListening();
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
        public int chooseEffectSequence(String something) {
            return 0;
        }

        @Override
        public int chooseSpawn(String something) {
            return 0;
        }

        @Override
        public int choosePowerup(String something) {
            return 0;
        }

        @Override
        public int chooseDestination(String something) {
            return 0;
        }

        @Override
        public int chooseWeapon(String something) {
            return 0;
        }

        @Override
        public int chooseWeaponToBuy(String something) {
            return 0;
        }

        @Override
        public int chooseWeaponToDiscard(String something) {
            return 0;
        }

        @Override
        public int chooseWeaponToReload(String something) {
            return 0;
        }

        @Override
        public int chooseAction(String something) {
            return 0;
        }

        @Override
        public int choosePowerupForPaying(String something) {
            return 0;
        }

        @Override
        public int chooseUseTagBack(String something) {
            return 0;
        }

        @Override
        public int chooseTarget(String something) {
            return 0;
        }

        @Override
        public void drawState(MatchState state) {

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

