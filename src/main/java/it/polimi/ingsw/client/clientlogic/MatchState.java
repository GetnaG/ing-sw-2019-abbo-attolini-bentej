package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.client.interaction.GUI.GUI;
import it.polimi.ingsw.client.interaction.InteractionInterface;
import it.polimi.ingsw.communication.protocol.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the state of a Match.
 * <p>
 * It uses an Observer/Subscriber pattern to notify to {@linkplain InteractionInterface} that the state has been updated.
 *
 * @author Fahed B. Tej
 * @see InteractionInterface
 * @see ClientController
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
                        Boolean.parseBoolean(update.getNewValue().get(0)));
                break;
            case KILLSHOT_TRACK://with separator
                boardState.setKillshotTrack(
                        update.getNewValue().stream()
                                .map(s -> s.split(Update.SEPARATOR))
                                .map(array -> Arrays.asList(array))
                                .collect(Collectors.toList()));
                break;
            case IS_ACTION_TILE_FRENZY:
                boardState.setIsActionTileFrenzy(Boolean.parseBoolean(update.getNewValue().get(0)));
                break;
            // setting player state
            case TURN_POSITION:
                update.getNewValue().forEach(s -> {
                    if (playersState.stream().noneMatch(state -> state.getNickname().equals(s)))
                        playersState.add(new PlayerState(update.getNewValue().indexOf(s), s));
                });
                playersState.forEach(p -> p.setTurnPosition(update.getNewValue().indexOf(p.getNickname())));
                break;
            case SQUARE_POSITION:
                getReceiverState(update).setSquarePosition(Integer.parseInt(update.getNewValue().get(0)));
                break;
            case AMMO_CUBE_ARRAY:
                getReceiverState(update).setAmmoCubes(
                        update.getNewValue().stream().map(Integer::parseInt).collect(Collectors.toList())
                );
                break;
            case IS_PLAYER_BOARD_FRENZY:
                getReceiverState(update).setPlayerBoardFrenzy(Boolean.parseBoolean(update.getNewValue().get(0)));
                break;
            case SKULL_NUMBER:
                getReceiverState(update).setSkullNumber(Integer.parseInt(update.getNewValue().get(0)));
                break;
            case DAMAGE_ARRAY:
                getReceiverState(update).setDamage(update.getNewValue());
                break;
            case MARKS_ARRAY:
                getReceiverState(update).setMarks(update.getNewValue());
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
                playersState.forEach(playerState -> playerState.setConnected(
                        boardState.getConnectedPlayers().contains(playerState.getNickname())));
                break;

            case HALL_TIMER:
                //TODO what to do with hall timer
                int timer = Integer.parseInt(update.getNewValue().get(0));
                GUI.updateTimer(timer);
                break;
            case GAME_OVER:
                //TODO the provided list contains the winners in the right order
                break;
            case CURRENT_PLAYER:
                playersState.forEach(playerState -> playerState.setCurrent(playerState.getNickname().equals(update.getNewValue().get(0))));
            default:
                // nothing
        }

        subscribedInteractionInterfaces.forEach(InteractionInterface::notifyUpdatedState);
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
