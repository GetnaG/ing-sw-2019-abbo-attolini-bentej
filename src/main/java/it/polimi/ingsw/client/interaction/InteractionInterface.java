package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.server.controller.effects.EffectInterface;

import java.util.List;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public interface InteractionInterface {

    public void notifyUpdatedState();

    public String chooseEffectSequence(String something);

    public String chooseSpawn(String something);

    public String choosePowerup(String something);

    public String chooseDestination(String something);

    public String chooseWeapon(String something);

    public String chooseWeaponToBuy(String something);

    public String chooseWeaponToDiscard(String something);

    public String chooseWeaponToReload(String something);

    public String chooseAction(String something);

    public String choosePowerupForPaying(String something);

    public String chooseUseTagBack(String something);

    public String chooseTarget(String something);

    String askName();

    //TODO: the argument will be a reference to a string
    void sendNotification(String message);

    //TODO: To replace when user interaction will be implemented
    String tempAsk(String message);
    int tempAsk(String message, String[][] options);


}
