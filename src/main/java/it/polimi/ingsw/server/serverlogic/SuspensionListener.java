package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.communication.User;

/**
 * 
 */
public interface SuspensionListener {

    /**
     * @param player
     * @param user
     */
    public void playerSuspension(String player, ToClientInterface user);

    /**
     * @param player
     */
    public void playerResumption(String player);

    void playerUpdate(String player, ToClientInterface newConnection);

}