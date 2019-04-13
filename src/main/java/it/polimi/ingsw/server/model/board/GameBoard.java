package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;

/**
 * Represents the Game Board used in the game. It contains all the information about the configuration of the rooms, the decks used and the mode in which the Game is played.
 * @author Fahed Ben Tej
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
     * Discarded Powerup Cards.
     */
    private List<PowerupCard> discardedPowerups;


    /**
     * Discarded Ammo Cards.
     */
    private List<PowerupCard> discardedAmmos;



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

        //Discarded cards
        discardedPowerups = new ArrayList<>();
        discardedAmmos = new ArrayList<>();
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
     * Finds the {@code Square} which contains the {@code Spawn} of the given {@code Color}
     * @param color the color of the spawn to find
     * @return a {@code Square} which contains the {@code Spawn} of the given {@code Color}
     */
    public Square findSpawn(AmmoCube color) {

        return null;
    }

    /**
     * Gets all the valid squares in which a player can move. The {@code sameDirection} flag is used to specify the constraint of moving in only one direction.
     * @param start  starting square
     * @param maxDistance  maximum moves square-to-square permitted
     * @param sameDirection flag used to specify the constraint of moving in only one direction.
     * @return all the squares in which a player can move
     */
    public List<Square> getValidDestinations(Square start, int maxDistance, boolean sameDirection) {

        //List<Square> validDestinations = new ArrayList<>();
       // if (maxDistance == 0) return validDestinations;

        return null;
    }

    /**
     * Used to insert tokens in the Track.
     * @param tokens the player who did the kill. If there's an overkill, {@code tokens} must contain two times the same player.
     *              In case of 3+ kills, they are counted as 2. If tokens is empty, no kill is counted.
     *
     */
    public void addTokensAndRemoveSkull (List<Player> tokens) {
        // TODO implement here
    }

    /**
     * Checks whether Final Frenzy is triggered.
     * @return boolean indicating whether Final Frenzy is triggered.
     */
    public boolean checkFinalFrenzy() {
        // TODO implement here
        return false;
    }

    /**
     * Scores the track.
     */
    public void scoreBoard() {
        // TODO implement here
    }

    /**
     * Inserts an Ammo Card in the {@code discardedAmmos} set.
     * @param usedCard the discarded Ammo Card.
     */
    public void putAmmoCard(AmmoCard usedCard) {
        // TODO implement here
    }

    /**
     * Inserts an Powerup Card in the {@code discardedPowerups} set.
     * @param usedCard the discarded Powerup Card.
     */
    public void putPowerupCard(PowerupCard usedCard) {
        // TODO implement here
    }

    /**
     *
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