package it.polimi.ingsw.communication;

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
     * The type of this update, the field modified.
     */
    UpdateType type;
    /**
     * The new value for the field affected.
     */
    List<String> newValue;
    /**
     * Identifies the receiver of the update.
     */
    String nickname;

    public UpdateType getType() {
        return type;
    }

    public void setType(UpdateType type) {
        this.type = type;
    }

    public List<String> getNewValue() {
        return newValue;
    }

    public void setNewValue(List<String> newValue) {
        this.newValue = newValue;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * The available field that can be modified.
     */
    public enum UpdateType {
        CONFIGURATION_ID,
        AMMOCARD_ARRAY,
        WEAPONCARD_ARRAY,
        IS_WEAPON_DECK_DRAWABLE,
        KILLSHOT_TRACK,//with separator
        IS_ACTION_TILE_FRENZY,
        TURN_POSITION,
        SQUARE_POSITION,
        NICKNAME,
        AMMOCUBE_ARRAY,
        IS_PLAYER_BOARD_FRENZY,
        SKULL_NUMBER,
        DAMAGE_ARRAY,
        IS_CONNECTED,
        LOADED_WEAPONS,
        UNLOADED_WEAPON,
        POWERUPS
    }
}
