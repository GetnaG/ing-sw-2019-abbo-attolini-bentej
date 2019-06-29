package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Once a player is dead, at the end of the active player, we have to:
 * (1) score its killboard
 * (2) remove every damage from player board.
 * (3) make him draw a powerup card. He chooses the powerup which determines his next spawn.
 * This class takes care only of point (3).
 *
 * @author Fahed Ben Tej
 */
public class RespawnTurn implements TurnInterface {
    private Consumer<UpdateBuilder> updater;

    public RespawnTurn(Consumer<UpdateBuilder> updater) {
        this.updater = updater;
    }

    /**
     * Makes the player draw a powerup card. He chooses the powerup which determines his next spawn.
     */
    public void startTurn(Player subjectPlayer, List<Damageable> allTargets, GameBoard board) {

        /*Drawing a card*/
        List<PowerupCard> availablePowerups = new ArrayList<>(subjectPlayer.getAllPowerup());
        PowerupCard drawnCard = board.getPowerupCard();
        availablePowerups.add(drawnCard);

        /*Making the player choose a powerup*/
        PowerupCard cardChosen;
        try {
            cardChosen = subjectPlayer.getToClient().chooseSpawn(availablePowerups);
        } catch (ToClientException e) {

            /*The player is offline: default choice*/
            cardChosen = drawnCard;
        }

        /*Discarding the chosen card and adding the new one*/
        try {
            subjectPlayer.removePowerup(cardChosen);
            subjectPlayer.addPowerup(drawnCard);
        } catch (IllegalArgumentException e) {
            /*The chosen card was not in the player's hand, it was the new one*/
        }

        /*Setting the player position in the spawn determined by that square*/
        subjectPlayer.setPosition(board.findSpawn(cardChosen.getCube()));

        updater.accept(new UpdateBuilder()
                .setPowerupsInHand(subjectPlayer, subjectPlayer.getAllPowerup())
                .setPlayerPosition(subjectPlayer, subjectPlayer.getPosition()));
    }
}