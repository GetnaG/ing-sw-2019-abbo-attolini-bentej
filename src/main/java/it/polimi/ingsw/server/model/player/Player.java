package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.communication.ToClientInterface;
import it.polimi.ingsw.server.controller.ScoreListener;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.serverlogic.SuspensionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player in the game.
 * <p>
 * A player has a nickname (that must be granted unique by the assigner), a
 * score (scoring happens through a {@link ScoreListener}: when a player
 * dies is added to it, and when scoring begins
 * {@link #scoreAndResetDamage()} is called on this) and a position.
 * The player who starts the game is flagged accordingly, on this will depend
 * the behaviour of some parts of the game.
 * <p>
 * Each player has his own communication interface {@link ToClientInterface},
 * this is because in a mach there can be players using different
 * communication methods.
 * <p>
 * Internally, this class delegates cubes and cards handling to an
 * {@link AmmoBox} and a {@link HandManager}. Damage is handled by a
 * {@link PlayerBoardInterface}
 * <p>
 * <i>Implementation note: this class contains many methods in order to not
 * expose attributes like its {@link AmmoBox}, {@link HandManager} and
 * {@link PlayerBoardInterface} to the outside user</i>
 *
 * @author Abbo Giulio A.
 * @see ScoreListener
 * @see ToClientInterface
 * @see PlayerBoardInterface
 * @see Damageable
 */
public class Player implements Damageable {
    /**
     * The name of this player.
     */
    private String nickname;

    /**
     * The total score of this player.
     */
    private int score;

    /**
     * Whether this is the player who started the game.
     */
    private boolean firstPlayer;

    /**
     * Info on the resources associated with this player.
     */
    private String figureRes;

    /**
     * Handles the communication with this player.
     */
    private ToClientInterface toClient;

    /**
     * Handles the cards fot this player.
     */
    private HandManager hand;

    /**
     * Handles the cubes for this player.
     */
    private AmmoBox ammoBox;

    /**
     * Handles damage and scoring for this player.
     */
    private PlayerBoardInterface playerBoard;

    /**
     * The position if this player.
     */
    private Square position;

    /**
     * The first adrenaline action available.
     */
    private Action adrenaline1;

    /**
     * The second adrenaline action available.
     */
    private Action adrenaline2;

    /**
     * Will be notified of the death of this player and will call a scoring
     * method when necessary.
     */
    private ScoreListener scorer;

    /**
     * Suspension listner is called when a player has to be suspended.
     */
    private SuspensionListener suspension;

    /**
     * Constructor for testing purposes.
     *
     * @param hand        the hand manager
     * @param ammoBox     the ammo box
     * @param playerBoard the player board
     * @param scorer      the score listener
     */
    Player(HandManager hand, AmmoBox ammoBox, PlayerBoardInterface playerBoard, ScoreListener scorer) {
        this.hand = hand;
        this.ammoBox = ammoBox;
        this.playerBoard = playerBoard;
        this.scorer = scorer;
    }

    /**
     * Instantiates a player with the given parameters.
     *
     * @param nickname    the name of this player
     * @param firstPlayer true if this is the first player
     * @param figureRes   info on the resources associated with this
     * @param toClient    communication interface with the player
     * @param playerBoard the board that will handle the damage
     * @param scorer      the interface that handles the scoring
     */
    public Player(String nickname, boolean firstPlayer, String figureRes,
                  ToClientInterface toClient,
                  PlayerBoardInterface playerBoard, ScoreListener scorer) {
        this.nickname = nickname;
        this.firstPlayer = firstPlayer;
        this.figureRes = figureRes;
        this.toClient = toClient;
        this.playerBoard = playerBoard;
        this.scorer = scorer;

        score = 0;
        hand = new HandManager();
        ammoBox = new AmmoBox();

        //FIXME To be specified, maybe add from json?
        adrenaline1 = new Action();
        adrenaline2 = new Action();
    }

    /**
     * Instantiates a player with the given parameters.
     *
     * @param nickname    the name of this player
     * @param firstPlayer true if this is the first player
     * @param figureRes   info on the resources associated with this
     * @param toClient    communication interface with the player
     * @param playerBoard the board that will handle the damage
     * @param scorer      the interface that handles the scoring
     */
    public Player(String nickname, boolean firstPlayer, String figureRes,
                  ToClientInterface toClient,
                  PlayerBoardInterface playerBoard, ScoreListener scorer, SuspensionListener suspension) {
        this.nickname = nickname;
        this.firstPlayer = firstPlayer;
        this.figureRes = figureRes;
        this.toClient = toClient;
        this.playerBoard = playerBoard;
        this.scorer = scorer;
        this.suspension = suspension;

        score = 0;
        hand = new HandManager();
        ammoBox = new AmmoBox();

        //FIXME To be specified, maybe add from json?
        adrenaline1 = new Action();
        adrenaline2 = new Action();
    }

    /**
     * Instantiates a player with the given parameters and a
     * {@linkplain NormalPlayerBoard}.
     *
     * @param nickname    the name of this player
     * @param firstPlayer true if this is the first player
     * @param figureRes   info on the resources associated with this
     * @param toClient    communication interface with the player
     * @param scorer      the interface that handles the scoring
     */
    public Player(String nickname, boolean firstPlayer, String figureRes,
                  ToClientInterface toClient,
                  ScoreListener scorer) {
        this(nickname, firstPlayer, figureRes, toClient,
                new NormalPlayerBoard(), scorer);
    }

    /**
     * This constructor should be only used for testing purposes.
     *
     * @param nickname the name of this player
     */
    public Player(String nickname) {
        this(nickname, false, null, null, null);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This also notifies the {@linkplain ScoreListener} if the player dies.
     *
     * @param shooters the players that dealt the damage
     * @throws NullPointerException if {@code shooters} is null
     */
    @Override
    public void giveDamage(List<Player> shooters) {
        playerBoard.addDamage(shooters);
        if (playerBoard.isDead())
            scorer.addKilled(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param shooters the players that dealt the marks
     * @throws NullPointerException if {@code shooters} is null
     */
    @Override
    public void giveMark(List<Player> shooters) {
        playerBoard.addMarks(shooters);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Square getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     *
     * @param newPosition the new position of this entity
     */
    @Override
    public void setPosition(Square newPosition) {
        position = newPosition;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This also adds a skull to the player.
     */
    @Override
    public void scoreAndResetDamage() {
        playerBoard.score();
        playerBoard.addSkull();
        playerBoard.resetDamage();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Player getKillshotPlayer() {
        return playerBoard.getKillshot();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Player getOverkillPlayer() {
        return playerBoard.getOverkill();
    }

    /**
     * {@inheritDoc}
     * Returns the nickname of this.
     *
     * @return {@inheritDoc}
     */
    @Override
    public String getName() {
        return nickname;
    }

    /**
     * Returns a list with the available adrenaline actions.
     *
     * @return the available adrenaline actions
     */
    public List<Action> getAdrenalineActions() {
        List<Action> actions = new ArrayList<>();
        if (playerBoard.isAdr1Unlocked())
            actions.add(adrenaline1);
        if (playerBoard.isAdr2Unlocked())
            actions.add(adrenaline2);
        return actions;
    }

    /**
     * Adds the given cubes to the player.
     * All excess cubes are discarded, all cubes must be of type {@code BLUE}
     * , {@code RED} or {@code YELLOW}; an {@code ANY} type can not be added.
     *
     * @param cubes a {@linkplain List} of the cubes to be added
     * @throws IllegalArgumentException if a {@code cube} is of type {@code ANY}
     */
    public void addAmmo(List<AmmoCube> cubes) {
        ammoBox.addAmmo(cubes);
    }

    /**
     * Adds the given {@linkplain PowerupCard} to the player, if possible.
     *
     * @param card the card to be added
     * @throws NullPointerException if {@code card} is null
     */
    public void addPowerup(PowerupCard card) {
        hand.addPowerup(card);
    }

    /**
     * Removes the specified card from the player.
     *
     * @param card the card to be removed
     * @throws NullPointerException     if {@code card} is null
     * @throws IllegalArgumentException if {@code card} was not in the hand
     *                                  in the first place
     */
    public void removePowerup(PowerupCard card) {
        hand.removePowerup(card);
    }

    /**
     * Returns all the powerups this player has.
     *
     * @return a {@linkplain List} with all the powerups available to this
     * player.
     */
    public List<PowerupCard> getAllPowerup() {
        return hand.getPowerups();
    }

    /**
     * Returns the number of {@linkplain PowerupCard}s this player has.
     *
     * @return the number of {@linkplain PowerupCard}s this player has
     */
    public int getNumOfPowerups() {
        return hand.getPowerups().size();
    }

    /**
     * Returns {@code true} if this player can afford to pay the {@code cost}.
     * The {@code cost} is a {@linkplain List} of {@linkplain AmmoCube}s
     * representing how much must be payed for example to use an effect or
     * buy a card.
     * When buying a weapon the first cube is free, in that case {@code
     * skipFirst} should be set as {@code true}.
     *
     * @param cost      the cubes to be payed
     * @param skipFirst indicates whether the first element of the list must
     *                  be taken into account
     * @return true if this player has enough cubes to pay the price
     */
    public boolean canAfford(List<AmmoCube> cost, boolean skipFirst) {
        if (cost.isEmpty())
            return true;
        return ammoBox.checkPrice((skipFirst) ?
                cost.subList(1, cost.size()) :
                cost);
    }

    /**
     * Checks if this player can afford to pay the {@code cost} using powerups.
     * This should be used after {@linkplain #canAfford(List, boolean)}.
     * This returns a {@linkplain List} containing all the
     * {@linkplain PowerupCard} usable to pay the price. If there are not (or
     * not enough) cards the returned list is empty.
     *
     * @param cost      the cubes to be payed
     * @param skipFirst indicates whether the first element of the list must
     *                  be taken into account
     * @return a list of usable powerups or an empty list if none are
     * available or there are not enough
     */
    public List<PowerupCard> canAffordWithPowerups(List<AmmoCube> cost,
                                                   boolean skipFirst) {
        return hand.getPowerupForPaying(ammoBox.getMissing((skipFirst) ?
                cost.subList(1, cost.size()) :
                cost));
    }

    /**
     * Adds the given card to this player and pays its price.
     * A cube of type {@code ANY} can not be payed, first it is necessary to
     * choose what color to use.
     * Uses the list {@code asCubes} to pay if this player has not enough cubes.
     * This first checks if this player can afford to pay the price.
     *
     * @param card    the card to be bought
     * @param asCubes the powerups to be used as cubes
     * @throws IllegalArgumentException if the player can not afford to buy
     *                                  the card
     */
    public void buy(WeaponCard card, List<PowerupCard> asCubes) {

        List<AmmoCube> cost = new ArrayList<>(card.getCost().subList(1,
                card.getCost().size()));
        if (canAfford(card.getCost(), true)) {
            ammoBox.pay(cost);
            hand.addWeaponCard(card);
        } else if (!canAffordWithPowerups(card.getCost(), true).isEmpty()) {
            for (PowerupCard asCube : asCubes) {
                hand.removePowerup(asCube);
                cost.remove(asCube.getCube());
            }
            ammoBox.pay(cost);
            hand.addWeaponCard(card);
        } else
            throw new IllegalArgumentException("Can not afford buying");
    }

    /**
     * Removes the specified card from this player.
     *
     * @param card the card to be removed
     * @throws NullPointerException     if {@code card} is null
     * @throws IllegalArgumentException if {@code card} was not in the hand
     *                                  in the first place
     */
    public void discard(WeaponCard card) {
        hand.removeWeapon(card);
    }

    /**
     * Unloads the specified weapon.
     * An unloaded weapon can not be used.
     *
     * @param weapon the weapon to be unloaded
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if the {@code weapon} was not in the
     *                                  hand or is already unloaded
     */
    public void unload(WeaponCard weapon) {
        hand.unload(weapon);
    }

    /**
     * Reloads the specified weapon.
     * A loaded weapon can be used to shoot.
     *
     * @param weapon the weapon to be reloaded
     * @throws NullPointerException     if {@code weapon} is null
     * @throws IllegalArgumentException if the {@code weapon} was not in the
     *                                  hand or is already loaded
     */
    public void reload(WeaponCard weapon) {
        hand.reload(weapon);
    }

    /**
     * Returns a list of all the loaded {@linkplain WeaponCard} this player has.
     *
     * @return a {@linkplain List} of the loaded {@linkplain WeaponCard}s
     */
    public List<WeaponCard> getLoadedWeapons() {
        return hand.getLoadedWeapons();
    }

    /**
     * Returns a list of all the unloaded {@linkplain WeaponCard} this player
     * has.
     *
     * @return a {@linkplain List} of the unloaded {@linkplain WeaponCard}s
     */
    public List<WeaponCard> getReloadableWeapons() {
        return hand.getUnloadedWeapons();
    }

    /**
     * Returns a list of all the {@linkplain WeaponCard} this player has.
     *
     * @return a {@linkplain List} of all the {@linkplain WeaponCard}s
     */
    public List<WeaponCard> getAllWeapons() {
        List<WeaponCard> allWeapons = hand.getLoadedWeapons();
        allWeapons.addAll(hand.getUnloadedWeapons());
        return allWeapons;
    }

    /**
     * Returns the number of {@linkplain WeaponCard}s this player has.
     *
     * @return the number of {@linkplain WeaponCard}s this player has
     */
    public int getNumOfWeapons() {
        return getAllWeapons().size();
    }

    /**
     * Adds the given score to this player.
     *
     * @param score the score to be added
     * @throws IllegalArgumentException if {@code score} is negative
     */
    public void addScore(int score) {
        if (score < 0)
            throw new IllegalArgumentException("The score must be positive");
        this.score += score;
    }

    /**
     * Returns this player's score.
     *
     * @return the score of this player
     */
    public int getScore() {
        return score;
    }

    /**
     * @deprecated use {@link Player#addScore(int)} instead
     */
    @Deprecated(forRemoval = true)
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns whether this is the first player to start the game.
     *
     * @return true if this is the first player
     */
    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    /**
     * Returns a reference to the resources associated with this player.
     *
     * @return a reference to the resources associated with this player
     */
    public String getFigureRes() {
        return figureRes;
    }

    /**
     * Returns a reference to the suspension listener associated with this player.
     *
     * @return a reference to the suspension listener associated with this player.
     */
    public SuspensionListener getSuspensionListener(){
        return suspension;
    }

    /**
     * Returns the {@linkplain ToClientInterface} used to communicate with
     * this player.
     *
     * @return the {@linkplain ToClientInterface} used to communicate with
     * this player
     */
    public ToClientInterface getToClient() {
        return toClient;
    }

    /**
     * Changes the playerBoard for this player.
     * Marks are kept if the given board accepts them.
     *
     * @param playerBoard the new board
     */
    public void setPlayerBoard(PlayerBoardInterface playerBoard) {
        List<Player> marks = this.playerBoard.getMarks();
        this.playerBoard = playerBoard;
        this.playerBoard.addMarks(marks);
    }

    /**
     * Gets the Board of the player.
     * @return Bord of the player.
     */
    public PlayerBoardInterface getPlayerBoard(){
        return playerBoard;
    }
}
