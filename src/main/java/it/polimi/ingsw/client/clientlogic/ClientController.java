package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.communication.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the Controller of the client. It elaborates the received Notifications, Updates and Commands .
 *
 * @author Fahed B. Tej
 */
public class ClientController {

    /**
     * Represents the state of the Game.
     */
    private MatchState matchState;

    /**
     * Represent the coket connection to the server
     */
    private SocketFromServer socketFromServer;

    public ClientController(MatchState model) {
        this.matchState = model;
    }

    /**
     * Handles notifications. Notifications are defined as events that don't change the state of the game
     *
     * @param notifications events that don't change the state of the game
     */
    public void handleNotifications(Notification[] notifications) {


    }

    /**
     * Handles updates. Updates are events that change the state of the game.
     *
     * @param updates events that change the state of the game.
     */
    public void handleUpdates(Update[] updates) {
        Arrays.asList(updates).forEach(matchState::handleUpdate);
    }

    /**
     * Handles a question. It takes care of asking the user of the answer and returns it to the caller.
     *
     * @param message a question
     * @return the index of the answer. In case of notifications and updates, it returns 0.
     */
    public int handleQuestion(MessageType message, String[][] options) {
        switch (message) {
            case NOTIFICATION:
            case UPDATE:
                throw new IllegalArgumentException("This method does not handle Notifications or Updates");
            case EFFECTS_SEQUENCE:
                chooseEffectSequence(message, options);
                break;
            case SPAWN:
                chooseSpawn(message, options);
                break;
            case POWERUP:
                chooseSpawn(message, options);
                break;
            case DESTINATION:
                chooseDestination(message, options);
                break;
            case WEAPON:
                chooseWeapon(message, options);
                break;
            case WEAPON_TO_BUY:
                chooseWeaponToBuy(message, options);
                break;
            case WEAPON_TO_DISCARD:
                chooseWeaponToDiscard(message, options);
                break;
            case WEAPON_TO_RELOAD:
                chooseWeaponToReload(message, options);
                break;
            case ACTION:
                chooseAction(message, options);
                break;
            case POWERUP_FOR_PAYING:
                choosePowerup(message, options);
                break;
            case USE_TAGBACK:
                chooseUseTagback(message, options);
                break;
            case TARGET:
                chooseTarget(message, options);
            default:
                // nothing


        }
        return 0;
    }

    /**
     * Chooses an Effect from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return
     */
    public int chooseEffectSequence(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses an Spawn from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseSpawn(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Powerup from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int choosePowerup(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Destination from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseDestination(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Weapon from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeapon(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Weapon to buy from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeaponToBuy(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a Weapon to discard from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeaponToDiscard(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a Weapon to reload from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeaponToReload(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses an action from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseAction(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a Powerup for pating from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int choosePowerupForPaying(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a tagback use from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseUseTagback(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a target from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseTarget(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Sets up a connection with the server accoring to the given type
     *
     * @param type 0 means socket, 1 means RMI
     */
    public void setConnectionType(int type) {
        if (type == 0) {
            // socket
            //TODO Implement
        } else {
            //RMI
        }

    }

    /**
     * Used to check if the given username is valid
     *
     * @param username username in question
     * @return 0 if it's avaiable,
     * 1 if it's not avaiable and player is online,
     * 2 if it's not avaibale and the player is offline
     */
    public int checkUsername(String username) {
        //TODO Implement
        return 0;
    }

    /**
     * Gets the players in the hall of the server
     * @return players in the hall of the server
     */
    public List<String> getPlayersInHall() {
        //TODO Implement
        List<String> players = new ArrayList<>();
        players.add("Alice");
        players.add("Bob");
        players.add("Charlie");

        return players;
    }

}
