package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.server.model.board.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the nicknames in the game.
 *
 * @author Fahed B. Tej.
 */
public class Nicknames {
    /**
     * List of the names of the players in the game. It include connected and disconnected players.
     */
    private List<String> names;

    /**
     * List of the names reserved by someone using the requestNickname() method.
     */
    private List<String> reservedNames;

    /**
     * List of the names of the disconnected players.
     */
    private List<String> offlineNames;

    /**
     * Default constructor
     */
    public Nicknames(){
        this.names = new ArrayList<>();
        this.reservedNames = new ArrayList<>();
        this.offlineNames = new ArrayList<>();
    }

    /**
     * Given a nickname, the following method will check if the nickname is already avaiable, taken by an online player or taken by an offline player. Then, if it is avaiable, it will add it.
     * @param nickname      requested nickname
     * @return  1 if is avaiable, 0 if taken by an offline player or -1 if taken by an offline player.
     */
    public int requestNickname(String nickname) {
        if (names.contains(nickname) && !reservedNames.contains(nickname) && !offlineNames.contains(nickname)){
            names.add(nickname);
            return 1;
        }
        if (offlineNames.contains(offlineNames))
            return -1;
        return 0;
    }

    /**
     * Adds user to the offline players.
     * @param user  disconected user
     */
    public void addOfflineUser(String user){
        offlineNames.add(user);
    }

}
