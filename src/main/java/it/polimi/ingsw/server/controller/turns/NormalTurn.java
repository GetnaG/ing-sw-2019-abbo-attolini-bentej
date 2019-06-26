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
    /**
     * Current Player
     */
    private Player player;
    /**
     * Board used in the Game.
     */
    private GameBoard board;
    /**
     * Possible Actions
     */
    private List<Action> actions;
    /**
     * Already hitted targets
     */
    private List<Damageable> alreadyTargeted;

    /**
     * True if final frenzy is triggered
     */
    private boolean isFinalFrenzyTriggered;

    private Consumer<UpdateBuilder> updater;

    private List<Damageable> allTargets;

    /**
     * Creates a normal turn.
     */
    public NormalTurn(Player currentPlayer, List<Damageable> allTargets, GameBoard board,
                      Consumer<UpdateBuilder> updater) {
        this.player = currentPlayer;
        this.board = board;
        this.alreadyTargeted = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.isFinalFrenzyTriggered = false;
        this.updater = updater;
        this.allTargets = allTargets;
    }

    /**
     * A player takes two actions then he has the option to reload. Between each of these steps he is asked if he want to use a Powerup Card.
     *
     * @param currentPlayer
     * @param board         GameBoard
     * @return -1 if Final Frenzy is triggered, else 0
     */
    public int startTurn(Player currentPlayer, GameBoard board) {
        //TODO Refractor
        this.player = currentPlayer;
        this.board = board;

        try {
            askAndRunPowerup();
            askAndRunAction();
            askAndRunPowerup();
            askAndRunAction();
            askAndRunPowerup();
            askAndReload();
        } catch (ToClientException e) {
            /*The execution stopped with an exception: interrupting the turn*/
        }

        updater.accept(new UpdateBuilder()); //TODO: add here what changed
        if (isFinalFrenzyTriggered)
            return -1;
        return 0;
    }

    /**
     * Asks the player if he wants to use a Powerup Card. Then it runs its effect.
     */
    private void askAndRunPowerup() throws ToClientException {

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
            card.getEffect().runEffect(player, allTargets, board, alreadyTargeted, new ArrayList<>());
        }
    }

    /**
     * Asks the player to choose from a list of Actions and runs that action.
     */
    private void askAndRunAction() throws ToClientException {
        isFinalFrenzyTriggered = board.checkFinalFrenzy();
        if (isFinalFrenzyTriggered)
            return;

        actions = new ArrayList<>();
        List<EffectInterface> tripleMove = new ArrayList<>();

        tripleMove.add(new Move());
        tripleMove.add(new Move());
        tripleMove.add(new Move());

        actions.add(new Action("TripleMove", tripleMove));
        actions.add(new Action("Grab", new Grab()));
        actions.add(new Action("Shoot", new Shoot()));
        actions.addAll(player.getAdrenalineActions());

        player.getToClient().chooseAction(actions)
                .runAll(player, allTargets, board, alreadyTargeted, new ArrayList<>());

    }

    /**
     * Asks the player to choose whether to reload. Then it takes care of reloading.
     */
    private void askAndReload() throws ToClientException {
        isFinalFrenzyTriggered = board.checkFinalFrenzy();
        if (isFinalFrenzyTriggered)
            return;

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
    }

}