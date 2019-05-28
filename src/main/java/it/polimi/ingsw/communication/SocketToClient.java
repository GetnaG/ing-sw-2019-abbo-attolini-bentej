package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handles the communication through socket, server side.
 * This also ensures that only one communication with the client happens at one
 * time.
 *
 * @author Abbo Giulio A.
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
    SocketToClient(Socket socket) throws ToClientException {
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

            /*Try-with-resources will call close() automatically*/
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(new Gson().toJson(message));
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
     * Handles the communication when there are notifications.
     *
     * @param notification the notification to be sent
     * @throws ToClientException if there are problems with the socket
     */
    private void sendNotification(Notification.NotificationType notification) throws ToClientException {
        send(new ProtocolMessage(new Notification[]{
                new Notification(notification)}));
    }

    /**
     * Handles the communication when there are updates.
     *
     * @param update the update to be sent
     * @throws ToClientException if there are problems with the socket
     */
    private void sendUpdate(Update update) throws ToClientException {
        send(new ProtocolMessage(new Update[]{update}));
    }

    /**
     * Handles the interaction using {@linkplain EffectInterface}.
     * Each choice could be a chain of effects.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected effect
     * @throws ToClientException if there are problems with the socket
     */
    private EffectInterface askEffect(MessageType command,
                                      List<EffectInterface> options)
            throws ToClientException {

        /*Creating a list of names for each chain of effects*/
        List<List<String>> names = new ArrayList<>();
        for (EffectInterface effect : options) {
            List<String> chain = new ArrayList<>();
            effect.forEach(el -> chain.add(el.getName()));
            names.add(chain);
        }

        /*Asking and returning the right element*/
        int choice = sendAndCheck(command, names);
        return options.get(choice);
    }

    /**
     * Handles the interaction using {@linkplain PowerupCard}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected powerup
     * @throws ToClientException if there are problems with the socket
     */
    private PowerupCard askPowerup(MessageType command,
                                   List<PowerupCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(command, options.stream()
                .map(PowerupCard::getId).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using {@linkplain Square}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected square
     * @throws ToClientException if there are problems with the socket
     */
    private Square askSquare(MessageType command, List<Square> options)
            throws ToClientException {
        return options.get(sendAndCheck(command, options.stream()
                .map(Square::getID).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using {@linkplain WeaponCard}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected weapon
     * @throws ToClientException if there are problems with the socket
     */
    private WeaponCard askWeapon(MessageType command,
                                 List<WeaponCard> options)
            throws ToClientException {
        return options.get(sendAndCheck(command, options.stream()
                .map(WeaponCard::getId).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using {@linkplain Action}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected action
     * @throws ToClientException if there are problems with the socket
     */
    private Action askAction(MessageType command, List<Action> options)
            throws ToClientException {
        return options.get(sendAndCheck(command, options.stream()
                .map(Action::getName).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using lists of {@linkplain Damageable}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected damageable list
     * @throws ToClientException if there are problems with the socket
     */
    private List<Damageable> askDamageableList(MessageType command,
                                               List<List<Damageable>> options)
            throws ToClientException {
        int choice = sendAndCheck(command, options.stream()
                .map(o -> o.stream()
                        .map(Damageable::getName).collect(Collectors.toList()))
                .collect(Collectors.toList()));
        return options.get(choice);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options)
            throws ToClientException {
        return askEffect(MessageType.EFFECTS_SEQUENCE, options);
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
        return askPowerup(MessageType.SPAWN, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options)
            throws ToClientException {
        return askPowerup(MessageType.POWERUP, options);
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
        return askSquare(MessageType.DESTINATION, options);
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
        return askWeapon(MessageType.WEAPON, options);
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
        return askWeapon(MessageType.WEAPON_TO_BUY, options);
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
        return askWeapon(MessageType.WEAPON_TO_DISCARD, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options)
            throws ToClientException {
        return askWeapon(MessageType.WEAPON_TO_RELOAD, options);
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
        return askAction(MessageType.ACTION, options);
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
        return askPowerup(MessageType.POWERUP_FOR_PAYING, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options)
            throws ToClientException {
        return askPowerup(MessageType.USE_TAGBACK, options);
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
        return askDamageableList(MessageType.TARGET, options);
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
}
