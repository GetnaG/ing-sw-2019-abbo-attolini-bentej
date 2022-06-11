package it.polimi.ingsw.communication.socket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.communication.*;
import it.polimi.ingsw.communication.protocol.MessageType;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.ProtocolMessage;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * This class handles the communication through socket, server side.
 * This also ensures that only one communication with the client happens at one
 * time.
 *
 * @author giubots
 * @see ProtocolMessage
 */
public class SocketToClient implements ToClientInterface {
    /**
     * The socket through which communicate.
     */
    private final Socket socket;

    /**
     * This sets the socket and sends a test notification.
     *
     * @param socket the socket through which communicate
     */
    public SocketToClient(Socket socket) throws ToClientException {
        this.socket = socket;
        sendNotification(Notification.NotificationType.GREET);
    }

    /**
     * Sends the provided message through the socket ans returns the response.
     * This should be used when there are no options tho choose from.
     *
     * @param message the message to be sent
     * @return the client's answer
     * @throws ToClientException if there are problems with the socket
     */
    private ProtocolMessage send(ProtocolMessage message) throws ToClientException {
        ProtocolMessage answer;
        synchronized (socket) {
            try {

                /*Setting up*/
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                /*Sending*/
                out.println(new Gson().toJson(message));

                /*Waiting answer*/
                String input = null;
                while (input == null)
                    input = in.readLine();
                answer = new Gson().fromJson(input, ProtocolMessage.class);
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            } catch (JsonSyntaxException e) {

                /*Ignoring the answer, asking again*/
                return send(message);
            }
        }
        return answer;
    }

    /**
     * Handles the communication when there are options attached to the message.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the index of the selected option
     * @throws ToClientException if there are problems with the socket
     */
    private int sendAndCheck(MessageType command, List<? extends List<String>> options) throws ToClientException {
        try {
            int choice = Integer.parseInt(send(new ProtocolMessage(command, options)).getUserChoice());

            /*Checking if the answer is valid*/
            if (choice >= 0 && choice < options.size())
                return choice;
        } catch (NumberFormatException ignored) {
            /*Continuing if the answer was not a number*/
        }

        /*The answer is not valid: sending an error and asking again*/
        sendNotification(Notification.NotificationType.ERROR);
        return sendAndCheck(command, options);
    }

    /**
     * Handles the communication when there are options attached to the
     * message and it can be refused.
     *
     * @param command the message to be sent
     * @param options the options to choose from, with the refuse option
     * @return the index of the selected option
     * @throws ToClientException      if there are problems with the socket
     * @throws ChoiceRefusedException if the client refuses
     */
    private int refusableSendAndCheck(MessageType command, List<? extends List<String>> options)
            throws ToClientException, ChoiceRefusedException {
        int choice = sendAndCheck(command, options);

        if (options.get(choice).get(0).equals(CommunicationHelper.CHOICE_REFUSED))
            throw new ChoiceRefusedException();

        return choice;
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public Action chooseEffectsSequence(List<Action> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.EFFECTS_SEQUENCE,
                new CommunicationHelper().askEffect(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.SPAWN,
                new CommunicationHelper().askPowerup(options, false)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException      if there are problems whit the socket
     * @throws ChoiceRefusedException if the client refuses
     */
    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options)
            throws ToClientException, ChoiceRefusedException {
        return options.get(refusableSendAndCheck(MessageType.POWERUP,
                new CommunicationHelper().askPowerup(options, true)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public Square chooseDestination(List<Square> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.DESTINATION,
                new CommunicationHelper().askSquare(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.WEAPON,
                new CommunicationHelper().askWeapon(options, false)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.WEAPON_TO_BUY,
                new CommunicationHelper().askWeapon(options, false)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.WEAPON_TO_DISCARD,
                new CommunicationHelper().askWeapon(options, false)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException      if there are problems whit the socket
     * @throws ChoiceRefusedException if the client refuses
     */
    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options)
            throws ToClientException, ChoiceRefusedException {
        return options.get(refusableSendAndCheck(MessageType.WEAPON_TO_RELOAD,
                new CommunicationHelper().askWeapon(options, true)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public Action chooseAction(List<Action> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.ACTION,
                new CommunicationHelper().askAction(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.POWERUP_FOR_PAYING,
                new CommunicationHelper().askPowerup(options, false)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException      if there are problems whit the socket
     * @throws ChoiceRefusedException if the client refuses
     */
    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options)
            throws ToClientException, ChoiceRefusedException {
        return options.get(refusableSendAndCheck(MessageType.USE_TAGBACK,
                new CommunicationHelper().askPowerup(options, true)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options)
            throws ToClientException {
        return options.get(sendAndCheck(MessageType.TARGET,
                new CommunicationHelper().askDamageableList(options)));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client responds.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public String chooseUserName() throws ToClientException {
        return send(new ProtocolMessage(MessageType.NICKNAME)).getUserChoice();
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the connection is closed.
     *
     * @throws ToClientException if there are problems with the socket
     */
    @Override
    public void quit() throws ToClientException {
        sendNotification(Notification.NotificationType.QUIT);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the clients sends an ack.
     *
     * @throws ToClientException if there are problems with the socket
     */
    @Override
    public void sendNotification(Notification.NotificationType notification) throws ToClientException {
        send(new ProtocolMessage(new Notification[]{
                new Notification(notification)}));
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the clients sends an ack.
     *
     * @param update
     * @throws ToClientException if there are problems with the socket
     */
    @Override
    public void sendUpdate(UpdateBuilder update) throws ToClientException {
        send(new ProtocolMessage(update.build()));
    }
}
