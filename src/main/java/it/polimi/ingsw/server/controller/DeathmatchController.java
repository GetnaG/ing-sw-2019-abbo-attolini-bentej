package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.server.controller.turns.*;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.ConfigurationHelper;
import it.polimi.ingsw.server.model.board.Configurations;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.KillshotTrack;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.serverlogic.ServerMain;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controls the flow of the game.
 * The game is seen as a sequence of turns. The order of the turns follows
 * the order of the {@link User}s list provided with the constructor.
 * <p>
 * At first each player will take a <i>first turn</i>, he is prompted to
 * spawn and to run a <i>normal turn</i>; then follow <i>normal turns</i>
 * until the final frenzy triggers; everyone gets one more <i>final frenzy
 * turn</i> and finally everything is scored.
 * The game will also stop when there are less than <b>three</b> players
 * connected.
 * <p>
 * This will also act as a {@link ScoreListener}, it is notified when a
 * player is killed and needs to be scored, which happens at the end of the
 * turn for {@link Damageable}s and at the end of the game for the
 * {@link GameBoard}.
 * <p>
 * As a {@link SuspensionListener}, this will be notified when a player is
 * suspended, and will ensure that will skip his turns.
 *
 * @author Fahed B. Tej
 * @author Abbo Giulio A.
 * @see Player
 * @see GameBoard
 * @see TurnInterface
 */
public class DeathmatchController implements SuspensionListener, ScoreListener {
    /**
     * The prefix used for retrieving the player's resource.
     * To this is appended a number from 0 to the max number of players.
     */
    private static final String PLAYER_RESOURCE_PREFIX = "figureRes";
    /**
     * The players in the Game.
     */
    private List<Player> players;
    /**
     * The board used in the game.
     */
    private GameBoard board;
    /**
     * The suspended Players.
     */
    private List<Player> suspendedPlayers;
    /**
     * Damageable objects killed in the turn, they will eventually be scored.
     */
    private List<Damageable> killedInTurn;
    private boolean gameOver;
    private Configurations configuration;
    private boolean frenzy;

    /**
     * Constructs a controller with the provided parameters.
     * The players are created from the given users and their order will
     * determine the order of the turns.
     *
     * @param users         the users that will be the players of the game
     * @param skullsLeft    the number of initial skulls on the board
     * @param configuration the configuration of the squares on the board
     */
    public DeathmatchController(List<? extends User> users, int skullsLeft, Configurations configuration) {

        /*Constructing the players from the users*/
        players = new ArrayList<>();
        users.forEach(u -> players.add(new Player(u.getName(), PLAYER_RESOURCE_PREFIX + users.indexOf(u), u, this)));

        //TODO: can I use directly the loader?
        board = new GameBoard(new KillshotTrack(skullsLeft), ConfigurationHelper.boardCreator(configuration));

        suspendedPlayers = new ArrayList<>();
        killedInTurn = new ArrayList<>();
        gameOver = false;
        this.configuration = configuration;
        frenzy = false;
    }

    /**
     * Starts the game and handles the turns.
     * The game is divided in the following four phases:<ul>
     * <li>First turn</li>
     * <li>NormalTurns and spawn</li>
     * <li>Final frenzy</li>
     * <li>Final scoring</li></ul>
     */
    public void start() {
        updateAllPlayers(fullUpdate());
        Player currentPlayer = players.get(0);
        updateAllPlayers(new UpdateBuilder().setCurrent(currentPlayer));

        /*First turn for the players not suspended*/
        Iterator<Player> iterator = new PlayerIterator(players.get(0), false);
        while (iterator.hasNext()) {
            currentPlayer = iterator.next();
            updateAllPlayers(new UpdateBuilder().setCurrent(currentPlayer));
            new FirstTurn(this::updateAllPlayers).startTurn(currentPlayer, board);
            turn(new NormalTurn(currentPlayer, new ArrayList<>(players), board,
                    this::updateAllPlayers), currentPlayer);
        }

        /*Ensuring that all the players are on the board, the default is the blue spawn*/
        for (Player p : players)
            if (p.getPosition() == null)
                p.setPosition(board.findSpawn(AmmoCube.BLUE));

        /*A series of normal turns interrupted by Final Frenzy*/
        iterator = new PlayerIterator(players.get(0), true);
        while (!board.checkFinalFrenzy() && iterator.hasNext()) {
            currentPlayer = iterator.next();
            updateAllPlayers(new UpdateBuilder().setCurrent(currentPlayer));
            turn(new NormalTurn(currentPlayer, new ArrayList<>(players), board,
                    this::updateAllPlayers), currentPlayer);
        }

        /*Setting up final frenzy*/
        int whoTriggered = players.indexOf(currentPlayer);
        currentPlayer = (gameOver) ? currentPlayer : iterator.next();
        frenzy = true;
        players.forEach(Player::setupFinalFrenzy);

        /*Final frenzy from the player after who started it*/
        iterator = new PlayerIterator(currentPlayer, false);
        while (iterator.hasNext()) {
            currentPlayer = iterator.next();
            updateAllPlayers(new UpdateBuilder().setCurrent(currentPlayer));
            if (players.indexOf(currentPlayer) > whoTriggered) {

                /*For those who are before the first player*/
                turn(new FrenzyTurnBefore(currentPlayer, board, this::updateAllPlayers),
                        currentPlayer);
            } else {

                /*For those who are after the first player and him*/
                turn(new FrenzyTurnAfter(currentPlayer, board, this::updateAllPlayers),
                        currentPlayer);
            }
        }

        /*Scoring all the players and the board*/
        notifyAllPlayers(Notification.NotificationType.GAME_OVER);
        players.forEach(this::addKilled);
        scoreAllKilled();
        emptyKilledList();
        scoreBoard();

        /*Notifying who won and ending the match*/
        updateAllPlayers(new UpdateBuilder().setWinners(players.stream()
                .sorted(Comparator.comparingInt(Player::getScore)).collect(Collectors.toList())));
        ServerMain.getDeathMatchHall().removeMatch(this);
    }

    /**
     * Notifies all not suspended players.
     *
     * @param notificationType the notification to send
     */
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
     * Updates all not suspended players.
     *
     * @param update the update to send
     */
    public void updateAllPlayers(UpdateBuilder update) {
        for (Player p : players) {
            if (!suspendedPlayers.contains(p)) {
                try {
                    p.getToClient().sendUpdate(update);
                } catch (ToClientException ignored) {
                    /*Ignored because there is nothing to recover from*/
                }
            }
        }
    }

    private UpdateBuilder fullUpdate() {
        UpdateBuilder updateBuilder = new UpdateBuilder()
                .setConfigurationId(configuration.getId())
                .setAmmoCards(board)
                .setWeaponsOnBoard(board)
                .setWeaponDrawable(!board.isWeaponDeckEmpty())
                .setKillshotTrack(board.getKillshotTrack())
                .setMatchFrenzy(frenzy)
                .setPlayers(players);
        for (Player p : players) {
            updateBuilder.setPlayerPosition(p, p.getPosition())
                    .setActiveCubes(p, p.getAmmoCubes())
                    .setIsPlayerFrenzy(p, p.isFrenzy())
                    .setSkullsOnBoard(p, p.getSkulls())
                    .setPlayerDamage(p, p.getPlayerBoard().getDamage())
                    .setPlayerMarks(p, p.getPlayerBoard().getMarks())
                    .setIsConnected(p, !suspendedPlayers.contains(p))
                    .setLoadedWeapons(p, p.getLoadedWeapons())
                    .setUnloadedWeapon(p, p.getReloadableWeapons())
                    .setPowerupsInHand(p, p.getAllPowerup());
        }
        return updateBuilder;
    }

    /**
     * Deals with the provided turn of the current player.
     * This also scores all the killed players and respawns them.
     * After this the board will be ready for the next turn.
     *
     * @param turnInterface the turn that will be run
     * @param currentPlayer the player that will run this turn
     */
    private void turn(TurnInterface turnInterface, Player currentPlayer) {
        turnInterface.startTurn(currentPlayer, board);
        scoreAllKilled();
        board.replaceAll();

        /*Respawning*/
        for (Player p : players) {
            if (killedInTurn.contains(p))
                new RespawnTurn(this::updateAllPlayers).startTurn(p, board);
        }
        emptyKilledList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerSuspension(String player) {
        for (Player p : players)
            if (p.getName().equals(player) && !suspendedPlayers.contains(p)) {
                suspendedPlayers.add(p);
                break;
            }
        if (players.size() - suspendedPlayers.size() < 3) {
            gameOver = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerResumption(String player) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.remove(p);
                updateAllPlayers(fullUpdate());
                return;
            }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerUpdate(String player, ToClientInterface newConnection) {
        for (Player p : players)
            if (p.getName().equals(player)) {
                suspendedPlayers.remove(p);
                p.setToClient(newConnection);
                updateAllPlayers(fullUpdate());
                return true;
            }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scoreBoard() {
        board.scoreBoard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addKilled(Damageable killed) {
        if (!killedInTurn.contains(killed))
            killedInTurn.add(killed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scoreAllKilled() {
        killedInTurn.forEach(Damageable::scoreAndResetDamage);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Changes to the returned list will not be reflected on the original.
     */
    @Override
    public List<Damageable> getKilled() {
        return new ArrayList<>(killedInTurn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emptyKilledList() {
        killedInTurn.clear();
    }

    /**
     * This iterates over the player in the match that are not suspended.
     */
    private class PlayerIterator implements Iterator<Player> {
        /**
         * Whether the iteration is a loop.
         */
        private boolean isCircular;
        /**
         * The index of the first element.
         */
        private int start;
        /**
         * The index of the {@linkplain #next} element.
         */
        private int index;
        /**
         * The element that will be returned next.
         */
        private Player next;

        /**
         * An iterator over the not suspended players.
         * If {@code isCircular} is true, then the iterator will never stop;
         * otherwise this will stop when the {@code from} element is reached
         * again.
         *
         * @param from       the first element that will be returned
         * @param isCircular whether this will be a loop
         */
        PlayerIterator(Player from, boolean isCircular) {
            this.isCircular = isCircular;
            start = players.indexOf(from);
            index = start;

            /*Setting the first valid next element*/
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

            /*Setting the first valid next element*/
            next = circularNext();
            while (next != null && suspendedPlayers.contains(next)) {
                next = circularNext();
                notifyAllPlayers(Notification.NotificationType.USER_WILL_SKIP);
            }

            /*Returning the memorized element*/
            return toReturn;
        }

        /**
         * Returns the next player in the list as if it was circular.
         *
         * @return the next player in the list as if it was circular or null
         * if the iteration should stop
         */
        private Player circularNext() {
            index = ((index + 1) < players.size()) ? (index + 1) : 0;
            return (!gameOver && (isCircular || index != start)) ?
                    players.get(index) : null;
        }
    }
}