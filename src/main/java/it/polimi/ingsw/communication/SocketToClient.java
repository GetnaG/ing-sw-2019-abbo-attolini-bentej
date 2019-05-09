package it.polimi.ingsw.communication;

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
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handles the communication through socket, server side.
 * This also ensures that only one communication with the client happens at one
 * time.
 *
 * @author Abbo Giulio A.
 * @see SocketProtocol
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
    SocketToClient(Socket socket) {
        this.socket = socket;
    }

    /**
     * Handles the communication for a list of choices.
     *
     * @param command the message to be sent
     * @param options the option to choose from
     * @return the selected option
     * @throws ToClientException if there are problems with the socket
     */
    private String askWaitAndCheck(SocketProtocol command, List<String> options)
            throws ToClientException {

        /*Sending the data an retrieving the answer*/
        String choice;
        synchronized (socket) {

            /*Try-with-resources will call close() automatically*/
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(),
                         true)
            ) {
                out.println(command);
                out.println(SocketProtocol.PROTOCOL_LIST);
                for (String s : options)
                    out.println(s);
                out.println(SocketProtocol.PROTOCOL_END_LIST);
                choice = in.readLine();
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }

        /*Checking if the answer was one of the options*/
        if (choice != null && options.contains(choice))
            return choice;

        /*The answer is not valid: sending an error and asking again*/
        sendError(SocketProtocol.PROTOCOL_ERR_CHOICE);
        return askWaitAndCheck(command, options);
    }

    /**
     * Handles the communication for a list of lists.
     *
     * @param command the message to be sent
     * @param options the option to choose from
     * @return the index of the selected option
     * @throws ToClientException if there are problems with the socket
     */
    private int listAskWaitAndCheck(SocketProtocol command,
                                    List<List<String>> options) throws ToClientException {

        /*Sending the data an retrieving the answer*/
        int choice = -1;
        synchronized (socket) {

            /*Try-with-resources will call close() automatically*/
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(),
                         true)
            ) {
                out.println(command);
                out.println(SocketProtocol.PROTOCOL_MULTI);
                for (List<String> list : options) {
                    out.println(SocketProtocol.PROTOCOL_LIST);
                    for (String s : list)
                        out.println(s);
                    out.println(SocketProtocol.PROTOCOL_END_LIST);
                }
                out.println(SocketProtocol.PROTOCOL_END_MULTI);
                choice = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }

        }

        /*Checking if the answer is valid.*/
        if (choice >= 0 && choice < options.size())
            return choice;

        /*The answer is not valid: sending an error and asking again*/
        sendError(SocketProtocol.PROTOCOL_ERR_CHOICE);
        return listAskWaitAndCheck(command, options);
    }

    private void sendError(SocketProtocol command) throws ToClientException {
        synchronized (socket) {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(),
                         true)
            ) {
                out.println(command);
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }
    }

    private String askAndWait(SocketProtocol name) throws ToClientException {
        String choice = null;
        synchronized (socket) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(name);
                choice = in.readLine();
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }
        if (choice != null)
            return choice;
        return ""; //TODO throw new exception wrong choice
    }


    private EffectInterface askEffect(SocketProtocol name, List<EffectInterface> options) throws ToClientException {
        String choice = askWaitAndCheck(name,
                options.stream().map(EffectInterface::getName).collect(Collectors.toList()));
        for (EffectInterface i : options)
            if (i.getName().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private PowerupCard askPowerup(SocketProtocol name, List<PowerupCard> options) throws ToClientException {
        String choice = askWaitAndCheck(name,
                options.stream().map(PowerupCard::getId).collect(Collectors.toList()));
        for (PowerupCard i : options)
            if (i.getId().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private Square askSquare(SocketProtocol name, List<Square> options) throws ToClientException {
        String choice = askWaitAndCheck(name,
                options.stream().map(Square::getID).collect(Collectors.toList()));
        for (Square i : options)
            if (i.getID().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private WeaponCard askWeapon(SocketProtocol name, List<WeaponCard> options) throws ToClientException {
        String choice = askWaitAndCheck(name,
                options.stream().map(WeaponCard::getId).collect(Collectors.toList()));
        for (WeaponCard i : options)
            if (i.getId().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private Action askAction(SocketProtocol name, List<Action> options) throws ToClientException {
        String choice = askWaitAndCheck(name,
                options.stream().map(Action::getName).collect(Collectors.toList()));
        for (Action i : options)
            if (i.getName().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private List<Damageable> askDamageableList(SocketProtocol name, List<List<Damageable>> options) throws ToClientException {
        int choice = listAskWaitAndCheck(name,
                options.stream().map(o -> o.stream().map(Damageable::getName).collect(Collectors.toList())).collect(Collectors.toList()));
        return options.get(choice);
        //FIXME unchecked exception
    }


    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options) throws ToClientException {
        return askEffect(SocketProtocol.EFFECTS_SEQUENCE, options);
    }

    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> options) throws ToClientException {
        return askPowerup(SocketProtocol.SPAWN, options);
    }

    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options) throws ToClientException {
        return askPowerup(SocketProtocol.POWERUP, options);
    }

    @Override
    public Square chooseDestination(List<Square> options) throws ToClientException {
        return askSquare(SocketProtocol.DESTINATION, options);
    }

    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options) throws ToClientException {
        return askWeapon(SocketProtocol.WEAPON, options);
    }

    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) throws ToClientException {
        return askWeapon(SocketProtocol.WEAPON_TO_BUY, options);
    }

    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) throws ToClientException {
        return askWeapon(SocketProtocol.WEAPON_TO_DISCARD, options);
    }

    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options) throws ToClientException {
        return askWeapon(SocketProtocol.WEAPON_TO_RELOAD, options);
    }

    @Override
    public Action chooseAction(List<Action> options) throws ToClientException {
        return askAction(SocketProtocol.ACTION, options);
    }

    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options) throws ToClientException {
        return askPowerup(SocketProtocol.POWERUP_FOR_PAYING, options);
    }

    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options) throws ToClientException {
        return askPowerup(SocketProtocol.USE_TAGBACK, options);
    }

    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options) throws ToClientException {
        return askDamageableList(SocketProtocol.TARGET, options);
    }

    @Override
    public String chooseUserName() throws ToClientException {
        return askAndWait(SocketProtocol.NICKNAME);
    }
}
