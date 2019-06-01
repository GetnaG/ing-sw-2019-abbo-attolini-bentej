package it.polimi.ingsw.communication.protocol;

import java.util.List;

/**
 * This represents an update element.
 * Updates are messages that do not expect an answer but modify the game status.
 *
 * @author Abbo Giulio A.
 * @see ProtocolMessage
 */
public class Update {
    /**
     * The separator that must be put between elements of the same sequence.
     */
    public static final String SEPARATOR = "|@|";

    /**
     * The type of this update, the field modified.
     */
    private UpdateType type;
    /**
     * The new value for the field affected.
     */
    private List<String> newValue;
    /**
     * Identifies the receiver of the update.
     */
    private String nickname;

    /**
     * Creates an update that involves a specific player.
     *
     * @param type     the field modified
     * @param newValue the new value for the field
     * @param nickname the name of the involved player
     */
    public Update(UpdateType type, List<String> newValue, String nickname) {
        this.type = type;
        this.newValue = newValue;
        this.nickname = nickname;
    }

    /**
     * Creates an update that does not involve a specific player.
     *
     * @param type     the field modified
     * @param newValue the new value for the field
     */
    public Update(UpdateType type, List<String> newValue) {
        this.type = type;
        this.newValue = newValue;
    }

    /**
     * Returns the type of this update.
     *
     * @return the type of this update
     */
    public UpdateType getType() {
        return type;
    }

    /**
     * Returns the new value for the field involved in this update.
     * A value can be a sequence of elements.
     *
     * @return the new value for the field involved in this update
     */
    public List<String> getNewValue() {
        return newValue;
    }

    /**
     * Returns the name of the player involved in this update.
     *
     * @return the name of the player involved in this update
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * The available field that can be modified.
     */
    public enum UpdateType {
        /**
         * The configuration of the game board.
         */
        CONFIGURATION_ID,
        /**
         * The cards in a player's hand.
         */
        AMMO_CARD_ARRAY,
        /**
         * The cards in a player's hand.
         */
        WEAPON_CARD_ARRAY,
        /**
         * Whether there are still cards in the weapon deck.
         */
        IS_WEAPON_DECK_DRAWABLE,
        /**
         * The elements in the killshot track.
         * Each element of the {@linkplain #newValue} array will correspond
         * to a space in the killshot track; if multiple elements are in the
         * same space, they are separated by {@linkplain #SEPARATOR}.
         */
        KILLSHOT_TRACK,
        /**
         * Whether the action tile for the provided player is frenzy.
         */
        IS_ACTION_TILE_FRENZY,
        /**
         * The sequence followed in the turns; the first element of
         * {@linkplain #newValue} is the "first player".
         */
        TURN_POSITION,
        /**
         * The position of the players on the board.
         */
        SQUARE_POSITION,
        /**
         * The nicknames of the players.
         */
        NICKNAME,
        /**
         * The active ammo cubes for the player, BLUE, RED, YELLOW.
         */
        AMMO_CUBE_ARRAY,
        /**
         * Whether frenzy mode is active.
         */
        IS_PLAYER_BOARD_FRENZY,
        /**
         * The number of skulls on the player's board.
         */
        SKULL_NUMBER,
        /**
         * The list of players that damaged this player.
         */
        DAMAGE_ARRAY,
        /**
         * Whether the player specified is connected.
         */
        IS_CONNECTED,
        /**
         * The cards in a player's hand.
         */
        LOADED_WEAPONS,
        /**
         * The unloaded weapons.
         */
        UNLOADED_WEAPON,
        /**
         * The cards in a player's hand.
         */
        POWERUPS,
        /**
         * The list of connected players.
         */
        CONNECTED_PLAYERS,
        /**
         * Time for Hall
         */
        HALL_TIMER
    }
}
