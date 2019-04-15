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
    private List<AmmoCard> discardedAmmos;

    /**
     * Contains all the squares where a new Ammo Card is needed.
     */
    List<Square> squareNewAmmoCard;

    /**
     * Contains all the squares where a new Weapon Card is needed.
     */
    List<SpawnSquare> squareNewWeaponCard;






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
     * Construct a GameBoard using the {@code track} which defines the mode and the {@code configuration} of the rooms .
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

        //Squares where there's a card needed to be replaced
        squareNewAmmoCard = new ArrayList<>();
        squareNewWeaponCard = new ArrayList<>();

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
     * @return all the squares in which a player can move. The {@code start} square is excluded.
     */
    public List<Square> getValidDestinations(Square start, int maxDistance, boolean sameDirection) {

        List<Square> validDestinations = new ArrayList<>();

        if (maxDistance == 0)
            return validDestinations;


        if(sameDirection) {
            Square curSquare = start;
            int curDistance = maxDistance;
            // North direction Squares
            while (curSquare.getNorthBorder() != Border.WALL){
                validDestinations.add(curSquare);
                curSquare = curSquare.getNorth();
                curDistance = curDistance -1;
            }

            // East direction Squares
            while (curSquare.getEastBorder() != Border.WALL){
                validDestinations.add(curSquare);
                curSquare = curSquare.getEast();
                curDistance = curDistance -1;
            }

            // South direction Squares
            while (curSquare.getSouthBorder() != Border.WALL){
                validDestinations.add(curSquare);
                curSquare = curSquare.getSouth();
                curDistance = curDistance -1;
            }

            // West direction Squares
            while (curSquare.getWestBorder() != Border.WALL){
                validDestinations.add(curSquare);
                curSquare = curSquare.getWest();
                curDistance = curDistance -1;
            }

            return validDestinations;
        } else {
            // Get all squares in the radius defined by maxDistance
            radiusValidDestinations(start,maxDistance,validDestinations);

            return validDestinations;

        }



    }

    /**
     * Helper method for {@link GameBoard#getValidDestinations(Square, int, boolean)}.
     * This method uses recursion to find the valid destinations.
     */
    private void radiusValidDestinations(Square start, int maxDistance, List<Square> alreadyVisitedSquares){

        if(maxDistance == 0) return;

        if(start.getNorthBorder() != Border.WALL){
            alreadyVisitedSquares.add(start.getNorth());
            radiusValidDestinations(start.getNorth(), maxDistance-1, alreadyVisitedSquares);
        }
        if(start.getEastBorder() != Border.WALL){
            alreadyVisitedSquares.add(start.getEast());
            radiusValidDestinations(start.getEast(), maxDistance-1, alreadyVisitedSquares);
        }
        if(start.getSouthBorder() != Border.WALL){
            alreadyVisitedSquares.add(start.getSouth());
            radiusValidDestinations(start.getSouth(), maxDistance-1, alreadyVisitedSquares);
        }
        if(start.getWestBorder() != Border.WALL){
            alreadyVisitedSquares.add(start.getWest());
            radiusValidDestinations(start.getWest(), maxDistance-1, alreadyVisitedSquares);
        }

    }

    /**
     * Used to insert tokens in the Track.
     * @param tokens the player who did the kill. If there's an overkill, {@code tokens} must contain two times the same player.
     *              In case of 3+ kills, they are counted as 2. If list {@code tokens} is empty, no kill is counted.
     *
     */
    public void addTokensAndRemoveSkull (List<Player> tokens) {
        track.removeSkull();
        track.addTokens(tokens);
    }

    /**
     * Checks whether Final Frenzy is triggered.
     * @return boolean indicating whether Final Frenzy is triggered.
     */
    public boolean checkFinalFrenzy() {
        return track.getSkullsLeft() == 0;
    }



    /**
     * Scores the track.
     */
    public void scoreBoard() {
        track.score();
    }

    /**
     * Inserts an Ammo Card in the {@code discardedAmmos} set.
     * @param usedCard the discarded Ammo Card.
     */
    public void putAmmoCard(AmmoCard usedCard) {
        discardedAmmos.add(usedCard);

    }

    /**
     * Inserts an Powerup Card in the {@code discardedPowerups} set.
     * @param usedCard the discarded Powerup Card.
     */
    public void putPowerupCard(PowerupCard usedCard) {
        discardedPowerups.add(usedCard);
    }

    /**
     * When an Ammo Card is grabbed from a Square, it is replaced  at the end of the turn.
     * This method stores the squares where there is an Ammo Card to be replaced.
     * @param toBeReplaced the square where there is an Ammo Card to be replaced.
     * The method {@link GameBoard#replaceAll} actually takes care of replacing the Ammo Card at the end of turn.
     */
    public void addTurretSquare(TurretSquare toBeReplaced) {
        squareNewAmmoCard.add(toBeReplaced);
    }

    /**
     * When a Weapon Card is grabbed from a Spawn Square, it is replaced at the end of the turn.
     * This method stores the Spawn Squares where there is a Weapon Card to be replaced.
     * @param toBeReplaced SpawnSquare where there's a Weapon to be replaced.
     * The method {@link GameBoard#replaceAll} actually takes care of replacing the Weapon Card at the end of turn.
     */
    public void addSpawnSquare(SpawnSquare toBeReplaced) {
        squareNewWeaponCard.add(toBeReplaced);
    }

    /**
     * @param location
     * @param weapons
     */
    public void replaceDiscardedWeapons(Square location, List<WeaponCard> weapons) {
        // TODO implement here
    }

    /**
     * Replaces all the Ammo Cards and Weapon Cards grabbed during the game with new Cards.
     */
    public void replaceAll() {

        //Replacing Ammo Cards
        for(Square s : squareNewAmmoCard)
            s.setGrabbable(ammoDeck.drawCard());
        //Replacing Weapon Card
        for (SpawnSquare s : squareNewWeaponCard)
            if (weaponDeck.cardsLeft() !=0){
                try{
                    s.getMarket().addCard(weaponDeck.drawCard());
                }catch(AgainstRulesException e) {}
            }

    }



}