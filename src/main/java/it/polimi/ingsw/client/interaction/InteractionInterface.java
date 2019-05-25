package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.MatchState;

import java.util.List;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public interface InteractionInterface {

    void notifyUpdatedState();

    int chooseEffectSequence(List<List<String>> optionKeys);

    int chooseSpawn(List<List<String>> optionKeys);

    int choosePowerup(List<List<String>> optionKeys);

    int chooseDestination(List<List<String>> optionKeys);

    int chooseWeapon(List<List<String>> optionKeys);

    int chooseWeaponToBuy(List<List<String>> optionKeys);

    int chooseWeaponToDiscard(List<List<String>> optionKeys);

    int chooseWeaponToReload(List<List<String>> optionKeys);

    int chooseAction(List<List<String>> optionKeys);

    int choosePowerupForPaying(List<List<String>> optionKeys);

    int chooseUseTagBack(List<List<String>> optionKeys);

    int chooseTarget(List<List<String>> optionKeys);

    void drawState(MatchState state);

    String askName();

    void sendNotification(String notificationKey);
}
