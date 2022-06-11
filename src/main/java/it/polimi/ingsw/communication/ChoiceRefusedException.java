package it.polimi.ingsw.communication;

/**
 * This exception notifies the caller that the user refuses all the options.
 * A method of {@link ToClientInterface} throws this when the user choice is
 * optional, meaning that the user can refuse to choose.
 *
 * This approach was preferred over the alternatives - returning null and
 * using a special object that denotes a <i>non-choice</i> - for two reasons:
 * it explicitly states whether a method is not mandatory thus forcing the
 * caller to handle this eventuality; and is nicer than checking if the
 * returned value is null or creating some mock object.
 *
 * @author giubots
 * @see ToClientInterface
 */
public class ChoiceRefusedException extends Exception {
    /*Simple exception with no details*/
}
