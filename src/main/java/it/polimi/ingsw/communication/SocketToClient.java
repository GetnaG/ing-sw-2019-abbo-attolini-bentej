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
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class SocketToClient implements ToClientInterface {
    private Socket socket;

    public SocketToClient(Socket socket) {
        this.socket = socket;
    }

    private String askWaitAndCheck(String name, List<String> options) /*throws IOException*/ {
        String choice = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(name);
            out.println(SocketProtocol.START_OF_LIST.getCommand());
            for (String s : options)
                out.println(s);
            out.println(SocketProtocol.END_OF_LIST.getCommand());
            choice = in.readLine();
        } catch (IOException e) {
            //TODO throw new IOException(e);
        }
        if (choice != null && options.contains(choice))
            return choice;
        return options.get(0);
        //TODO throw new exception wrong choice
    }

    private int listAskWaitAndCheck(String name, List<List<String>> options) /*throws IOException*/ {
        int choice = -1;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(name);
            out.println(SocketProtocol.START_LIST_OF_LISTS.getCommand());
            for (List<String> list : options) {
                out.println(SocketProtocol.START_OF_LIST.getCommand());
                for (String s : list)
                    out.println(s);
                out.println(SocketProtocol.END_OF_LIST.getCommand());
            }
            out.println(SocketProtocol.END_LIST_OF_LISTS.getCommand());
            choice = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            //TODO throw new IOException(e);
        }
        if (choice >= 0 && choice < options.size())
            return choice;
        return 0;
        //TODO throw new exception wrong choice
    }

    @Override
    public String chooseUserName() {
        String choice = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(SocketProtocol.NICKNAME.getCommand());
            choice = in.readLine();
        } catch (IOException e) {
            //TODO throw new IOException(e);
        }
        if (choice != null)
            return choice;
        return ""; //TODO throw new exception wrong choice
    }

    private EffectInterface askEffect(String name, List<EffectInterface> options) {
        String choice = askWaitAndCheck(name,
                options.stream().map(EffectInterface::getName).collect(Collectors.toList()));
        for (EffectInterface i : options)
            if (i.getName().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private PowerupCard askPowerup(String name, List<PowerupCard> options) {
        String choice = askWaitAndCheck(name,
                options.stream().map(PowerupCard::getId).collect(Collectors.toList()));
        for (PowerupCard i : options)
            if (i.getId().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private Square askSquare(String name, List<Square> options) {
        String choice = askWaitAndCheck(name,
                options.stream().map(Square::getID).collect(Collectors.toList()));
        for (Square i : options)
            if (i.getID().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private WeaponCard askWeapon(String name, List<WeaponCard> options) {
        String choice = askWaitAndCheck(name,
                options.stream().map(WeaponCard::getId).collect(Collectors.toList()));
        for (WeaponCard i : options)
            if (i.getId().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private Action askAction(String name, List<Action> options) {
        String choice = askWaitAndCheck(name,
                options.stream().map(Action::getName).collect(Collectors.toList()));
        for (Action i : options)
            if (i.getName().equalsIgnoreCase(choice))
                return i;
        return options.get(0);//FIXME unchecked exception
    }

    private List<Damageable> askDamageableList(String name, List<List<Damageable>> options) {
        int choice = listAskWaitAndCheck(name,
                options.stream().map(o -> o.stream().map(Damageable::getName).collect(Collectors.toList())).collect(Collectors.toList()));
        return options.get(choice);
        //FIXME unchecked exception
    }


    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options) {
        return askEffect(SocketProtocol.EFFECTS_SEQUENCE.getCommand(), options);
    }

    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> options) {
        return askPowerup(SocketProtocol.SPAWN.getCommand(), options);
    }

    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options) {
        return askPowerup(SocketProtocol.POWERUP.getCommand(), options);
    }

    @Override
    public Square chooseDestination(List<Square> options) {
        return askSquare(SocketProtocol.DESTINATION.getCommand(), options);
    }

    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options) {
        return askWeapon(SocketProtocol.WEAPON_CARD.getCommand(), options);
    }

    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) {
        return askWeapon(SocketProtocol.WEAPON_TO_BUY.getCommand(), options);
    }

    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) {
        return askWeapon(SocketProtocol.WEAPON_TO_DISCARD.getCommand(), options);
    }

    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options) {
        return askWeapon(SocketProtocol.WEAPON_TO_RELOAD.getCommand(), options);
    }

    @Override
    public Action chooseAction(List<Action> options) {
        return askAction(SocketProtocol.ACTION.getCommand(), options);
    }

    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options) {
        return askPowerup(SocketProtocol.POWERUP_FOR_PAYING.getCommand(), options);
    }

    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options) {
        return askPowerup(SocketProtocol.USE_TAGBACK.getCommand(), options);
    }

    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options) {
        return askDamageableList(SocketProtocol.TARGET.getCommand(), options);
    }
}
