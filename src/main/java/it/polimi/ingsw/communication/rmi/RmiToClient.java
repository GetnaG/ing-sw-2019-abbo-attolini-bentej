package it.polimi.ingsw.communication.rmi;

import it.polimi.ingsw.communication.CommunicationHelper;
import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.Update;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class adapts the remote object provided by the client to the
 * mechanics of the server.
 *
 * @author Abbo Giulio A.
 */
public class RmiToClient implements ToClientInterface {
    /**
     * The remote object provided by the client.
     */
    private RmiFromClientInterface client;

    /**
     * Creates an object with the provided remote object.
     *
     * @param fromClient the remote object provided by the client
     */
    RmiToClient(RmiFromClientInterface fromClient) {
        client = fromClient;
    }

    /**
     * Ask a question to the client, with options.
     *
     * @param type    the type of question
     * @param options the possible sequences to choose
     * @return the index of the selected sequence
     * @throws ToClientException if there are problems with RMi
     */
    private int askAndCheck(MessageType type,
                            List<? extends List<String>> options) throws ToClientException {
        try {
            int choice = client.handleQuestion(type, options.stream()
                    .map(a -> a.toArray(new String[]{}))
                    .collect(Collectors.toList())
                    .toArray(new String[][]{{}}));

            /*Checking if the answer is valid*/
            if (choice >= 0 && choice < options.size())
                return choice;

            /*The answer is not valid: sending an error and asking again*/
            sendNotification(Notification.NotificationType.ERROR);
            return askAndCheck(type, options);
        } catch (RemoteException e) {
            throw new ToClientException("Rmi exception, question with options", e);
        }
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.EFFECTS_SEQUENCE,
                new CommunicationHelper().askEffect(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.SPAWN,
                new CommunicationHelper().askPowerup(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.POWERUP,
                new CommunicationHelper().askPowerup(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public Square chooseDestination(List<Square> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.DESTINATION,
                new CommunicationHelper().askSquare(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.WEAPON,
                new CommunicationHelper().askWeapon(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.WEAPON_TO_BUY,
                new CommunicationHelper().askWeapon(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.WEAPON_TO_DISCARD,
                new CommunicationHelper().askWeapon(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.WEAPON_TO_RELOAD,
                new CommunicationHelper().askWeapon(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public Action chooseAction(List<Action> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.ACTION,
                new CommunicationHelper().askAction(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.POWERUP_FOR_PAYING,
                new CommunicationHelper().askPowerup(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.USE_TAGBACK,
                new CommunicationHelper().askPowerup(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options)
            throws ToClientException {
        return options.get(askAndCheck(MessageType.TARGET,
                new CommunicationHelper().askDamageableList(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client responds.
     *
     * @throws ToClientException if there are problems whit RMI
     */
    @Override
    public String chooseUserName() throws ToClientException {
        try {
            return client.handleQuestion(MessageType.NICKNAME);
        } catch (RemoteException e) {
            throw new ToClientException("Rmi exception when asking", e);
        }
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the connection is closed.
     *
     * @throws ToClientException if there are problems with RMI
     */
    @Override
    public void quit() throws ToClientException {
        sendNotification(Notification.NotificationType.QUIT);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the clients sends an ack.
     *
     * @throws ToClientException if there are problems with RMI
     */
    @Override
    public void sendNotification(Notification.NotificationType type) throws ToClientException {
        try {
            client.handleNotifications(new Notification[]{new Notification(type)});
        } catch (RemoteException e) {
            throw new ToClientException("Rmi exception in notifications", e);
        }
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the clients sends an ack.
     *
     * @throws ToClientException if there are problems with RMI
     */
    @Override
    public void sendUpdate(Update update) throws ToClientException {
        try {
            client.handleUpdates(new Update[]{update});
        } catch (RemoteException e) {
            throw new ToClientException("Rmi exception in updates", e);
        }
    }
}
