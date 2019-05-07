package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.ToClientInterface;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.net.Socket;
import java.util.List;

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

    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options) {
        return null;
    }

    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> option) {
        return null;
    }

    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options) {
        return null;
    }

    @Override
    public Square chooseDestination(List<Square> options) {
        return null;
    }

    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options) {
        return null;
    }

    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) {
        return null;
    }

    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) {
        return null;
    }

    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options) {
        return null;
    }

    @Override
    public Action chooseAction(List<Action> options) {
        return null;
    }

    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options) {
        return null;
    }

    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options) {
        return null;
    }

    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options) {
        return null;
    }
}
