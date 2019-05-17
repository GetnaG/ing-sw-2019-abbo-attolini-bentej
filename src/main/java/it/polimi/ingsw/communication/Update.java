package it.polimi.ingsw.communication;

/**
 * This represents an update element.
 * Updates are messages that do not expect an answer but modify the game status.
 *
 * @author Abbo Giulio A.
 * @see ProtocolMessage
 */
class Update {
    /**
     * The type of this update, the field modified.
     */
    UpdateType type;
    /**
     * The new value for the field affected.
     */
    String newValue;

    /**
     * The available field that can be modified.
     */
    enum UpdateType {
    }
}
