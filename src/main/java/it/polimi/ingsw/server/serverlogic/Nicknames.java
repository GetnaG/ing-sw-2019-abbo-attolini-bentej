package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the nicknames in the game.
 *
 * @author Fahed B. Tej.
 */
public class Nicknames implements SuspensionListener {
    /**
     * List of the names of the players in the game. It include connected and disconnected players.
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
    public Nicknames(){
        this.onlineNames = new ArrayList<>();
        this.offlineNames = new ArrayList<>();
        this.waitingRoomNames = new ArrayList<>();
    }

    /**
     * Adds the given nickname to the registered nicknames.
     * @param nickname      requested nickname
     * @return  1 if operation was successful, 0 if taken by an offline player or -1 if taken by an online player.
     */
    public int addNickname(String nickname) {
        if (!onlineNames.contains(nickname)  && !offlineNames.contains(nickname)){
            onlineNames.add(nickname);
            return 1;
        }
        if (offlineNames.contains(nickname))
            return 0;
        return -1;
    }

    /**
     * Sets the given player's status to disconnected
     * @param player    disconnected player
     */
    @Override
    public void playerSuspension(Player player) {
        onlineNames.remove(player.getName());
        offlineNames.add(player.getName());
    }

    /**
     * Sets the given player's status to disconnected
     * @param player    reconnected player
     */
    @Override
    public void playerResumption(Player player) {
        onlineNames.add(player.getName());
        offlineNames.remove(player.getName());
    }

    /**
     * Removes the given players from the waiting room list
     * @param players   nicknames of players to be removed
     */
    public void removeFromWaitingRoom(List<Player> players){
        waitingRoomNames.removeAll(players);
    }

    public String getNicknameFirstPlayer(){
        return waitingRoomNames.get(0);
    }

}
