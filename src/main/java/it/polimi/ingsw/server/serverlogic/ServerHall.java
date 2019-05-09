package it.polimi.ingsw.server.serverlogic;

import it.polimi.ingsw.communication.User;
import it.polimi.ingsw.server.controller.DeathmatchController;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents the place where Users gather to create a Game.
 * <p>
 * A Game is created when at least 3 users are connected and the time is elapsed.
 *
 * @author Fahed B. Tej
 */
public class ServerHall implements Runnable {

    /**
     * Timer used to build a Game
     */
    private long startTimer;

    /**
     * Seconds before starting a game >= 3 player
     */
    private int seconds;
    /**
     * Connected Users
     */
    private Collection<User> connectedUsers;

    /**
     * Status of the nextGame;
     */
    private GameStatus statusNextGame;

    /**
     * Match controller
     */
    private DeathmatchController nextGame;

    public ServerHall(int secondsWaitingRoom){
        this.seconds = secondsWaitingRoom;
        this.connectedUsers = new HashSet<>();
        statusNextGame = GameStatus.NOTSTARTED;
    }


    /**
     * This method represent the ServerHall Thread. It takes care of collecting the players together and creates a Game.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        //noinspection InfiniteLoopStatement
        do {

            if (statusNextGame == GameStatus.NOTSTARTED && !connectedUsers.isEmpty()) {
                nextGame = new DeathmatchController();
                statusNextGame = GameStatus.STARTING;
                startTimer = System.currentTimeMillis();
            }

            if (statusNextGame == GameStatus.STARTING && connectedUsers.size() >= 3) {
                statusNextGame = GameStatus.STARTINGTIMER;
                startTimer = System.currentTimeMillis();
            }

            if (statusNextGame == GameStatus.STARTINGTIMER && connectedUsers.size() >= 3 && System.currentTimeMillis() - startTimer >= seconds * 1000) {
                nextGame.addUsers(connectedUsers);
                nextGame.start();
                statusNextGame = GameStatus.NOTSTARTED;
            }

            // se esiste un nextGame && i giocatori sono meno di 3 allora ferma l'eventuale timer e cambia stato gioco
            if (statusNextGame == GameStatus.STARTINGTIMER && connectedUsers.size() < 3) {
                statusNextGame = GameStatus.STARTING;
            }


        } while (true);

    }

    /**
     * Adds the connected user to the waiting room
     * @param user  connected user
     */
    public void addUser(User user){
        connectedUsers.add(user);
    }

    /**
     * Removes the disconnected user from the waiting room
     * @param user  disconnected user
     */
    public void removeUser(User user){
        connectedUsers.remove(user);

    }



}
