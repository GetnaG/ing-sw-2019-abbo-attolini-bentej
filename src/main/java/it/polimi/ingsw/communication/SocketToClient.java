package it.polimi.ingsw.communication;

import com.google.gson.Gson;
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
 * @see ProtocolType
 */
public class SocketToClient implements ToClientInterface {
    /**
     * The socket through which communicate.
     */
    private final Socket socket;

    /**
     * This sets the socket.
     *
     * @param socket the socket through which communicate
     */
    SocketToClient(Socket socket) throws ToClientException {
        this.socket = socket;
        sendNotification(Notification.NotificationType.GREET);
    }

    private ProtocolMessage send(ProtocolMessage message) throws ToClientException {
        ProtocolMessage answer;
        synchronized (socket) {

            /*Try-with-resources will call close() automatically*/
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println(new Gson().toJson(message));

                answer = new Gson().fromJson(in.readLine(),
                        ProtocolMessage.class);
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }
        return answer;
    }

    /**
     * Handles the communication for a list of lists.
     * This keeps asking the client if the answer is not valid.
     *
     * @param command the message to be sent
     * @param options the option to choose from
     * @return the index of the selected option
     * @throws ToClientException if there are problems with the socket
     */
    private int sendAndCheck(ProtocolType command, List<List<String>> options) throws ToClientException {
        try {
            int choice = Integer.parseInt(
                    send(new ProtocolMessage(command, options)).getUserChoice());

            /*Checking if the answer is valid.*/
            if (choice >= 0 && choice < options.size())
                return choice;
        } catch (NumberFormatException ignored) {
        }

        /*The answer is not valid: sending an error and asking again*/
        sendNotification(Notification.NotificationType.ERROR);
        return sendAndCheck(command, options);
    }

    private void sendNotification(Notification.NotificationType notification) throws ToClientException {
        send(new ProtocolMessage(new Notification[]{
                new Notification(notification)}));
    }

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
    private EffectInterface askEffect(ProtocolType command,
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
    private PowerupCard askPowerup(ProtocolType command,
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
    private Square askSquare(ProtocolType command, List<Square> options)
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
    private WeaponCard askWeapon(ProtocolType command,
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
    private Action askAction(ProtocolType command, List<Action> options)
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
    private List<Damageable> askDamageableList(ProtocolType command,
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
        return askEffect(ProtocolType.EFFECTS_SEQUENCE, options);
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
        return askPowerup(ProtocolType.SPAWN, options);
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
        return askPowerup(ProtocolType.POWERUP, options);
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
        return askSquare(ProtocolType.DESTINATION, options);
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
        return askWeapon(ProtocolType.WEAPON, options);
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
        return askWeapon(ProtocolType.WEAPON_TO_BUY, options);
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
        return askWeapon(ProtocolType.WEAPON_TO_DISCARD, options);
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
        return askWeapon(ProtocolType.WEAPON_TO_RELOAD, options);
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
        return askAction(ProtocolType.ACTION, options);
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
        return askPowerup(ProtocolType.POWERUP_FOR_PAYING, options);
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
        return askPowerup(ProtocolType.USE_TAGBACK, options);
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
        return askDamageableList(ProtocolType.TARGET, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public String chooseUserName() throws ToClientException {
        return send(new ProtocolMessage(ProtocolType.NICKNAME)).getUserChoice();
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
