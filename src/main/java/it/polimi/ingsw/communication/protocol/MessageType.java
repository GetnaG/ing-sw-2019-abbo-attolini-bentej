package it.polimi.ingsw.communication.protocol;

/**
 * This class defines the type of messages exchanged.
 * The type can be {@linkplain #NOTIFICATION} or {@linkplain #UPDATE}, or
 * other commands.
 *
 * @author giubots
 * @see ProtocolMessage
 */
public enum MessageType {
    /**
     * Choose between sequences of effects.
     */
    EFFECTS_SEQUENCE(true),
    /**
     * Choose where to spawn.
     */
    SPAWN(true),
    /**
     * Choose what powerup to use.
     */
    POWERUP(true),
    /**
     * Choose a square.
     */
    DESTINATION(true),
    /**
     * Choose a weapon.
     */
    WEAPON(true),
    /**
     * Choose which weapon to buy.
     */
    WEAPON_TO_BUY(true),
    /**
     * Choose which weapon to discard.
     */
    WEAPON_TO_DISCARD(true),
    /**
     * Choose which weapon to reload.
     */
    WEAPON_TO_RELOAD(true),
    /**
     * Choose an action.
     */
    ACTION(true),
    /**
     * Choose which powerup to use for paying.
     */
    POWERUP_FOR_PAYING(true),
    /**
     * Choose which tagback card to use.
     */
    USE_TAGBACK(true),
    /**
     * Choose a target.
     */
    TARGET(true),
    /**
     * Choose a nickname.
     */
    NICKNAME(false),
    /**
     * The message contains some updates.
     */
    UPDATE(false),
    /**
     * The message contains some notifications.
     */
    NOTIFICATION(false);

    /**
     * Whether this value allows the user choose between provided options.
     */
    private final boolean hasOptions;

    /**
     * This constructor allows to specify fields for the enum.
     *
     * @param hasOptions whether the user is expected to choose between
     *                   provided options
     */
    MessageType(boolean hasOptions) {
        this.hasOptions = hasOptions;
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
