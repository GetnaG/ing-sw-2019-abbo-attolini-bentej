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
     * The active instance.
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
        onlineNames = new ArrayList<>();
        offlineNames = new ArrayList<>();
    }

    /**
     * Returns the active instance of this class.
     *
     * @return the active instance of this class
     */
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
            return 1;
        }
        if (offlineNames.contains(nickname)) {
            playerResumption(nickname);
            return 0;
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets the given player's status to disconnected.
     */
    @Override
    public synchronized void playerSuspension(String player) {
        if (!onlineNames.remove(player))
            //if the player was not in onlineNames
            return;
        if (!offlineNames.contains(player))
            offlineNames.add(player);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets the given player's status to (re)connected.
     */
    @Override
    public synchronized void playerResumption(String player) {
        if (!onlineNames.contains(player))
            onlineNames.add(player);
        offlineNames.remove(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerUpdate(String player, ToClientInterface newConnection) {
        throw new UnsupportedOperationException("Player update is not implemented");
    }
}
