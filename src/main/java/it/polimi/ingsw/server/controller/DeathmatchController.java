package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.server.controller.turns.*;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.ConfigurationHelper;
import it.polimi.ingsw.server.model.board.Configurations;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.KillshotTrack;
import it.polimi.ingsw.server.model.player.NormalPlayerBoard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Controls the flow of the Game.
 * <p>
 * The Game is seen as a sequence of turns. The order of the turns follows the order of connection.
 *
 * @author Fahed B. Tej
 */
public class DeathmatchController implements SuspensionListener, ScoreListener {
    private static final String PLAYER_RESOURCE_PREFIX = "figureRes";
    /**
     * Players in the Game
     */
    private List<Player> players;
    /**
     * Game Board used in the game
     */
    private GameBoard board;
    /**
     * Suspended Players
     */
    private List<Player> suspendedPlayers;
    /**
     * Damageable objects killed during turn.
     */
    private List<Damageable> killedInTurn;

    /**
     * Constructs a DeathmatchController with the given users
     *
     * @param users users in the game. The order of the turns is based on the given list.
     */
    public DeathmatchController(List<? extends User> users, int skullsLeft, Configurations configuration) {
        players = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);
            players.add(new Player(currentUser.getName(),
                    i == 0, PLAYER_RESOURCE_PREFIX + i, currentUser,
                    new NormalPlayerBoard(), this));
        }
        board = new GameBoard(new KillshotTrack(skullsLeft),
                ConfigurationHelper.boardCreator(configuration));
        // boards
        suspendedPlayers = new ArrayList<>();
        killedInTurn = new ArrayList<>();
    }

    /**
     * Starts the game. The game is divided in the following four phases:
     * (1) Initial Turn
     * (2) NormalTurns and RespawnTurns
     * (3) FinalFrenzy
     * (4) Scoring
     */
    public void start() {
        board.replaceAll();
        Player currentPlayer = players.get(0);

        // First turn
        //FIXME: set default location to suspended
        Iterator<Player> iterator = new PlayerIterator(players.get(0), false);
        while (iterator.hasNext()) {
            currentPlayer = iterator.next();
            new FirstTurn().startTurn(currentPlayer, board);
            normalTurn(currentPlayer);
        }

        // A series of normal turns interrupted by Final Frenzy
        iterator = new PlayerIterator(players.get(0), true);
        while (!board.checkFinalFrenzy() && iterator.hasNext()) {
            currentPlayer = iterator.next();
            normalTurn(currentPlayer);
        }

        //TODO setup finalfrenzy
        int whoTriggered = players.indexOf(currentPlayer);
        currentPlayer = iterator.next();

        // Final frenzy
        iterator = new PlayerIterator(currentPlayer, false);
        while (iterator.hasNext()) {
            currentPlayer = iterator.next();
            if (players.indexOf(currentPlayer) > whoTriggered) {
                // For those who come before the first player
                new FrenzyTurnBefore(currentPlayer, board).startTurn(currentPlayer, board);
            } else {
                // for those who come after the first player
                new FrenzyTurnAfter(currentPlayer, board).startTurn(currentPlayer, board);
            }
        }

        finalScore();
    }

    private void finalScore() {
        players.forEach(this::addKilled);
        scoreAllKilled();
        scoreBoard();
    }

    public void notifyAllPlayers(Notification.NotificationType notificationType) {
        for (Player p : players) {
            if (!suspendedPlayers.contains(p)) {
                try {
                    p.getToClient().sendNotification(notificationType);
                } catch (ToClientException ignored) {
                    /*Ignored because there is nothing to recover from*/
                }
            }
        }
    }

    /**
     * Deals with the normal turn of the {@code currentPlayer}.
     */
    private void normalTurn(Player currentPlayer) {
        new NormalTurn(currentPlayer, board).startTurn(currentPlayer, board);
        scoreAllKilled();
        board.replaceAll();
        respawnDeadPlayers();
    }


    /**
     * Respawns all dead players. Each player gets to play a Respawn Turn.
     */
    private void respawnDeadPlayers() {//TODO
        List<Player> toBeRespawned = players.stream().filter(p -> p.getPlayerBoard().isDead()).collect(Collectors.toList());
        for (Player p : toBeRespawned) {
            new RespawnTurn().startTurn(p, board);
        }
    }

    /**
     * Changes the given player status to suspended.
     *
     * @param player player to be suspended
     */
    @Override
    public void playerSuspension(String player) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.add(p);
                break;
            }
        if (players.size() - suspendedPlayers.size() < 3) {
            notifyAllPlayers(Notification.NotificationType.GAME_OVER);
            finalScore();
        }
        throw new IllegalArgumentException("Player does not exist: " + player);
    }

    /**
     * Changes the given player to resumed.
     *
     * @param player player resumed
     */
    @Override
    public void playerResumption(String player) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.remove(p);
                return;
            }
    }

    @Override
    public boolean playerUpdate(String player, ToClientInterface newConnection) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.remove(p);
                p.setToClient(newConnection);
                return true;
            }
        return false;
    }


    /**
     * This calls {@code scoreBoard} on the {@linkplain GameBoard}. It is used
     * at the end of the game.
     */
    @Override
    public void scoreBoard() {
        board.scoreBoard();
    }

    /**
     * Adds a {@linkplain Damageable} object to the objects that will be
     * scored.
     *
     * @param killed the {@code Damageable} to be scored
     * @throws NullPointerException if {@code killed} is null
     */
    @Override
    public void addKilled(Damageable killed) {
        if (!killedInTurn.contains(killed))
            killedInTurn.add(killed);
    }

    /**
     * Scores all the {@linkplain Damageable} objects added. This method must
     * also take care of adding the <i>kill shot</i> and
     * <i>overkill</i> tokens (if applicable) to the {@code GameBoard}.
     * This affects all the objects added since the last call to {@code
     * emptyKilledList}.
     */
    @Override
    public void scoreAllKilled() {
        killedInTurn.forEach(Damageable::scoreAndResetDamage);
    }


    /**
     * Returns a {@linkplain List} of the objects that will be scored. These are
     * the {@linkplain Damageable} added since the last call to {@code
     * emptyKilledList}.
     *
     * @return a list of the objects that will be scored
     */
    @Override
    public List<Damageable> getKilled() {
        return new ArrayList<>(killedInTurn);
    }


    /**
     * Clears the list of objects on which scoring will be performed. Invoking
     * {@code scoreAllKilled} will effect only the objects added after the last
     * call to this method. A call to {@code scoreAllKilled} immediately after
     * this method will produce no effect.
     */
    @Override
    public void emptyKilledList() {
        killedInTurn.clear();
    }

    private class PlayerIterator implements Iterator<Player> {
        private boolean isCircular;
        private int start;
        private int index;
        private Player next;

        PlayerIterator(Player from, boolean isCircular) {
            this.isCircular = isCircular;
            start = players.indexOf(from);
            index = start;
            next = from;
            while (next != null && suspendedPlayers.contains(next)) {
                next = circularNext();
                notifyAllPlayers(Notification.NotificationType.USER_WILL_SKIP);
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Player next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Player toReturn = next;
            next = circularNext();
            while (next != null && suspendedPlayers.contains(next)) {
                next = circularNext();
                notifyAllPlayers(Notification.NotificationType.USER_WILL_SKIP);
            }
            return toReturn;
        }

        private Player circularNext() {
            index = ((index + 1) < players.size()) ? (index + 1) : 0;
            return (isCircular || index != start) ? players.get(index) : null;
        }
    }
}