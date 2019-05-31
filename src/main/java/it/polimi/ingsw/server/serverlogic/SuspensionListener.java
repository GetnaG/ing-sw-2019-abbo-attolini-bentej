package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.ToClientInterface;

/**
 * Classes implementing this will handle the suspension of a player.
 *
 * @author Abbo Giulio A.
 * @see Nicknames
 * @see it.polimi.ingsw.server.controller.DeathmatchController
 */
public interface SuspensionListener {

    /**
     * Notifies the suspension of a player.
     * This means that the player is no longer active, did not answer in time
     * or there are problems in the communication.
     * <p>
     * The nickname of the player will become available for future
     * connections that will take his place, and the player will skip his turn.
     *
     * @param player                  the nickname of the suspended player
     * @param marchSuspensionListener will put the player back in the right
     *                                match
     */
    void playerSuspension(String player, SuspensionListener marchSuspensionListener);

    /**
     * Notifies that a suspended player is back online on the old connection.
     * He will no longer skip his turn, and his nickname will not be available.
     *
     * @param player the name of the player that is back online
     */
    void playerResumption(String player);

    /**
     * Notifies that a player is online with the same name of a suspended
     * player.
     * <p>
     * The new player will communicate through the provided interface, the
     * old interface can be discarded.
     *
     * @param player        the nickname of the player that is back online
     * @param newConnection the class that will handle the communication from
     * @return true if the player was successfully updated
     */
    boolean playerUpdate(String player, ToClientInterface newConnection);

}