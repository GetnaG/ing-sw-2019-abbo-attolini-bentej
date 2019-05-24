package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.MatchState;
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

    public int chooseEffectSequence(String something);

    public int chooseSpawn(String something);

    public int choosePowerup(String something);

    public int chooseDestination(String something);

    public int chooseWeapon(String something);

    public int chooseWeaponToBuy(String something);

    public int chooseWeaponToDiscard(String something);

    public int chooseWeaponToReload(String something);

    public int chooseAction(String something);

    public int choosePowerupForPaying(String something);

    public int chooseUseTagBack(String something);

    public int chooseTarget(String something);

    public void drawState(MatchState state);

    String askName();

    //TODO: the argument will be a reference to a string
    void sendNotification(String message);

    //TODO: To replace when user interaction will be implemented
    String tempAsk(String message);
    int tempAsk(String message, String[][] options);


}
