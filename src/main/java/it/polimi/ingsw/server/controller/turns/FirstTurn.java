package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ChoiceRefusedException;
import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.communication.UpdateBuilder;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.PowerupDeck;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;

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
    private Consumer<UpdateBuilder> updater;

    public FirstTurn(Consumer<UpdateBuilder> updater) {
        this.updater = updater;
    }

    /**
     * Once the player draws two cards from the powerup deck, he chooses one which defines the Spawn.
     * @param currentPlayer
     * @param board GameBoard
     */
    public int startTurn(Player currentPlayer, GameBoard board) {

        List<PowerupCard> cardsDrawn = new ArrayList<>();

        List<PowerupCard> drawnPowerups = new ArrayList<>();
        PowerupCard cardChosen ;

        // Drawing two cards
        PowerupCard firstPowerup = board.getPowerupCard();
        PowerupCard secondPowerup = board.getPowerupCard();
        drawnPowerups.add(firstPowerup);
        drawnPowerups.add(secondPowerup);
        // Player choosen a card, the other one is discarded.
        try{
            cardChosen = currentPlayer.getToClient().chooseSpawn(drawnPowerups);
            currentPlayer.addPowerup(cardChosen);
            drawnPowerups.remove(cardChosen);
            if (cardChosen.equals(firstPowerup)) {
                board.putPowerupCard(secondPowerup);
            } else {
                board.putPowerupCard(firstPowerup);
            }

        } catch ( ToClientException e){
            // player is not reachable.
            // (1) making a default move
            cardChosen = drawnPowerups.get(0);
            board.putPowerupCard(drawnPowerups.get(1));
            // (2) taking him offline: already done by the User class (calls matchSuspensionListener)
        }

        currentPlayer.setPosition(board.findSpawn(cardChosen.getCube()));
        updater.accept(new UpdateBuilder()
                .setPowerupsInHand(currentPlayer, currentPlayer.getAllPowerup())
                .setPlayerPosition(currentPlayer, currentPlayer.getPosition()));
        return 0;
    }



}