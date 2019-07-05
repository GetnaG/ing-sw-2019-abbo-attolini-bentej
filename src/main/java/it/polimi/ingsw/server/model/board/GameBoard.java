package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the Game Board used in the game. It contains all the information about the configuration of the rooms, the decks used and the mode in which the Game is played.
 *
 * @author Fahed Ben Tej
 */
public class GameBoard implements ReplaceListener {


    /**
     * The configuration of the rooms. Note that there exists only 4 possible configurations.
     */
    private List<Room> configuration;
    /**
     * Contains all the squares where a new Ammo Card is needed.
     */
    private List<Square> squareNewAmmoCard;
    /**
     * Contains all the squares where a new Weapon Card is needed.
     */
    private List<SpawnSquare> squareNewWeaponCard;
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
     *
     * @param track         defines the mode in which the game will be played. {@see KillshotTrack} {@see DominationTrack} {@see TurretTrack}
     * @param configuration defines the rooms which compose the boards. Note that rooms are composed squares.
     */
    public GameBoard(AbstractTrack track, List<Room> configuration) {
        this.track = track;
        this.configuration = configuration;
        configuration.stream().flatMap(room -> room.getAllSquares().stream()).forEach(square -> square.setReplacer(this));
        // Decks are shuffled when created
        powerupDeck = new PowerupDeck(this);
        weaponDeck = new WeaponDeck(this);
        ammoDeck = new AmmoDeck(this);

        //Discarded cards
        discardedPowerups = new ArrayList<>();
        discardedAmmos = new ArrayList<>();

        //Squares where there's a card needed to be replaced
        squareNewAmmoCard = new ArrayList<>();
        squareNewWeaponCard = new ArrayList<>();

        /*Adding all the missing ammo cards and weapons*/
        for (Room room : configuration) {
            for (Square square : room.getAllSquares()) {
                if (square.equals(getRoom(square).getSpawnSquare()))
                    addSpawnSquare(getRoom(square).getSpawnSquare());
                else addSquare(square);
            }
        }
        replaceAll();
    }


    public List<Room> getConfiguration() {
        return configuration;
    }

    public Room getRoom(Square square) {
        for (Room room : configuration) {
            for (Square s : room.getAllSquares()) {
                if (s.getID().equals(square.getID()))
                    return room;
            }
        }
        throw new IllegalArgumentException("Room not found for square " + square.getID());
    }

    /**
     * @param destination is the square that will be checked
     * @return 1 if the destination square is visible from the calling square, 0 otherwise
     */

    public boolean checkVisible(Square departure, Square destination) {

        if (getRoom(departure) == getRoom(destination))
            return true;

        else {
            if (departure.getNorthBorder() != Border.WALL && departure.getNorthBorder() != null) {
                if (getRoom(departure.getNorth()) == getRoom(destination) || departure.getNorthBorder() == Border.CORRIDOR)
                    return true;

            }


            if (departure.getSouthBorder() != Border.WALL && departure.getSouthBorder() != null) {
                if (getRoom(departure.getSouth()) == getRoom(destination) || departure.getSouthBorder() == Border.CORRIDOR)
                    return true;
            }

            if (departure.getEastBorder() != Border.WALL && departure.getEastBorder() != null) {
                if (getRoom(departure.getEast()) == getRoom(destination) || departure.getEastBorder() == Border.CORRIDOR)
                    return true;
            }

            if (departure.getWestBorder() != Border.WALL && departure.getWestBorder() != null) {
                if (getRoom(departure.getWest()) == getRoom(destination) || departure.getWestBorder() == Border.CORRIDOR)
                    return true;
            }
        }

        return false;
    }

    public Square getSquare(int idSquare) {
        for (Room room : configuration)
            for (Square s : room.getAllSquares())
                if (Integer.parseInt(s.getID()) == idSquare)
                    return s;
        return null;
    }

    /**
     * Gets a random Ammo Card from the Ammo Deck. Note that Ammo Card can always be drawn. {@see AmmoDeck}
     *
     * @return random Ammo Card drawn from the Ammo Deck.
     */
    public AmmoCard getAmmoCard() {
        return ammoDeck.drawCard();
    }

    /**
     * Gets a random Powerup Card from the Powerup Deck. Note that Powerup Card can always be drawn {@see PowerupDeck}.
     *
     * @return random Powerup Card from the Powerup Deck.
     */
    public PowerupCard getPowerupCard() {
        return powerupDeck.drawCard();
    }

    /**
     * Gets a random Weapon Card from the Weapon Deck. Note that Weapon Cards can't be drawn if the Deck is empty {@see WeaponDeck}.
     * A call to {@code }
     *
     * @return random Weapon Card from the Weapon Deck.
     */
    public WeaponCard getWeaponCard() throws AgainstRulesException {
        return weaponDeck.drawCard();
    }

    /**
     * Checks if the Weapon Deck is empty
     *
     * @return true if is empty, false otherwise.
     */
    public boolean isWeaponDeckEmpty() {
        return weaponDeck.cardsLeft() > 0;
    }

    /**
     * Finds the {@code Square} which contains the {@code Spawn} of the given {@code SquareColor}
     *
     * @param color the color of the spawn to find
     * @return a {@code Square} which contains the {@code Spawn} of the given {@code SquareColor}
     */
    public SpawnSquare findSpawn(AmmoCube color) {
        List<SpawnSquare> spawns = configuration.stream()
                .filter(Room::hasSpawnSquare).map(x -> x.getSpawnSquare()).filter(x -> x.getSpawnColor() == color).collect(Collectors.toList());

        return spawns.get(0);
    }

    /**
     * Gets all the valid squares in which a player can move. The {@code sameDirection} flag is used to specify the constraint of moving in only one direction.
     *
     * @param start         starting square
     * @param maxDistance   maximum moves square-to-square permitted
     * @param sameDirection flag used to specify the constraint of moving in only one direction.
     * @return all the squares in which a player can move. The {@code start} square is excluded.
     */
    public List<Square> getValidDestinations(Square start, int maxDistance, boolean sameDirection) {

        List<Square> validDestinations = new ArrayList<>();

        if (maxDistance == 0)
            return validDestinations;


        if (sameDirection) {
            Square curSquare = start;
            int curDistance = maxDistance;
            // North direction Squares
            while (curSquare.getNorthBorder() != Border.WALL && curDistance > 0) {
                validDestinations.add(curSquare.getNorth());
                curSquare = curSquare.getNorth();
                curDistance = curDistance - 1;
            }
            curSquare = start;
            curDistance = maxDistance;
            // East direction Squares
            while (curSquare.getEastBorder() != Border.WALL && curDistance > 0) {
                validDestinations.add(curSquare.getEast());
                curSquare = curSquare.getEast();
                curDistance = curDistance - 1;
            }
            curSquare = start;
            curDistance = maxDistance;
            // South direction Squares
            while (curSquare.getSouthBorder() != Border.WALL && curDistance > 0) {
                validDestinations.add(curSquare.getSouth());
                curSquare = curSquare.getSouth();
                curDistance = curDistance - 1;
            }
            curSquare = start;
            curDistance = maxDistance;
            // West direction Squares
            while (curSquare.getWestBorder() != Border.WALL && curDistance > 0) {
                validDestinations.add(curSquare.getWest());
                curSquare = curSquare.getWest();
                curDistance = curDistance - 1;
            }

            return validDestinations;
        } else {
            // Get all squares in the radius defined by maxDistance
            validDestinations.add(start);
            radiusValidDestinations(start, maxDistance, validDestinations);
            validDestinations.remove(start);
            validDestinations.removeIf(Objects::isNull);
            return validDestinations;

        }


    }

    /**
     * Helper method for {@link GameBoard#getValidDestinations(Square, int, boolean)}.
     * This method uses recursion to find the valid destinations.
     */
    private void radiusValidDestinations(Square start, int maxDistance, List<Square> alreadyVisitedSquares) {

        if (maxDistance == 0) return;

        if (start.getNorthBorder() != Border.WALL && maxDistance > 0) {
            if (!alreadyVisitedSquares.contains(start.getNorth())) {
                alreadyVisitedSquares.add(start.getNorth());
                radiusValidDestinations(start.getNorth(), maxDistance - 1, alreadyVisitedSquares);
            }
        }
        if (start.getEastBorder() != Border.WALL && maxDistance > 0) {
            if (!alreadyVisitedSquares.contains(start.getEast())) {
                alreadyVisitedSquares.add(start.getEast());
                radiusValidDestinations(start.getEast(), maxDistance - 1, alreadyVisitedSquares);
            }
        }
        if (start.getSouthBorder() != Border.WALL && maxDistance > 0) {
            if (!alreadyVisitedSquares.contains(start.getSouth())) {
                alreadyVisitedSquares.add(start.getSouth());
                radiusValidDestinations(start.getSouth(), maxDistance - 1, alreadyVisitedSquares);
            }
        }
        if (start.getWestBorder() != Border.WALL && maxDistance > 0) {
            if (!alreadyVisitedSquares.contains(start.getWest())) {
                alreadyVisitedSquares.add(start.getWest());
                radiusValidDestinations(start.getWest(), maxDistance - 1, alreadyVisitedSquares);
            }
        }
    }

    /**
     * Used to insert tokens in the Track.
     *
     * @param tokens the player who did the kill. If there's an overkill, {@code tokens} must contain two times the same player.
     *               In case of 3+ kills, they are counted as 2. If list {@code tokens} is empty, no kill is counted.
     */
    public void addTokensAndRemoveSkull(List<Player> tokens) {
        track.removeSkull();
        track.addTokens(tokens);
    }

    /**
     * Returns the killshot track.
     *
     * @return the killshot track
     */
    public List<List<Player>> getKillshotTrack() {
        return track.getTokens();
    }

    /**
     * Checks whether Final Frenzy is triggered.
     *
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
     *
     * @param usedCard the discarded Ammo Card.
     */
    public void putAmmoCard(AmmoCard usedCard) {
        discardedAmmos.add(usedCard);

    }

    /**
     * Inserts an Powerup Card in the {@code discardedPowerups} set.
     *
     * @param usedCard the discarded Powerup Card.
     */
    public void putPowerupCard(PowerupCard usedCard) {
        discardedPowerups.add(usedCard);
    }

    /**
     * When an Ammo Card is grabbed from a Square, it is replaced  at the end of the turn.
     * This method stores the squares where there is an Ammo Card to be replaced.
     *
     * @param toBeReplaced the square where there is an Ammo Card to be replaced.
     *                     The method {@link GameBoard#replaceAll} actually takes care of replacing the Ammo Card at the end of turn.
     */
    public void addSquare(Square toBeReplaced) {
        squareNewAmmoCard.add(toBeReplaced);
    }

    /**
     * When a Weapon Card is grabbed from a Spawn Square, it is replaced at the end of the turn.
     * This method stores the Spawn Squares where there is a Weapon Card to be replaced.
     *
     * @param toBeReplaced SpawnSquare where there's a Weapon to be replaced.
     *                     The method {@link GameBoard#replaceAll} actually takes care of replacing the Weapon Card at the end of turn.
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
        for (Square s : squareNewAmmoCard)
            if (s.peekAmmoCard() == null)
                s.setAmmoCard(ammoDeck.drawCard());
        //Replacing Weapon Card
        for (SpawnSquare s : squareNewWeaponCard)
            if (weaponDeck.cardsLeft() != 0) {
                for (WeaponCard i : s.getMarket().getCards()) {
                    try {
                        if (i == null)
                            s.getMarket().addCard(weaponDeck.drawCard());
                    } catch (AgainstRulesException e) {
                    }
                }
            }
        squareNewAmmoCard.clear();
        squareNewWeaponCard.clear();
    }

    /**
     * Gets discarded powerups.
     *
     * @return discarded powerups
     */
    public List<PowerupCard> getDiscardedPowerups() {
        return discardedPowerups;
    }

    /**
     * Gets discarded ammos.
     *
     * @return discarded ammos
     */
    public List<AmmoCard> getDiscardedAmmos() {
        return discardedAmmos;
    }

    /**
     * Gets the minimum distance between two squares, ignoring the wall according to the {@code ignoreWalls} parameter.
     *
     * @param ignoreWalls boolean indicating if the path should ignore walls or not
     * @return an int representing the minimum distance between two squares.
     */
    public int minimumDistance(Square start, Square end, Boolean ignoreWalls) {  //TODO risolvere il loop
        if (start.equals(end) || start == null || end == null)
            return 0;

        int dist = 0;
        List<Square> objects = new ArrayList<>();
        List<Square> neighbours = new ArrayList<>();

        objects.add(start);
        while (!objects.contains(end)) {
            dist++;
            for (Square s : objects) {
                neighbours.addAll(getNeighbours(s, ignoreWalls));
            }
            objects.addAll(neighbours);
            neighbours.clear();
        }
        return dist;
    }

    /**
     * Gets the square  with the minimum distance in the given set of squares.
     *
     * @param squares set of squares
     * @param map     distance for every square
     * @return
     */
    private Square min(Set<Square> squares, Map<Square, Integer> map) {
        int min = 999;
        Square minSquare = null;
        for (Square s : squares)
            if (map.get(s) <= min) {
                min = map.get(s);
                minSquare = s;
            }
        return minSquare;
    }

    /**
     * Gets the walkable neighbours of a square.
     *
     * @param s square
     * @return
     */
    private List<Square> getNeighbours(Square s, Boolean ignoreWalls) {
        if (s == null) return new ArrayList<>();

        List<Square> squares = new ArrayList<>();

        if (ignoreWalls || s.getNorthBorder() != Border.WALL)
            squares.add(s.getNorth());
        if (ignoreWalls || s.getEastBorder() != Border.WALL)
            squares.add(s.getEast());
        if (ignoreWalls || s.getSouthBorder() != Border.WALL)
            squares.add(s.getSouth());
        if (ignoreWalls || s.getWestBorder() != Border.WALL)
            squares.add(s.getWest());

        return squares.stream().filter(e -> e != null).collect(Collectors.toList());
    }

    /**
     * Gets all the players in the given square.
     *
     * @param square
     * @return a set presenting the players in the given square
     */
    public Collection<Damageable> getPlayerInSquare(Square square, Collection<? extends Damageable> players) {
        return players.stream().filter(p -> p.getPosition()!= null && p.getPosition().equals(square)).collect(Collectors.toSet());
    }

    /**
     * Gets all the square composing the board.
     *
     * @return all squares composing the board
     */
    public Set<Square> getAllSquares() {
        return configuration.stream()
                .map(room -> room.getAllSquares()).
                        flatMap(List::stream)
                .collect(Collectors.toSet());
    }

}