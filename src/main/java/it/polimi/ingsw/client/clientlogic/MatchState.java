package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.Update;

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
    private List<InteractionInterface> subscribedIntercationInterfaces;

    public MatchState(List<PlayerState> playersState, BoardState boardState) {
        this.playersState = playersState;
        this.boardState = boardState;
    }

    public void subscribe(InteractionInterface interactionInterface) {
        subscribedIntercationInterfaces.add(interactionInterface);
    }

    public void handleUpdate(Update update) {
        switch (update.getType()) {
            case CONFIGURATION_ID:
                boardState.setConfigurationID(Integer.parseInt(update.getNewValue().get(0)));
                break;
            case AMMOCARD_ARRAY:
                boardState.setAmmoCardsID(update.getNewValue());
            case WEAPONCARD_ARRAY:
                boardState.setWeaponCardID(update.getNewValue());
            case IS_WEAPON_DECK_DRAWABLE:
                boardState.setIsWeaponDeckDrawable(
                        Integer.parseInt(update.getNewValue().get(0)) > 0
                );
            case KILLSHOT_TRACK://with separator
                boardState.setKillshotTrack(
                        update.getNewValue().stream()
                                .map(s -> s.split(";"))
                                .map(array -> Arrays.asList(array))
                                .collect(Collectors.toList()));
            case IS_ACTION_TILE_FRENZY:
                boardState.setIsActionTileFrenzy(update.getNewValue().get(0).equals("true"));
                // setting player state
            case TURN_POSITION:
                getReceiverState(update).setTurnPosition(Integer.parseInt(update.getNewValue().get(0)));
            case SQUARE_POSITION:
                getReceiverState(update).setSquarePosition(Integer.parseInt(update.getNewValue().get(0)));
            case NICKNAME:
                //
            case AMMOCUBE_ARRAY:
                getReceiverState(update).setAmmoCubes(
                        update.getNewValue().stream().map(Integer::parseInt).collect(Collectors.toList())
                );
            case IS_PLAYER_BOARD_FRENZY:
                getReceiverState(update).setPlayerBoardFrenzy(update.getNewValue().get(0).equals("true"));
            case SKULL_NUMBER:
                getReceiverState(update).setSkullNumber(Integer.parseInt(update.getNewValue().get(0)));
            case DAMAGE_ARRAY:
                getReceiverState(update).setDamage(update.getNewValue());
            case IS_CONNECTED:
                getReceiverState(update).setConnected(update.getNewValue().get(0).equals("true"));
            case LOADED_WEAPONS:
                getReceiverState(update).setLoadedWeapons(update.getNewValue());
            case UNLOADED_WEAPON:
                getReceiverState(update).setUnloadedWeapons(update.getNewValue());
            case POWERUPS:
                getReceiverState(update).setPowerups(update.getNewValue());
            default:
                // nothing
        }

        subscribedIntercationInterfaces.forEach(i -> i.notifyUpdatedState());
    }

    private PlayerState getReceiverState(Update update) {
        return playersState.stream()
                .filter(p -> p.getNickname().equals(update.getNickname()))
                .collect(Collectors.toList()).get(0);
    }
}
