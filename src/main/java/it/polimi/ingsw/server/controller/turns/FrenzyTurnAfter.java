package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.controller.effects.*;
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

    /**
     * The current player.
     */
    private Player currentPlayer;
    /**
     * The game board.
     */
    private GameBoard board;

    private Consumer<UpdateBuilder> updater;

    /**
     * Construct a FrenzyTurn
     */
    public FrenzyTurnAfter(Player currentPlayer, GameBoard board,
                           Consumer<UpdateBuilder> updater) {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.updater = updater;
    }

    /**
     * A player can choose from the following actions :
     * (1) Move up to 2 squares, reload if you want, and then shoot.
     * (2) Move move up to 3 squares and grab something.
     * At the end of the turn there is no point in reloading because his match is over.
     * @param currentPlayer
     * @param board GameBoard
     *
     * @return -1 if Final Frenzy is triggered, else 0. In this case, final frenzy is never triggered because it has already happened.
     */
    public int startTurn(Player currentPlayer, GameBoard board) {
        List<Action> actions = new ArrayList<>();
        Action choosenAction;

        actions.add(getFirstFrenzyActionAfter());
        actions.add(getSecondFrenzyActionAfter());

        try {
            choosenAction = currentPlayer.getToClient().chooseAction(actions);
            choosenAction.runEffect(currentPlayer, null, board, new ArrayList<>(), new ArrayList<>());
        } catch (ToClientException e) {
            //TODO Handle if the user is disconnected
        }

        updater.accept(new UpdateBuilder());//TODO: put here things that could change
        return 0;
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