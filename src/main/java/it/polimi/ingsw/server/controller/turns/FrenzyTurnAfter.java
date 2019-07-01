package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.controller.effects.*;
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
 * //TODO FinalFrenzySetUp or TurnSetup(TurnType)
 * @see FrenzyTurnBefore
 * @see FrenzyTurnAfter
 *
 * @author Fahed Ben Tej
 */
public class FrenzyTurnAfter implements TurnInterface {
    private Consumer<UpdateBuilder> updater;

    /**
     * Construct a FrenzyTurn
     */
    public FrenzyTurnAfter(Consumer<UpdateBuilder> updater) {
        this.updater = updater;
    }

    /**
     * A player can choose from the following actions :
     * (1) Move up to 2 squares, reload if you want, and then shoot.
     * (2) Move move up to 3 squares and grab something.
     * At the end of the turn there is no point in reloading because his match is over.
     */
    public void startTurn(Player subjectPlayer, List<Damageable> allTargets, GameBoard board) {
        List<Action> actions = new ArrayList<>();
        actions.add(getFirstFrenzyActionAfter());
        actions.add(getSecondFrenzyActionAfter());

        try {
            Action chosenAction = subjectPlayer.getToClient().chooseAction(actions);
            for (EffectInterface effect : chosenAction) {
                effect.runEffect(subjectPlayer, allTargets, board, new ArrayList<>(), new ArrayList<>());
                updater.accept(null);
            }
        } catch (ToClientException e) {
            updater.accept(null);
        }
    }

    /**
     * Gets the first possible action.
     * @return the first possible action.
     */
    private Action getFirstFrenzyActionAfter(){
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(new Move());
        //TODO Insert Reload option
        steps.add(new Shoot());

        return new Action("FirstFrenzyActionAfter", steps);
    }

    /**
     * Gets the second possible action.
     * @return the second possible action.
     */
    private Action getSecondFrenzyActionAfter(){
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Grab());

        return new Action("SecondFrenzyActionAfter", steps);
    }

}