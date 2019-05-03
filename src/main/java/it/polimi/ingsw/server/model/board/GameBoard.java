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
     * Contains all the squares where a new Ammo Card is needed.
     */
    List<Square> squareNewAmmoCard;
    /**
     * Contains all the squares where a new Weapon Card is needed.
     */
    List<SpawnSquare> squareNewWeaponCard;
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

    }


    public List<Room> getConfiguration() {
        return configuration;
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
     *
     * @return random Weapon Card from the Weapon Deck.
     */
    public WeaponCard getWeaponCard() throws AgainstRulesException {
        return weaponDeck.drawCard();
    }

    /**
     * Finds the {@code Square} which contains the {@code Spawn} of the given {@code Color}
     *
     * @param color the color of the spawn to find
     * @return a {@code Square} which contains the {@code Spawn} of the given {@code Color}
     */
    public Square findSpawn(AmmoCube color) {
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
            radiusValidDestinations(start, maxDistance, validDestinations);
            validDestinations.remove(start);

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
            if (alreadyVisitedSquares.contains(start.getNorth()))
                return;
            alreadyVisitedSquares.add(start.getNorth());
            radiusValidDestinations(start.getNorth(), maxDistance - 1, alreadyVisitedSquares);
        }
        if (start.getEastBorder() != Border.WALL && maxDistance > 0) {
            if (alreadyVisitedSquares.contains(start.getEast()))
                return;
            alreadyVisitedSquares.add(start.getEast());
            radiusValidDestinations(start.getEast(), maxDistance - 1, alreadyVisitedSquares);
        }
        if (start.getSouthBorder() != Border.WALL && maxDistance > 0) {
            if (alreadyVisitedSquares.contains(start.getSouth()))
                return;
            alreadyVisitedSquares.add(start.getSouth());
            radiusValidDestinations(start.getSouth(), maxDistance - 1, alreadyVisitedSquares);
        }
        if (start.getWestBorder() != Border.WALL && maxDistance > 0) {
            if (alreadyVisitedSquares.contains(start.getWest()))
                return;
            alreadyVisitedSquares.add(start.getWest());
            radiusValidDestinations(start.getWest(), maxDistance - 1, alreadyVisitedSquares);
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
    public void addTurretSquare(TurretSquare toBeReplaced) {
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
            s.setGrabbable(ammoDeck.drawCard());
        //Replacing Weapon Card
        for (SpawnSquare s : squareNewWeaponCard)
            if (weaponDeck.cardsLeft() != 0) {
                for (WeaponCard i : s.getMarket().getCards()) {   //drawWeaponCard solo finchè ci sono null, controllo in testa
                    try {
                        if (i.equals(null))
                            s.getMarket().addCard(weaponDeck.drawCard());
                    } catch (AgainstRulesException e) {
                    }
                }
            }

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
     * Gets the minimum distance between two squares, ignoring the wall according to the {@code wallSensitive} parameter.
     *
     * @param wallSensitive boolean indicating if the path should ignore walls or not
     * @return an int representing the minimum distance between two squares.
     */
    public int minimumDistance(Square start, Square end, Boolean wallSensitive) {
        if (start.equals(end))
            return 0;

        Set<Square> Q = getAllSquares();
        Map<Square, Integer> dist = new HashMap<>();
        Map<Square, Square> prev = new HashMap<>();

        Q.forEach(x -> {
            dist.put(x, 999);
            prev.put(x, null);
        });
        dist.put(start, 0);
        Square u = start;
        while (!Q.isEmpty()) {
            u = min(Q, dist);
            Q.remove(u);
            List<Square> neighbours = getNeighbours(u, wallSensitive);
            for (Square v : neighbours) {
                int distance = dist.get(u) + 1;
                if (distance < dist.get(v))
                    dist.replace(v, distance);
            }
        }
        return dist.get(end);
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
            if (map.get(s) < min) {
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
    private List<Square> getNeighbours(Square s, Boolean wallSensitive) {
        List<Square> squares = new ArrayList<>();

        if (s.getNorthBorder() != Border.WALL || !wallSensitive)
            squares.add(s.getNorth());
        if (s.getEastBorder() != Border.WALL || !wallSensitive)
            squares.add(s.getEast());
        if (s.getSouthBorder() != Border.WALL || !wallSensitive)
            squares.add(s.getSouth());
        if (s.getWestBorder() != Border.WALL || !wallSensitive)
            squares.add(s.getWest());

        return squares;
    }


    /**
     * Gets all the players in the given square.
     *
     * @param square
     * @return a set presenting the players in the given square
     */
    public Collection<? extends Damageable> getPlayerInSquare(Square square, Collection<? extends Damageable> players) {
        return players.stream().filter(p -> p.getPosition().equals(square)).collect(Collectors.toSet());
    }

    /**
     * Gets all the square composing the board.
     *
     * @return all squares composing the board
     */
    public Set<Square> getAllSquares() {
        return configuration.stream()
                .map(room -> room.getSquares()).
                        flatMap(List::stream)
                .collect(Collectors.toSet());
    }
}