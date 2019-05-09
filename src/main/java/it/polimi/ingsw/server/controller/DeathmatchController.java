package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;


import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class DeathmatchController implements SuspensionListener, ScoreListener {

    /**
     * Default constructor
     */
    public DeathmatchController() {
    }

    /**
     * 
     */
    private Player players;

    /**
     * 
     */
    private Player currentPlayer;

    /**
     * 
     */
    private GameBoard board;

    /**
     * 
     */
    public void start() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callFirstTurn() {
        // TODO implement here
    }

    /**
     * 
     */
    private void checkFinalFrenzy() {
        // TODO implement here
    }

    /**
     * 
     */
    private void CallNormalTurn() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callScoreKilled() {
        // TODO implement here
    }

    /**
     * 
     */
    private void handleOverAndDoubleKill() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callRespawnForKilled() {
        // TODO implement here
    }

    /**
     * 
     */
    private void callFinalFrenzyTurn() {
        // TODO implement here
    }

    /**
     * 
     */
    public void scoreBoard() {
        // TODO implement here
    }

    /**
     * @param player
     */
    public void playerSuspension(Player player) {
        // TODO implement here
    }

    /**
     * @param player
     */
    public void playerResumption(Player player) {
        // TODO implement here
    }

    /**
     * @param killed
     */
    public void addKilled(Damageable killed) {
        // TODO implement here
    }

    /**
     * 
     */
    public void scoreAllKilled() {
        // TODO implement here
    }



    /**
     * @return
     */

    public List<Damageable> getKilled() {
        // TODO implement here
        return null;
    }


    /**
     * 
     */
    public void emptyKilledList() {
        // TODO implement here
    }

    /**
     * Users that are playing this game
     */
    public void addUsers(Collection<User> users){

    }

}