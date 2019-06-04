package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.ToClientInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the nicknames in the game.
 *
 * @author Fahed B. Tej.
 * @author Silvio Attolini
 */
public class Nicknames implements SuspensionListener {

    /**
     * Represents the instance of the actual list
     */
    private static Nicknames instance;
    /**
     * List of the names of the players in the game. It includes connected players.
     */
    private List<String> onlineNames;

    /**
     * List of the names of the disconnected players.
     */
    private List<String> offlineNames;

    /**
     * Default constructor
     */
    private Nicknames() {
        this.onlineNames = new ArrayList<>();
        this.offlineNames = new ArrayList<>();
    }

    public static Nicknames getInstance() {

        if (instance == null)
            instance = new Nicknames();
        return instance;
    }

    /**
     * Adds the given nickname to the registered nicknames.
     *
     * @param nickname requested nickname
     * @return 1 if operation was successful, 0 if taken by an offline player or -1 if taken by an online player
     */
    public synchronized int addNickname(String nickname) {


        if (!onlineNames.contains(nickname) && !offlineNames.contains(nickname)) {
            onlineNames.add(nickname);
            System.out.println("lista attuale: ");
            for (String name : onlineNames)
                System.out.println("" + name + "");
            System.out.println("\n");

            return 1;
        }
        if (offlineNames.contains(nickname))
            return 0;

        return -1;

        //deadlock checks

    }

    /**
     * Sets the given player's status to disconnected
     *
     * @param player disconnected player
     */
    @Override
    public synchronized void playerSuspension(String player) {

        if (!onlineNames.remove(player)) //if the player was not in onlineNames
            return;
        offlineNames.add(player);
    }

    /**
     * Sets the given player's status to (re)connected
     *
     * @param player reconnected player
     */
    @Override
    public synchronized void playerResumption(String player) {
        onlineNames.add(player);
        offlineNames.remove(player);

    }

    @Override
    public boolean playerUpdate(String player, ToClientInterface newConnection) {
        throw new UnsupportedOperationException(" player update is not implemented");
    }

}
