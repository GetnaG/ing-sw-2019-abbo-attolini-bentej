package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.Update;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.serverlogic.Nicknames;
import it.polimi.ingsw.server.serverlogic.ServerMain;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;

import java.util.List;
import java.util.concurrent.*;

/**
 * This is the middle man between the server and the actual client.
 * This will takes care of checking timeouts and connections lost.
 * This also asks to choose the username.
 *
 * @author Fahed B. Tej
 * @author Abbo Giulio A.
 */
public class User implements ToClientInterface {
    /**
     * How much time the user has for making a choice.
     */
    private static int waitingTime;
    /**
     * User's name.
     */
    private String name;
    /**
     * Server suspension listener to update in case of connection lost.
     * This should keep track of all the players on the server, for future
     * reconnection.
     */
    private SuspensionListener serverSuspensionListener;
    /**
     * Match suspension listener to update in case of connection lost.
     * This should make suspended users skip their turn.
     * Null if the match has not started yet.
     */
    private SuspensionListener matchSuspensionListener;
    /**
     * The underlying ToClientInterface
     */
    private ToClientInterface toClient;

    /**
     * Constructs a User with the provided interface and the server
     * suspension listener.
     *
     * @param toClient underlying connection interface
     */
    public User(ToClientInterface toClient) {
        this.toClient = toClient;
        serverSuspensionListener = Nicknames.getInstance();
        matchSuspensionListener = null;
        name = null;
    }

    /**
     * Sets the amount of time the users have to make a choice.
     *
     * @param waitingTime the time in seconds
     */
    public static void setWaitingTime(int waitingTime) {
        User.waitingTime = waitingTime;
    }

    /**
     * Makes the user choose a name and adds it to the hall.
     */
    public void init() {
        try {
            name = chooseUserName();
        } catch (ToClientException e) {

            /* The client was not able to choose: no action is necessary*/
            ServerMain.getLog().info("User disconnected before setting name");
            return;
        }

        /*Checking if a nickname is already taken*/
        int result = Nicknames.getInstance().addNickname(name);
        switch (result) {

            /*Taken and the player is offline: swapping with the old one*/
            case 0:
                try {
                    toClient.sendNotification(Notification.NotificationType.USERNAME_TAKEN_AND_OFFLINE);
                } catch (ToClientException e) {

                    /*User offline again: will not swap with the old*/
                    serverSuspensionListener.playerSuspension(name, matchSuspensionListener);
                    return;
                }

                /*Notifying match listener only if the match was started*/
                if (matchSuspensionListener != null)
                    matchSuspensionListener.playerUpdate(name, this);
                break;

            /*Success: adding to the hall*/
            case 1:
                try {
                    toClient.sendNotification(Notification.NotificationType.USERNAME_AVAILABLE);
                } catch (ToClientException e) {

                    /*Freeing the nickname*/
                    serverSuspensionListener.playerSuspension(name, matchSuspensionListener);
                    return;
                }
                ServerMain.getLog().info(() -> "Connected: " + name);
                ServerMain.getDeathMatchHall().addUser(this);
                break;

            /*Taken and the player is online: asking again*/
            case -1:
                try {
                    toClient.sendNotification(Notification.NotificationType.USERNAME_TAKEN_AND_ONLINE);
                } catch (ToClientException e) {

                    /*The old user is online: no action taken*/
                    return;
                }
                init();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    /**
     * Gets the nickname of the user
     *
     * @return nickname of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the match SuspensionListener of this user.
     * This is useful when a user is suspended and a new one has to take his
     * place.
     *
     * @return the match SuspensionListener of this user
     */
    public SuspensionListener getMatchSuspensionListener() {
        return matchSuspensionListener;
    }

    /**
     * Sets the match SuspensionListener for this user.
     *
     * @param matchSuspensionListener the match SuspensionListener for this user
     */
    public void setMatchSuspensionListener(SuspensionListener matchSuspensionListener) {
        this.matchSuspensionListener = matchSuspensionListener;
    }

    /**
     * This is done in order to handle the timing of the interactions in a
     * single place.
     *
     * @param callable the function that will interact with the user, should
     *                 throw {@linkplain ToClientException} in case of problems
     * @param <T>      the type returned by the {@code callable} parameter
     * @return the user's choice
     * @throws ToClientException if there are problems with the communication
     *                           or the client did not answer in time (in either
     *                           cases it is suspended, this is thrown to
     *                           allow calling methods to handle the
     *                           disconnection)
     */
    private <T> T genericInteraction(Callable<T> callable) throws ToClientException {
        Future<T> task = Executors.newSingleThreadExecutor().submit(callable);
        try {
            return task.get(waitingTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

            /*Computation canceled: interrupt*/
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted", e);
        } catch (ExecutionException e) {

            /*Over with exception: suspending the player*/
            serverSuspensionListener.playerSuspension(name, matchSuspensionListener);
            if (matchSuspensionListener != null)
                matchSuspensionListener.playerSuspension(name, matchSuspensionListener);
            throw new ToClientException("Exception while interacting", e);
        } catch (TimeoutException e) {

            /*Over because time out: closing connection and suspending*/
            toClient.quit();
            serverSuspensionListener.playerSuspension(name, matchSuspensionListener);
            if (matchSuspensionListener != null)
                matchSuspensionListener.playerSuspension(name, matchSuspensionListener);
            throw new ToClientException("Time over while interacting", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseEffectsSequence(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseSpawn(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.choosePowerup(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public Square chooseDestination(List<Square> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseDestination(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseWeaponCard(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseWeaponToBuy(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseWeaponToDiscard(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseWeaponToReload(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public Action chooseAction(List<Action> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseAction(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.choosePowerupForPaying(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options) throws ToClientException {
        return genericInteraction(() -> toClient.askUseTagback(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options) throws ToClientException {
        return genericInteraction(() -> toClient.chooseTarget(options));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to answer.
     */
    @Override
    public String chooseUserName() throws ToClientException {
        return genericInteraction(() -> toClient.chooseUserName());
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to send an ack.
     */
    @Override
    public void quit() throws ToClientException {
        genericInteraction(() -> {
            toClient.quit();
            return null;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to send an ack.
     */
    @Override
    public void sendNotification(Notification.NotificationType type) throws ToClientException {
        genericInteraction(() -> {
            toClient.sendNotification(type);
            return null;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * The client will have {@linkplain #waitingTime} seconds to send an ack.
     */
    @Override
    public void sendUpdate(Update update) throws ToClientException {
        genericInteraction(() -> {
            toClient.sendUpdate(update);
            return null;
        });
    }
}
