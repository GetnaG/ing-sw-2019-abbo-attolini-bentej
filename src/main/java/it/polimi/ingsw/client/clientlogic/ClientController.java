package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.socket.SocketFromServer;
import it.polimi.ingsw.communication.protocol.Update;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the Controller of the client. It elaborates the received Notifications, Updates and Commands.
 * A controller receives orders from the servers and redirects them to the
 * local model and to the user through the view.
 * <p>
 * (The view is receives orders from the controller and is notified by the
 * view; the model receives orders from the controller and notifies the view).
 *
 * @author Fahed B. Tej
 * @author Abbo Giulio A.
 */
public class ClientController {

    /**
     * Represents the state of the game.
     */
    private MatchState model;
    /**
     * Represents the view, interacts with the user.
     */
    private InteractionInterface view;

    /**
     * Creates a controller with the provided arguments.
     *
     * @param model represents the state of the game
     * @param view  the view, interacts with the user
     */
    public ClientController(MatchState model, InteractionInterface view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles notifications.
     * Notifications are defined as events that don't change the state of the game.
     *
     * @param notifications events that don't change the state of the game
     */
    public void handleNotifications(Notification[] notifications) {
        for (Notification n : notifications)
            view.sendNotification(n.getType().name());
    }

    /**
     * Handles updates. Updates are events that change the state of the game.
     *
     * @param updates events that change the state of the game
     */
    public void handleUpdates(Update[] updates) {
        Arrays.asList(updates).forEach(model::handleUpdate);
    }

    /**
     * Handles a question.
     * It takes care of asking the user of the answer and returns it to the caller.
     *
     * @param message a question
     * @return the answer
     */
    public String handleQuestion(MessageType message) {
        switch (message) {
            case NICKNAME:
                return view.askName();
            default:
                return "";
            //throw new IllegalArgumentException("Unhandled argument: " + message);
        }
    }

    /**
     * Handles a question with options.
     * It takes care of asking the user of the answer and returns it to the caller.
     *
     * @param message a question
     * @param options the options to choose from
     * @return the index of the answer
     */
    public int handleQuestion(MessageType message, String[][] options) {
        List<List<String>> optionsList = new ArrayList<>();
        for (String[] sequence : options)
            optionsList.add(new ArrayList<>(Arrays.asList(sequence)));

        switch (message) {
            case NOTIFICATION:
            case UPDATE:
                throw new IllegalArgumentException("This method does not handle Notifications or Updates");
            case EFFECTS_SEQUENCE:
                return view.chooseEffectSequence(optionsList);
            case SPAWN:
                return view.chooseSpawn(optionsList);
            case POWERUP:
                return view.choosePowerup(optionsList);
            case DESTINATION:
                return view.chooseDestination(optionsList);
            case WEAPON:
                return view.chooseWeapon(optionsList);
            case WEAPON_TO_BUY:
                return view.chooseWeaponToBuy(optionsList);
            case WEAPON_TO_DISCARD:
                return view.chooseWeaponToDiscard(optionsList);
            case WEAPON_TO_RELOAD:
                return view.chooseWeaponToReload(optionsList);
            case ACTION:
                return view.chooseAction(optionsList);
            case POWERUP_FOR_PAYING:
                return view.choosePowerupForPaying(optionsList);
            case USE_TAGBACK:
                return view.chooseUseTagBack(optionsList);
            case TARGET:
                return view.chooseTarget(optionsList);
            default:
                return 0;
            //  throw new IllegalArgumentException("Unhandled argument: " + message);
        }
    }

    /**
     * Sets up an RMI connection.
     */
    public void setConnection() {
        //TODO: implement RMI
    }

    /**
     * Sets up a socket connection.
     *
     * @param ip   the ip of the server
     * @param port the port of the server
     */
    public void setConnection(String ip, int port) throws IOException {
        new Thread(() -> {
            try {
                SocketFromServer fromServer = new SocketFromServer(this,
                        new Socket(ip, port));
                fromServer.startListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Gets the players in the hall of the server
     *
     * @return players in the hall of the server
     * this is why we should use SyncGUI
     */
    public List<String> getPlayersInHall() {
        return model.getConnectedPlayers();
    }

}
