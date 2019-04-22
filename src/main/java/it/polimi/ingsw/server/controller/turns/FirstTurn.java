package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.PowerupDeck;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * In the first turn of the game, a Player has to draw two cards from the Powerup Deck and choose only one.
 * The color of the Powerup Card chosen will define the Spawn Point of that Player.
 *
 * @author Fahed Ben Tej
 */
public class FirstTurn implements TurnInterface {

    /**
     * Once the player draws two cards from the powerup deck, he chooses one which defines the Spawn.
     * @param currentPlayer
     * @param board GameBoard
     */
    public void startTurn(Player currentPlayer, GameBoard board) {

        List<PowerupCard> cardsDrawn = new ArrayList<>();

        // Drawing two powerups
        cardsDrawn.add(board.getPowerupCard());
        cardsDrawn.add(board.getPowerupCard());
        //Choosing a card and setting the position according to its color.
        PowerupCard cardChosen = currentPlayer.getToClient().choosePowerup(cardsDrawn);
        currentPlayer.setPosition(board.findSpawn(cardChosen.getCube()));
    }



}