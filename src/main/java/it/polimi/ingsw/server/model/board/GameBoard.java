package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;

/**
 * Represents the Game Board used in the game. It contains all the information about the configuration of the rooms, the decks used and the mode in which the Game is played.
 */
public class GameBoard implements ReplaceListener {



    /**
     * The configuration of the rooms. Note that there exists only 4 possible configurations.
     */
    private List<Room> configuration;

    /**
     * Powerup Deck used during the game.
     */
    private PowerupDeck powerupDeck;


    /**
     * Weapon Deck used during the game.
     */
    private WeaponDeck weaponDeck;

    /**
     * Ammo Deck used during the game.
     */
    private AmmoDeck ammoDeck;

    /**
     * Track used during the game. It defines the mode in which the game is played.
     */
    private AbstractTrack track;

    /**
     * Construct a GameBoard using the {@track} which defines the mode and the {@code configuration} of the rooms .
     * @param track defines the mode in which the game will be played. {@see KillshotTrack} {@see DominationTrack} {@see TurretTrack}
     * @param configuration defines the rooms which compose the boards. Note that rooms are composed squares.
     */
    public GameBoard(AbstractTrack track, List<Room> configuration) {
        this.track = track;
        this.configuration = configuration;

        // Decks are shuffled when created
        powerupDeck = new PowerupDeck();
        weaponDeck = new WeaponDeck();
        ammoDeck = new AmmoDeck();
    }

    /**
     * Gets a random Ammo Card from the Ammo Deck. Note that Ammo Card can always be drawn. {@see AmmoDeck}
     * @return random Ammo Card drawn from the Ammo Deck.
     */
    public AmmoCard getAmmoCard() {
        return ammoDeck.drawCard();
    }

    /**
     * Gets a random Powerup Card from the Powerup Deck. Note that Powerup Card can always be drawn {@see PowerupDeck}.
     * @return random Powerup Card from the Powerup Deck.
     */
    public PowerupCard getPowerupCard() {
        return powerupDeck.drawCard();
    }

    /**
     * Gets a random Weapon Card from the Weapon Deck. Note that Weapon Cards can't be drawn if the Deck is empty {@see WeaponDeck}.
     * @return random Weapon Card from the Weapon Deck.
     *
     */
    public WeaponCard getWeaponCard() throws AgainstRulesException {
        return weaponDeck.drawCard();
    }

    /**
     * @param color 
     * @return
     */
    public Square findSpawn(AmmoCube color) {
        // TODO implement here
        return null;
    }

    /**
     * @param start 
     * @param maxDistance 
     * @param sameDirection 
     * @return
     */
    public List<Square> getValidDestinations(Square start, int maxDistance, boolean sameDirection) {
        // TODO implement here
        return null;
    }

    /**
     * @param tokens
     */
    public void addTokensAndRemoveSkull(List<Player> tokens) {
        // TODO implement here
    }

    /**
     * @return
     */
    public boolean checkFinalFrenzy() {
        // TODO implement here
        return false;
    }

    /**
     * 
     */
    public void scoreBoard() {
        // TODO implement here
    }

    /**
     * @param usedCard
     */
    public void putAmmoCard(AmmoCard usedCard) {
        // TODO implement here
    }

    /**
     * @param usedCard
     */
    public void putPowerupCard(PowerupCard usedCard) {
        // TODO implement here
    }

    /**
     * @param toBeReplaced
     */
    public void addTurretSquare(TurretSquare toBeReplaced) {
        // TODO implement here
    }

    /**
     * @param toBeReplaced
     */
    public void addSpawnSquare(SpawnSquare toBeReplaced) {
        // TODO implement here
    }

    /**
     * @param location 
     * @param weapons
     */
    public void replaceDiscardedWeapons(Square location, List<WeaponCard> weapons) {
        // TODO implement here
    }

    /**
     * 
     */
    public void replaceAll() {
        // TODO implement here
    }



}