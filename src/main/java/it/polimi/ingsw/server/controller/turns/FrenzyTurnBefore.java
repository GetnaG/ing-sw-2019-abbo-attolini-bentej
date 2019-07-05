package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.controller.effects.*;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Once Final Frenzy is triggered, every player has one extra turn.
 * If a Player turn comes before a the First Player, then he has access to certain actions.
 * If his turn comes after, then he has access to other different actions.
 *
 * @author Fahed Ben Tej
 * @see FrenzyTurnBefore
 * @see FrenzyTurnAfter
 */
public class FrenzyTurnBefore implements TurnInterface {
    private Consumer<UpdateBuilder> updater;

    /**
     * Construct a FrenzyTurn
     */
    public FrenzyTurnBefore(Consumer<UpdateBuilder> updater) {
        this.updater = updater;
    }

    /**
     * A player can choose from the following actions :
     * (1) Move up to 1 square, reload if you want, then shoot.
     * (2) Move up to 4 squares.
     * (3) Move up to 2 squares and grab something there.
     * At the end of the turn there is no point in reloading because his match is over.
     */
    public void startTurn(Player subjectPlayer, List<Damageable> allTargets, GameBoard board) {
        List<Action> actions = new ArrayList<>();
        actions.add(getFirstFrenzyActionBefore());
        actions.add(getSecondFrenzyActionBefore());
        actions.add(getThirdFrenzyActionBefore());

        try {
            for (EffectInterface effect : subjectPlayer.getToClient().chooseAction(actions)) {
                effect.runEffect(subjectPlayer, allTargets, board, new ArrayList<>(), new ArrayList<>());
                updater.accept(null);
            }
        } catch (ToClientException e) {
            updater.accept(null);
        }
    }

    /**
     * Gets the first possible action.
     *
     * @return the first possible action.
     */
    private Action getFirstFrenzyActionBefore() {
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(getReloadAction());
        steps.add(new Shoot());

        return new Action("FirstFrenzyActionBefore", steps);
    }

    /**
     * Gets the second possible action.
     *
     * @return the second possible action.
     */
    private Action getSecondFrenzyActionBefore() {
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Move());

        return new Action("SecondFrenzyActionBefore", steps);
    }

    /**
     * Gets the third possible action.
     *
     * @return the third possible action.
     */
    private Action getThirdFrenzyActionBefore() {
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Grab());

        return new Action("ThirdFrenzyActionBefore", steps);
    }

    /**
     * Creates an action that prompts the player to reload.
     *
     * @return an action that prompts the player to reload
     */
    private EffectInterface getReloadAction() {
        return new EffectInterface() {
            @Override
            public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board, List<Damageable> allTargeted, List<Damageable> damageTargeted) throws ToClientException {
                NormalTurn.askAndReload(subjectPlayer, updater);
            }

            @Override
            public String getName() {
                return "Reload";
            }

            @Override
            public List<AmmoCube> getCost() {
                return new ArrayList<>();
            }
        };
    }
}