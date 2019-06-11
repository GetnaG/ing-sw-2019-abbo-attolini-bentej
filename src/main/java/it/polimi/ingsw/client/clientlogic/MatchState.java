package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.protocol.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the state of a Match.
 *
 * It uses an Observer/Subscriber pattern to notify to {@linkplain InteractionInterface} that the state has been updated.
 *
 * @see InteractionInterface
 * @see ClientController
 *
 * @author Fahed B. Tej
 */
public class MatchState {

    private List<PlayerState> playersState;

    private BoardState boardState;
    private List<InteractionInterface> subscribedInteractionInterfaces;

    public MatchState() {
        this.playersState = new ArrayList<>();
        this.boardState = new BoardState();

        subscribedInteractionInterfaces = new ArrayList<>();
    }

    public void subscribe(InteractionInterface interactionInterface) {
        subscribedInteractionInterfaces.add(interactionInterface);
    }

    public void handleUpdate(Update update) {
        switch (update.getType()) {
            case CONFIGURATION_ID:
                boardState.setConfigurationID(Integer.parseInt(update.getNewValue().get(0)));
                break;
            case AMMO_CARD_ARRAY:
                boardState.setAmmoCardsID(update.getNewValue());
                break;
            case WEAPON_CARD_ARRAY:
                boardState.setWeaponCardID(update.getNewValue());
                break;
            case IS_WEAPON_DECK_DRAWABLE:
                boardState.setIsWeaponDeckDrawable(
                        Integer.parseInt(update.getNewValue().get(0)) > 0
                );
                break;
            case KILLSHOT_TRACK://with separator
                boardState.setKillshotTrack(
                        update.getNewValue().stream()
                                .map(s -> s.split(";"))
                                .map(array -> Arrays.asList(array))
                                .collect(Collectors.toList()));
                break;
            case IS_ACTION_TILE_FRENZY:
                boardState.setIsActionTileFrenzy(update.getNewValue().get(0).equals("true"));
                break;
                // setting player state
            case TURN_POSITION:
                playersState.forEach(p -> p.setTurnPosition(update.getNewValue().indexOf(p.getNickname())));
                break;
            case SQUARE_POSITION:
                getReceiverState(update).setSquarePosition(Integer.parseInt(update.getNewValue().get(0)));
                break;
            case NICKNAME:
                //
            case AMMO_CUBE_ARRAY:
                getReceiverState(update).setAmmoCubes(
                        update.getNewValue().stream().map(Integer::parseInt).collect(Collectors.toList())
                );
                break;
            case IS_PLAYER_BOARD_FRENZY:
                getReceiverState(update).setPlayerBoardFrenzy(update.getNewValue().get(0).equals("true"));
                break;
            case SKULL_NUMBER:
                getReceiverState(update).setSkullNumber(Integer.parseInt(update.getNewValue().get(0)));
                break;
            case DAMAGE_ARRAY:
                getReceiverState(update).setDamage(update.getNewValue());
                break;
            case IS_CONNECTED:
                getReceiverState(update).setConnected(update.getNewValue().get(0).equals("true"));
                break;
            case LOADED_WEAPONS:
                getReceiverState(update).setLoadedWeapons(update.getNewValue());
                break;
            case UNLOADED_WEAPON:
                getReceiverState(update).setUnloadedWeapons(update.getNewValue());
                break;
            case POWERUPS:
                getReceiverState(update).setPowerups(update.getNewValue());
                break;
            case CONNECTED_PLAYERS:
                boardState.setConnectedPlayers(update.getNewValue());
                subscribedInteractionInterfaces.forEach(i -> i.notifyUpdatedState());
                break;

            case HALL_TIMER:
                break;
            case GAME_OVER:
                break;
            default:
                // nothing
        }

    }

    private PlayerState getReceiverState(Update update) {
        return playersState.stream()
                .filter(p -> p.getNickname().equals(update.getNickname()))
                .collect(Collectors.toList()).get(0);
    }

    public int getConfigurationID() {
        return boardState.getConfigurationID();
    }

    public List<String> getAmmoCardsID() {
        return boardState.getAmmoCardsID();
    }

    public List<String> getWeaponsCardsID() {
            return boardState.getAmmoCardsID();
    }

    public boolean getIsWeaponDeckDrawable() {
        return boardState.isIsWeaponDeckDrawable();
    }

    public List<List<String>> getKillshotTrack() {
        return boardState.getKillshotTrack();
    }

    public boolean getIsActionTileFrenzy() {
        return boardState.isIsActionTileFrenzy();
    }

    public List<PlayerState> getPlayersState() {
        return playersState;
    }

    public List<String> getConnectedPlayers() {
        return boardState.getConnectedPlayers();
    }

    public List<String> getDisconnectedPlayers() {
        return boardState.getDisconnectedPlayers();
    }

    public List<String> getJustConnectedPlayers() {
        return boardState.getJustConnectedPlayers();
    }

}
