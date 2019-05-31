package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the nicknames in the game.
 *
 * @author Fahed B. Tej.
 * @author Silvio Attolini
 */
public class Nicknames implements SuspensionListener {
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
     * List of online players not in a game
     */
    private List<String> waitingRoomNames;

    /**
     * Default constructor
     */
    private Nicknames() {
        this.onlineNames = new ArrayList<>();
        this.offlineNames = new ArrayList<>();
        this.waitingRoomNames = new ArrayList<>();
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
     * @return 1 if operation was successful, 0 if taken by an offline player or -1 if taken by an online player.
     */
    public synchronized int addNickname(String nickname) {


        if (!onlineNames.contains(nickname) && !offlineNames.contains(nickname)) {
            onlineNames.add(nickname);

            System.out.println("lista attuale: ");

            return 1;
        }
        if (offlineNames.contains(nickname))//TODO set the old matchlistener
            return 0;

        return -1;

        //deadlock checks

    }

    /**
     * Sets the given player's status to disconnected
     *  @param player disconnected player
     * @param marchSuspensionListener
     */
    @Override
    public synchronized void playerSuspension(String player, SuspensionListener marchSuspensionListener) {
        //FIXME what if the player was not in onlineNames?
        onlineNames.remove(player);
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

        //TODO: kill the old user
        //ToClientInterface cliInterface = player.getToClient();
        //cliInterface.quit();
    }

    @Override
    public boolean playerUpdate(String player, ToClientInterface newConnection) {
        //TODO implement
        return false;
    }

    /**
     * Removes the given players from the waiting room list
     *
     * @param players nicknames of players to be removed
     */
    public void removeFromWaitingRoom(List<Player> players) {
        waitingRoomNames.removeAll(players);
    }

    public String getNicknameFirstPlayer() {
        return waitingRoomNames.get(0);
    }


}
