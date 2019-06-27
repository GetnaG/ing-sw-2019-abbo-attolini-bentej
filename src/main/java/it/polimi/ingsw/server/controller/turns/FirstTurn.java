package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * In the first turn of the game, a Player has to draw two cards from the Powerup Deck and choose only one.
 * The color of the Powerup Card chosen will define the Spawn Point of that Player.
 *
 * @author Fahed Ben Tej
 */
public class FirstTurn implements TurnInterface {
    /**
     * This will update all the players.
     */
    private Consumer<? super UpdateBuilder> updater;

    /**
     * A first turn that will update players with the provided method.
     *
     * @param updater the method that will update the players
     */
    public FirstTurn(Consumer<? super UpdateBuilder> updater) {
        this.updater = updater;
    }

    /**
     * Once the player draws two cards from the powerup deck, he chooses one which defines the Spawn.
     * The other is added to his hand.
     *
     * @param currentPlayer the player that spawns
     * @param board         the GameBoard
     * @return -1 if Final Frenzy is triggered, else 0
     */
    public int startTurn(Player currentPlayer, GameBoard board) {
        List<PowerupCard> drawnPowerups = new ArrayList<>();
        PowerupCard cardChosen;
        PowerupCard notChosen;

        /*Drawing two cards*/
        drawnPowerups.add(board.getPowerupCard());
        drawnPowerups.add(board.getPowerupCard());

        /*Player chooses a card to spawn*/
        try {
            cardChosen = currentPlayer.getToClient().chooseSpawn(drawnPowerups);
            drawnPowerups.remove(cardChosen);
            notChosen = drawnPowerups.get(0);
        } catch (ToClientException e) {

            /*The player is not reachable: default choice*/
            cardChosen = drawnPowerups.get(0);
            notChosen = drawnPowerups.get(1);
        }

        /*Spawning the player and adding the other card to his hand*/
        currentPlayer.setPosition(board.findSpawn(cardChosen.getCube()));
        board.putPowerupCard(notChosen);
        currentPlayer.addPowerup(cardChosen);

        /*Updating the position and the powerups in hand*/
        updater.accept(new UpdateBuilder()
                .setPowerupsInHand(currentPlayer, currentPlayer.getAllPowerup())
                .setPlayerPosition(currentPlayer, currentPlayer.getPosition()));
        return 0;
    }
}