package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.server.controller.DeathmatchController;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                    "Usage: java ServerMain <seconds for waiting room>");
            System.exit(1);
        }

        secondsWaitingRoom = Integer.parseInt(args[1]);


   }

    /**
     *
     * @param e
     */
    public void notifyException(Exception e) {

    }

    public static ServerHall getServerHall(){
        if (hall == null)
            hall = new ServerHall(secondsWaitingRoom);
        return hall;
    }
}
