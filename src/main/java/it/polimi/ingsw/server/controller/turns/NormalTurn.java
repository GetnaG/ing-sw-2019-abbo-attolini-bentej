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
                      Consumer<UpdateBuilder> updater){
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
     * @param currentPlayer 
     * @param board GameBoard
     *
     * @return -1 if Final Frenzy is triggered, else 0
     */
    public int startTurn(Player currentPlayer, GameBoard board) {
        //TODO Refractor
        this.player = currentPlayer;
        this.board = board;

        for (int i = 0; i < 2; i++){
            askAndRunPowerup();
            askAndRunAction();
        }
        askAndRunPowerup();
        askAndReload();

        updater.accept(new UpdateBuilder()); //TODO: add here what changed
        if (isFinalFrenzyTriggered)
            return -1;
        return 0;
    }

    /**
     * Asks the player if he wants to use a Powerup Card. Then it runs its effect.
     */
    private void askAndRunPowerup(){//TODO: check if usable as action (and cost when will be implemented)

        if (player.getAllPowerup().isEmpty())
            return;

        PowerupCard card = null;
        try {
            List<PowerupCard> powerupCards = player.getAllPowerup().stream().filter(c -> c.isUsableAsAction()).collect(Collectors.toList());
            card = player.getToClient().choosePowerup(powerupCards);
        } catch (ToClientException | ChoiceRefusedException e) {
            // (1) default move : nothing
            // (2) suspend player: already done by the User class (calls matchSuspensionListener)
            return;
        }

        if (card != null)
            card.getEffect().runEffect(player, allTargets, board, alreadyTargeted,
                    new ArrayList<>());
    }

    /**
     * Asks the player to choose from a list of Actions and runs that action.
     */
    private void askAndRunAction() {
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

        Action chosenAction = null;
        try {
            chosenAction = player.getToClient().chooseAction(actions);
        } catch (ToClientException e) {
            // (1) default move : does nothing
            // (2) suspend player: already done by the User class (calls matchSuspensionListener)
            return;
        }

        chosenAction.runEffect(player, allTargets, board, alreadyTargeted, new ArrayList<>());

    }

    /**
     * Asks the player to choose whether to reload. Then it takes care of reloading.
     */
    private void askAndReload() {
        isFinalFrenzyTriggered = board.checkFinalFrenzy();

        if (isFinalFrenzyTriggered)
            return;
        WeaponCard cardToReload;

        List<WeaponCard> weaponCards = player.getAllWeapons().stream()
                .filter(x -> player.canAfford(x.getCost(), false))
                .collect(Collectors.toList());

        try {
            cardToReload = player.getToClient().chooseWeaponToReload(weaponCards);
            if (cardToReload != null)
                player.reload(cardToReload);
        } catch (ToClientException | ChoiceRefusedException e) {
            // (1) default move : nothing
            // (2) suspend player: already done by the User class (calls matchSuspensionListener)
            return;
        }
    }

}