package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.server.controller.DeathmatchController;
import it.polimi.ingsw.server.model.board.Configurations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Represents the place where Users gather to create a Game.
 * <p>
 * A Game is created when at least {@linkplain #MINIMUM_PLAYERS} users
 * are connected and {@linkplain #secondsWaitingRoom} are elapsed, or when
 * {@linkplain #MAXIMUM_PLAYERS} are connected.
 *
 * @author Fahed B. Tej
 * @author Abbo Giulio A.
 */
public class ServerHall implements Runnable {
    /**
     * The minimum number of player for a match to start.
     */
    private static final int MINIMUM_PLAYERS = 3;
    /**
     * The maximum number of player for a match to start.
     */
    private static final int MAXIMUM_PLAYERS = 5;
    /**
     * The seconds before starting a game having enough players.
     */
    private int secondsWaitingRoom;
    /**
     * Connected Users waiting for a match to start.
     */
    private List<User> connectedUsers;
    /**
     * All the matches running.
     */
    private List<DeathmatchController> startedGames;
    /**
     * Status of the hall.
     */
    private GameStatus statusNextGame;
    /**
     * Whether the hall is still running.
     */
    private boolean running;
    /**
     * The task that will fire {@linkplain #timeOut()} after a delay.
     */
    private Future timer;

    /**
     * Constructs a server hall for a death match with the provided waiting time.
     *
     * @param secondsWaitingRoom the amount of seconds to wait for a match
     *                           with the minimum number of players to start
     */
    ServerHall(int secondsWaitingRoom) {
        this.secondsWaitingRoom = secondsWaitingRoom;
        connectedUsers = new ArrayList<>();
        startedGames = new ArrayList<>();
        statusNextGame = GameStatus.NOT_STARTED;
        timer = null;
    }

    /**
     * This method can be run in another thread, handles the state of the hall.
     */
    @Override
    public void run() {
        running = true;
        synchronized (this) {
            while (running) {
                statusNextGame = statusNextGame.nextState(connectedUsers);
                statusNextGame.act(this);
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Adds the connected user to the waiting room.
     * If the maximum number of user is reached the match starts.
     *
     * @param user the connected user
     */
    public synchronized void addUser(User user) {
        connectedUsers.add(user);
        updateAll();
        if (connectedUsers.size() >= MAXIMUM_PLAYERS)
            startMatch();
        notifyAll();
    }

    /**
     * Notifies that the specified match controller is no longer active.
     *
     * @param controller the controller of the match that is over
     */
    public synchronized void removeMatch(DeathmatchController controller) {
        startedGames.remove(controller);
    }

    /**
     * Notifies all running match controllers that a player is back online.
     * If the player is in one of the active matches, his match suspension
     * listener is updated.
     * If it is in none of the active matches it is added to the waiting room.
     *
     * @param name    the name of the player that is back online
     * @param newUser the new user that takes the place of the one offline
     */
    public synchronized void notifyMatchesPlayerResumption(String name, User newUser) {
        for (DeathmatchController controller : startedGames) {
            if (controller.playerUpdate(name, newUser)) {
                newUser.setMatchSuspensionListener(controller);
                return;
            }
        }
        addUser(newUser);
    }

    /**
     * Sends am update to all the users that are waiting.
     * The update contains a list of all the users waiting.
     */
    private void updateAll() {
        UpdateBuilder builder = new UpdateBuilder().setPlayersNames(
                connectedUsers.stream().map(User::getName).collect(Collectors.toList()));
        for (User u : connectedUsers) {
            try {
                u.sendUpdate(builder);
            } catch (ToClientException e) {
                connectedUsers.remove(u);
                updateAll();
                break;
            }
        }
    }

    /**
     * Sends am update to all the users that are waiting.
     */
    private void updateAll(UpdateBuilder builder) {
        for (User u : connectedUsers) {
            try {
                u.sendUpdate(builder);
            } catch (ToClientException e) {
                connectedUsers.remove(u);
                updateAll();
            }
        }
    }

    private void notifyAll(Notification.NotificationType type) {
        for (User u : connectedUsers) {
            try {
                u.sendNotification(type);
            } catch (ToClientException e) {
                connectedUsers.remove(u);
                updateAll();
            }
        }
    }


    /**
     * Starts a death match.
     */
    private synchronized void startMatch() {
        stopTimer();

        DeathmatchController controller =
                new DeathmatchController(connectedUsers, 8, Configurations.STANDARD1);//TODO chose configuration
        startedGames.add(controller);
        for (User u : connectedUsers)
            u.setMatchSuspensionListener(controller);
        notifyAll(Notification.NotificationType.GAME_STARTING);
        connectedUsers.clear();
        statusNextGame = GameStatus.NOT_STARTED;
        new Thread(controller::start).start();
        //TODO notify MatchState
        ServerMain.getLog().info("Match starting");
    }

    /**
     * Ensures that the timer after which the match will start is running.
     */
    private void startTimer() {
        if (timer == null || timer.isDone()) {
            timer = Executors.newSingleThreadScheduledExecutor().schedule(
                    this::timeOut, secondsWaitingRoom, TimeUnit.SECONDS);
            ServerMain.getLog().info("Timer starting");
            notifyAll(Notification.NotificationType.TIMER_STARTING);
            updateAll(new UpdateBuilder().setTimer(secondsWaitingRoom));
        }
    }

    /**
     * Ensure that the timer after which the match will start is not running.
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel(false);
            timer = null;
            ServerMain.getLog().info("Timer stopped");
            notifyAll(Notification.NotificationType.TIMER_STOPPED);
        }
    }

    /**
     * This method is called when the time is over: if there are still enough
     * players the match is started.
     */
    private synchronized void timeOut() {
        updateAll();
        if (connectedUsers.size() >= MINIMUM_PLAYERS)
            startMatch();
        notifyAll();
    }

    /**
     * Represents the status of the {@linkplain ServerHall}.
     */
    private enum GameStatus {
        /**
         * Game is not started.
         */
        NOT_STARTED {
            @Override
            void act(ServerHall hall) {
                hall.stopTimer();
            }
        },
        /**
         * Game has started and it's waiting for the minimum number of players.
         */
        WAITING {
            @Override
            void act(ServerHall hall) {
                hall.stopTimer();
            }
        },
        /**
         * Game is waiting for the timer to expire so it can start the game.
         */
        TIMER_STARTED {
            @Override
            void act(ServerHall hall) {
                hall.startTimer();
            }
        };

        /**
         * Runs the right commands based on the state.
         *
         * @param hall the server hall that is calling this
         */
        abstract void act(ServerHall hall);

        /**
         * Returns the next state of the hall.
         * The state returned is based on the number of connected users and
         * the current state.
         *
         * @param connected the list of connected users
         * @return the next state
         */
        private GameStatus nextState(List connected) {
            if (connected.isEmpty()) {
                return NOT_STARTED;
            }
            if (connected.size() < MINIMUM_PLAYERS) {
                return WAITING;
            }
            return TIMER_STARTED;
        }
    }
}
