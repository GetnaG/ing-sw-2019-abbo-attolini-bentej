package it.polimi.ingsw.communication.protocol;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This represents an update element.
 * Updates are messages that do not expect an answer but modify the game status.
 *
 * @author Abbo Giulio A.
 * @see ProtocolMessage
 */
public class Update implements Serializable {
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
     * Creates an update that does not involve a specific player with a
     * single message.
     *
     * @param type     the field modified
     * @param newValue the new value for the field
     */
    public Update(UpdateType type, String newValue) {
        this (type, Collections.singletonList(newValue));
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
         * The ammo cards on the board.
         */
        AMMO_CARD_ARRAY,
        /**
         * The weapons in the markets on the board.
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
         * Whether frenzy mode is active.
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
         * The active ammo cubes for the player, BLUE, RED, YELLOW.
         */
        AMMO_CUBE_ARRAY,
        /**
         * Whether the player's board is frenzy.
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
        HALL_TIMER,
        /**
         * Game over, the list of players is ordered, the first is the winner.
         */
        GAME_OVER
    }
}
