package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ChoiceRefusedException;
import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.controller.effects.*;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A normal turn is composed by two phases. In the first phase, the player chooses two from three possible actions. In the final phase, the player
 * chooses whether to reload.
 *
 * @author Fahed Ben Tej
 */
public class NormalTurn implements TurnInterface {
    private Consumer<UpdateBuilder> updater;

    /**
     * Creates a normal turn.
     */
    public NormalTurn(Consumer<UpdateBuilder> updater) {
        this.updater = updater;
    }

    /**
     * A player takes two actions then he has the option to reload. Between each of these steps he is asked if he want to use a Powerup Card.
     */
    public void startTurn(Player subjectPlayer, List<Damageable> allTargets, GameBoard board) {
        try {
            askAndRunPowerup(subjectPlayer, allTargets, board);
            askAndRunAction(subjectPlayer, allTargets, board);
            askAndRunPowerup(subjectPlayer, allTargets, board);
            askAndRunAction(subjectPlayer, allTargets, board);
            askAndRunPowerup(subjectPlayer, allTargets, board);
            askAndReload(subjectPlayer, updater);
        } catch (ToClientException e) {
            /*The execution stopped with an exception: interrupting the turn*/
            updater.accept(null);
        }
    }

    /**
     * Asks the player if he wants to use a Powerup Card. Then it runs its effect.
     */
    private void askAndRunPowerup(Player player, List<Damageable> allTargets, GameBoard board) throws ToClientException {

        if (player.getAllPowerup().isEmpty())
            return;

        PowerupCard card = null;
        try {
            List<PowerupCard> powerupCards = player.getAllPowerup().stream()
                    .filter(PowerupCard::isUsableAsAction)
                    .filter(c -> player.canAfford(c.getEffect().getCost(), false))
                    .collect(Collectors.toList());
            if (!powerupCards.isEmpty())
                card = player.getToClient().choosePowerup(powerupCards);
        } catch (ChoiceRefusedException e) {
            return;
        }

        if (card != null) {
            player.pay(card.getEffect().getCost());
            player.removePowerup(card);
            board.putPowerupCard(card);
            card.getEffect().runEffect(player, allTargets, board, new ArrayList<>(), new ArrayList<>());
            updater.accept(null);
        }
    }

    /**
     * Asks the player to choose from a list of Actions and runs that action.
     */
    private void askAndRunAction(Player player, List<Damageable> allTargets, GameBoard board) throws ToClientException {
        List<Action> actions = new ArrayList<>();
        List<EffectInterface> tripleMove = new ArrayList<>();

        tripleMove.add(new Move());
        tripleMove.add(new Move());
        tripleMove.add(new Move());

        actions.add(new Action("TripleMove", tripleMove));
        actions.add(new Action("Move&Grab", Arrays.asList(new Move(), new Grab())));
        actions.add(new Action("Shoot", new Shoot()));
        actions.addAll(player.getAdrenalineActions());

        for (EffectInterface effect : player.getToClient().chooseAction(actions)) {
            effect.runEffect(player, allTargets, board, new ArrayList<>(), new ArrayList<>());
            updater.accept(null);
        }
    }

    /**
     * Asks the player to choose whether to reload. Then it takes care of reloading.
     */
    static void askAndReload(Player player, Consumer<UpdateBuilder> updater) throws ToClientException {

        /*Taking the reloadable weapons that the player can afford*/
        List<WeaponCard> weaponCards = player.getReloadableWeapons().stream()
                .filter(x -> player.canAfford(x.getCost(), false) ||
                        !player.canAffordWithPowerups(x.getCost(), false).isEmpty())
                .collect(Collectors.toList());

        /*Choosing the card to reload*/
        WeaponCard cardToReload;
        try {
            cardToReload = player.getToClient().chooseWeaponToReload(weaponCards);
        } catch (ChoiceRefusedException e) {
            return;
        }

        /*Choosing a powerup to pay if the player can not afford reloading*/
        PowerupCard toPay = null;
        if (!player.canAfford(cardToReload.getCost(), false))
            toPay = player.getToClient().choosePowerupForPaying(
                    player.canAffordWithPowerups(cardToReload.getCost(), false));

        player.reload(cardToReload, toPay);
        updater.accept(new UpdateBuilder()
                .setLoadedWeapons(player, player.getLoadedWeapons())
                .setUnloadedWeapon(player, player.getReloadableWeapons())
                .setPowerupsInHand(player, player.getAllPowerup())
                .setActiveCubes(player, player.getAmmoCubes()));
    }
}