package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.Update;
import it.polimi.ingsw.communication.rmi.RmiFromClientInterface;
import it.polimi.ingsw.communication.rmi.RmiInversionInterface;
import it.polimi.ingsw.communication.socket.SocketFromServer;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents the Controller of the client.
 * It elaborates the received Notifications, Updates and Commands.
 * A controller receives orders from the servers and redirects them to the
 * local model and to the user through the view.
 *
 * @author Fahed B. Tej
 * @author giubots
 */
public class ClientController extends UnicastRemoteObject implements RmiFromClientInterface {
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
    public ClientController(MatchState model, InteractionInterface view) throws RemoteException {
        this.model = model;
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleNotifications(Notification[] notifications) {
        for (Notification n : notifications) {
            view.sendNotification(n.getType().name());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUpdates(Update[] updates) {
        model.handleUpdate(updates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handleQuestion(MessageType message) {
        if (message == MessageType.NICKNAME) {
            return view.askName();
        }
        throw new IllegalArgumentException("Unhandled argument: " + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
                throw new IllegalArgumentException("Unhandled argument: " + message);
        }
    }

    /**
     * Sets up an RMI connection.
     *
     * @param ip the ip of the registry
     */
    public void setRmi(String ip) throws IOException {
        try {
            ((RmiInversionInterface) LocateRegistry.getRegistry(ip).lookup(
                    "inversion")).invert(this);
        } catch (RemoteException | NotBoundException e) {
            throw new IOException(e);
        }
    }

    /**
     * Sets up a socket connection.
     *
     * @param ip   the ip of the server
     * @param port the port of the server
     */
    public void setSocket(String ip, int port) throws IOException {
        SocketFromServer fromServer = new SocketFromServer(this, new Socket(ip, port));
        new Thread(() -> {
            try {
                fromServer.startListening();
            } catch (IOException e) {
                ClientMain.LOG.log(Level.SEVERE, "Socket exception", e);
                System.exit(-1);
            }
        }).start();
    }
}
