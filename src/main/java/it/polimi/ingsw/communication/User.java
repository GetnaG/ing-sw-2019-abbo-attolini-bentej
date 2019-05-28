package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.serverlogic.Nicknames;
import it.polimi.ingsw.server.serverlogic.ServerMain;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * It's the middle man between the server and the actual client. Takes care of checking timeouts and/or connections lost.
 *
 * @author Fahed B. Tej
 */

public class User implements ToClientInterface {

    /**
     * User's name
     */
    private String name;

    /**
     * Timer lasts 30 seconds. //FIXME Make it parametric
     */
    private Timer timer;

    /**
     * Suspension listener to update in case of connection's lost
     */
    private SuspensionListener serverSuspensionListener;

    /**
     * The underlying ToClientInterface
     */
    private ToClientInterface toClient;
    /**
     * Constructs a User
     * @param toClient            underlying connection interface
     * @param suspensionListener  suspension listener
     */
    public User(ToClientInterface toClient, SuspensionListener suspensionListener) {
        this.toClient = toClient;
        this.serverSuspensionListener = suspensionListener;
    }

    /**
     * Constructs a User
     * @param toClient            underlying connection interface
     */
    public User(ToClientInterface toClient) {
        this.toClient = toClient;
    }

    /**
     * Gets the nickname of the user
     * @return  nickname of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Makes the user choose a name
     */
    public void init(){
        name = this.chooseUserName();
        ServerMain.getServerHall().addUser(this);
    }

    public void addSuspensionListener(SuspensionListener suspensionListener){
        this.serverSuspensionListener = suspensionListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override //TODO Make all interactions with client throw a NotReachableException
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<EffectInterface> task =executorService.submit(()-> toClient.chooseEffectsSequence(options));

        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> option) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<PowerupCard> task =executorService.submit(()-> toClient.chooseSpawn(option));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<PowerupCard> task =executorService.submit(()-> toClient.choosePowerup(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }


        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Square chooseDestination(List<Square> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Square> task =executorService.submit(()-> toClient.chooseDestination(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WeaponCard> task =executorService.submit(()-> toClient.chooseWeaponCard(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WeaponCard> task =executorService.submit(()-> toClient.chooseWeaponToBuy(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WeaponCard> task =executorService.submit(()-> toClient.chooseWeaponToDiscard(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<WeaponCard> task =executorService.submit(()-> toClient.chooseWeaponToReload(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Action chooseAction(List<Action> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Action> task =executorService.submit(()-> toClient.chooseAction(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<PowerupCard> task =executorService.submit(()-> toClient.choosePowerupForPaying(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<PowerupCard> task =executorService.submit(()-> toClient.askUseTagback(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Damageable>> task =executorService.submit(()-> toClient.chooseTarget(options));
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }

        return null;
    }

    @Override
    public String chooseUserName() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> task =executorService.submit(()-> toClient.chooseUserName());
        try {
            return task.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//         throw new NotReachableException
        }
        return null;
    }

    /**
     * Closes the connection.
     */
    @Override
    public void quit() {
        try {
            toClient.quit();
        } catch (ToClientException e) {
            e.printStackTrace();
        }
    }

}
