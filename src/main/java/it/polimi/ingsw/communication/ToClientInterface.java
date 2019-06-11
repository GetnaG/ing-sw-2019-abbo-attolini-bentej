package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.protocol.Notification;
import it.polimi.ingsw.communication.protocol.Update;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.List;

/**
 * This interface is used to communicate with the client.
 * <p>
 * If some options are provided as arguments, this ensures that the answer
 * was one of them. In other words, the returned value is always one of the
 * options if provided, unless an exceptions is thrown.
 * <p>
 * If no options are provided, the returned value will not be checked.
 * <p>
 * An exceptions is thrown if there are problems with the communication or if
 * the client does not answer in a certain amount of time; in this case the
 * client must be suspended.
 *
 * @author Abbo Giulio A.
 * @see ToClientException
 */
public interface ToClientInterface {

    /**
     * Asks the client to choose one of the effects chains from the provided
     * list.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    EffectInterface chooseEffectsSequence(List<EffectInterface> options) throws ToClientException;

    /**
     * Asks the client to choose one of the spawn location from the provided
     * list.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    PowerupCard chooseSpawn(List<PowerupCard> options) throws ToClientException;

    /**
     * Asks the client to choose one of the powerups from the provided list.
     * The client is allowed to choose none.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException      if there are problems with the communication
     *                                or the client does not answer in time
     * @throws ChoiceRefusedException if the client refuses the options
     */
    PowerupCard choosePowerup(List<PowerupCard> options) throws ToClientException, ChoiceRefusedException;

    /**
     * Asks the client to choose one of the squares from the provided list.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    Square chooseDestination(List<Square> options) throws ToClientException;

    /**
     * Asks the client to choose one of the weapons from the provided list.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    WeaponCard chooseWeaponCard(List<WeaponCard> options) throws ToClientException;

    /**
     * Asks the client to choose which of the weapons he wants to buy.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    WeaponCard chooseWeaponToBuy(List<WeaponCard> options) throws ToClientException;

    /**
     * Asks the client to choose which weapons he wants to discard.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    WeaponCard chooseWeaponToDiscard(List<WeaponCard> options) throws ToClientException;

    /**
     * Asks the client to choose which of the weapons he wants to reload.
     * The client is allowed to choose none.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException      if there are problems with the communication
     *                                or the client does not answer in time
     * @throws ChoiceRefusedException if the client refuses the options
     */
    WeaponCard chooseWeaponToReload(List<WeaponCard> options) throws ToClientException, ChoiceRefusedException;

    /**
     * Asks the client to choose one of the actions from the provided list.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    Action chooseAction(List<Action> options) throws ToClientException;

    /**
     * Asks the client to choose one of the powerups from the provided list
     * to be used for paying.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    PowerupCard choosePowerupForPaying(List<PowerupCard> options) throws ToClientException;

    /**
     * Asks the client to choose one of the {@code Tagback Grenades}.
     * The client is allowed to choose none.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException      if there are problems with the communication
     *                                or the client does not answer in time
     * @throws ChoiceRefusedException if the client refuses the options
     */
    PowerupCard askUseTagback(List<PowerupCard> options) throws ToClientException, ChoiceRefusedException;

    /**
     * Asks the client to choose a set of targets to hit.
     *
     * @param options the possible choices
     * @return the client's choice, one of the options
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    List<Damageable> chooseTarget(List<List<Damageable>> options) throws ToClientException;

    /**
     * Asks the client to choose a nickname.
     *
     * @return the client's choice
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    String chooseUserName() throws ToClientException;

    /**
     * Closes the connection.
     *
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    void quit() throws ToClientException;

    /**
     * Sends the provided notification to the client.
     *
     * @param type the type of the notification
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    void sendNotification(Notification.NotificationType type) throws ToClientException;

    /**
     * Sends the provided update to the client.
     *
     * @param update the update to send
     * @throws ToClientException if there are problems with the communication
     *                           or the client does not answer in time
     */
    void sendUpdate(Update update) throws ToClientException;
}