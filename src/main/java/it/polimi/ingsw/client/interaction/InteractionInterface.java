package it.polimi.ingsw.client.interaction;

import it.polimi.ingsw.client.clientlogic.ClientController;
import it.polimi.ingsw.client.clientlogic.MatchState;
import it.polimi.ingsw.client.interaction.GUI.GUI;

import java.util.List;

/**
 * Classes implementing this will handle the communication with the user.
 * Methods must block the execution until the return value - the user's
 * choice - becomes available.
 *
 * @author giubots
 * @author Fahed B. Tej
 * @see GUI
 * @see CLI
 */
public interface InteractionInterface {

    /**
     * This method is called by the model when it is updated.
     */
    void notifyUpdatedState();

    /**
     * Chooses an Effect from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseEffectSequence(List<List<String>> optionKeys);

    /**
     * Chooses a spawn from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseSpawn(List<List<String>> optionKeys);

    /**
     * Chooses a powerup from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int choosePowerup(List<List<String>> optionKeys);

    /**
     * Chooses a destination from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseDestination(List<List<String>> optionKeys);

    /**
     * Chooses a weapon from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseWeapon(List<List<String>> optionKeys);

    /**
     * Chooses a weapon to buy from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseWeaponToBuy(List<List<String>> optionKeys);

    /**
     * Chooses a weapon to discard from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseWeaponToDiscard(List<List<String>> optionKeys);

    /**
     * Chooses a weapon to reload from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseWeaponToReload(List<List<String>> optionKeys);

    /**
     * Chooses an Action from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseAction(List<List<String>> optionKeys);

    /**
     * Chooses a powerup for paying from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int choosePowerupForPaying(List<List<String>> optionKeys);

    /**
     * Chooses a tagback grenade from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseUseTagBack(List<List<String>> optionKeys);

    /**
     * Chooses a target from the given options.
     *
     * @param optionKeys the possible answers
     * @return the index of the answer
     */
    int chooseTarget(List<List<String>> optionKeys);

    /**
     * Asks to choose a name.
     *
     * @return the chosen name
     */
    String askName();

    /**
     * Sends a notification to the user.
     *
     * @param notificationKey the resource key used to retrieve the
     *                        notification message
     */
    void sendNotification(String notificationKey);

    /**
     * Sets the controller in the view.
     *
     * @param controller the controller to set
     */
    void setController(ClientController controller);

    /**
     * Sets the model in the view.
     *
     * @param model the model to set
     */
    void setModel(MatchState model);
}
