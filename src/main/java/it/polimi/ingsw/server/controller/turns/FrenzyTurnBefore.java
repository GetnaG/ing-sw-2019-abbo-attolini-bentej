package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.controller.effects.*;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

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
public class FrenzyTurnBefore implements TurnInterface {
    /**
     * The current player.
     */
    private Player currentPlayer;
    /**
     * The game board.
     */
    private GameBoard board;

    /**
     * Construct a FrenzyTurn
     */
    public FrenzyTurnBefore(Player currentPlayer, GameBoard board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
    }

    /**
     * A player can choose from the following actions :
     * (1) Move up to 1 square, reload if you want, then shoot.
     * (2) Move up to 4 squares.
     * (3) Move up to 2 squares and grab something there.
     * At the end of the turn there is no point in reloading because his match is over.
     * @param currentPlayer 
     * @param board GameBoard
     */
    public void startTurn(Player currentPlayer, GameBoard board) {

        List<Action> actions = new ArrayList<>();
        Action choosenAction;

        actions.add(getFirstFrenzyActionBefore());
        actions.add(getSecondFrenzyActionBefore());
        actions.add(getThirdFrenzyActionBefore());

        try {
            choosenAction = currentPlayer.getToClient().chooseAction(actions);
            choosenAction.runEffect(currentPlayer, null, board, new ArrayList<>(), new ArrayList<>());
        } catch (ToClientException e) {
            //TODO Handle if the user is disconnected
        }
    }

    /**
     * Gets the first possible action.
     * @return the first possible action.
     */
    private Action getFirstFrenzyActionBefore(){
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        //TODO Insert Reload option
        steps.add(new Shoot());

        return new Action("FirstFrenzyActionBefore", steps);
    }

    /**
     * Gets the second possible action.
     * @return the second possible action.
     */
    private Action getSecondFrenzyActionBefore(){
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Move());

        return new Action("SecondFrenzyActionBefore", steps);
    }
    /**
     * Gets the third possible action.
     * @return the third possible action.
     */
    private Action getThirdFrenzyActionBefore(){
        List<EffectInterface> steps = new ArrayList<>();
        steps.add(new Move());
        steps.add(new Move());
        steps.add(new Grab());

        return new Action("ThirdFrenzyActionBefore", steps);
    }


}