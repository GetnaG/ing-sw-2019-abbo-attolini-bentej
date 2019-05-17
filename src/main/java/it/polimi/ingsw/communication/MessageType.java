package it.polimi.ingsw.communication;

/**
 * This class defines the type of messages exchanged.
 *
 * @author Abbo Giulio A.
 * @see ProtocolMessage
 */
public enum MessageType {
    /**
     * Choose between sequences of effects.
     */
    EFFECTS_SEQUENCE("Please choose an effect sequence.", true),
    /**
     * Choose where to spawn.
     */
    SPAWN("Please choose a card to use for spawning.", true),
    /**
     * Choose what powerup to use.
     */
    POWERUP("Please choose a powerup", true),
    /**
     * Choose a square.
     */
    DESTINATION("Please choose a square.", true),
    /**
     * Choose a weapon.
     */
    WEAPON("Please choose a weapon.", true),
    /**
     * Choose which weapon to buy.
     */
    WEAPON_TO_BUY("Please choose a weapon to be bought.", true),
    /**
     * Choose which weapon to discard.
     */
    WEAPON_TO_DISCARD("Please choose a weapon to be discarded.", true),
    /**
     * Choose which weapon to reload.
     */
    WEAPON_TO_RELOAD("Please choose a weapon to be reloaded.", true),
    /**
     * Choose an action.
     */
    ACTION("Please choose an action.", true),
    /**
     * Choose which powerup to use for paying.
     */
    POWERUP_FOR_PAYING("Please choose a powerup to cover the cost.", true),
    /**
     * Choose which tagback card to use.
     */
    USE_TAGBACK("Please choose which tagback to use.", true),
    /**
     * Choose a target.
     */
    TARGET("Please select a target.", true),
    /**
     * Choose a nickname.
     */
    NICKNAME("Please choose a nickname.", false),
    /**
     * The message contains some updates.
     */
    UPDATE("Update", false),
    /**
     * The message contains some notifications.
     */
    NOTIFICATION("Notification", false);

    private final String message;//TODO: remove

    /**
     * Whether this value allows the user choose between provided options.
     */
    private final boolean hasOptions;

    /**
     * This constructor allows to specify fields for the enum.
     *
     * @param message    the string that will be sent
     * @param hasOptions whether the user is expected to choose between
     *                   provided options
     */
    MessageType(String message, boolean hasOptions) {
        this.message = message;
        this.hasOptions = hasOptions;
    }

    /**
     * Returns the string associated with this constant.
     *
     * @return the string associated with this
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns whether the user is expected to choose between provided options.
     *
     * @return whether the user is expected to choose between provided options
     */
    public boolean hasOptions() {
        return hasOptions;
    }
}
