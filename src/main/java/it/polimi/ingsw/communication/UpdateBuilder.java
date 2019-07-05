package it.polimi.ingsw.communication;

import it.polimi.ingsw.communication.protocol.Update;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class creates an {@link Update} with the info provided.
 *
 * @author Abbo Giulio A.
 * @see Update
 */
public class UpdateBuilder {
    /**
     * Represents the chosen configuration
     * Update is based on the number of players
     */
    private Integer configurationId;
    /**
     * Lists the ammo cards on the board
     * It's updated when an actions modifies it, e.g. A player grabs an ammo card
     * A null elements states either that the ammo card has picked up
     * or it is located on a SpawnSquare, witch contains a Weapon Market
     */
    private List<AmmoCard> ammoCards;
    /**
     * Lists the weapon cards on the board
     * It's updated when an actions modifies it, e.g. A player grabs a weapon card from the market
     * A null elements states either that the weapon card has picked up
     * or it is not yet been set
     */
    private List<WeaponCard> weaponsOnBoard;
    /**
     * Whether the weapons can be drawn.
     */
    private Boolean weaponDrawable;
    /**
     * The killshot thack on the board.
     */
    private List<? extends List<Player>> killshotTrack;
    /**
     * It's set to true upon the last kill, when the match enters the frenzy mode
     */
    private Boolean isMatchFrenzy;
    /**
     * Lists the players the are playing in the game
     */
    private List<String> players;
    /**
     * Associates a square to its player, determining his position on the board
     */
    private Map<Player, Square> playerPosition;
    /**
     * Associates the list of usable ammo cubes to its player
     */
    private Map<Player, List<AmmoCube>> activeCubes;
    /**
     * Contains the list of players that are currently in frenzy mode
     */
    private Map<Player, Boolean> isPlayerFrenzy;
    /**
     * Associates the number of skulls on a player's board with the player that holds them
     */
    private Map<Player, Integer> skullsOnBoard;
    /**
     * Associates the list of players that damaged the player with player himself
     */
    private Map<Player, List<Player>> playerDamage;
    /**
     * Associates the list of players that gave marks to the player with player himself
     */
    private Map<Player, List<Player>> playerMarks;
    /**
     * Contains the list of players that are currently connected
     */
    private Map<Player, Boolean> isConnected;
    /**
     * Associates the list of loaded weapons held by a player with player himself
     */
    private Map<Player, List<WeaponCard>> loadedWeapons;
    /**
     * Associates the list of unloaded weapons held by a player with player himself
     */
    private Map<Player, List<WeaponCard>> unloadedWeapon;
    /**
     * Associates the list of powerups held by a player with player himself
     */
    private Map<Player, List<PowerupCard>> powerupsInHand;
    /**
     * Represents the current timer as a countdown
     */
    private Integer timer;
    /**
     * Represents the winners of the game, order by highest score
     */
    private List<? extends Player> winners;
    /**
     * Represents the current player
     */
    private Player current;

    public UpdateBuilder setConfigurationId(Integer configurationId) {
        this.configurationId = configurationId;
        return this;
    }

    public UpdateBuilder setAmmoCards(GameBoard board) {
        ammoCards = new ArrayList<>(Collections.nCopies(12, null));
        board.getAllSquares().forEach(s ->
                ammoCards.set(Integer.parseInt(s.getID()), s.peekAmmoCard()));
        return this;
    }

    public UpdateBuilder setWeaponsOnBoard(GameBoard board) {
        weaponsOnBoard = new ArrayList<>(Collections.nCopies(9, null));

        weaponsOnBoard.addAll(0, board.findSpawn(AmmoCube.BLUE).getMarket().getCards());
        weaponsOnBoard.addAll(3, board.findSpawn(AmmoCube.RED).getMarket().getCards());
        weaponsOnBoard.addAll(6, board.findSpawn(AmmoCube.YELLOW).getMarket().getCards());
        return this;
    }

    public UpdateBuilder setWeaponDrawable(Boolean weaponDrawable) {
        this.weaponDrawable = weaponDrawable;
        return this;
    }

    public UpdateBuilder setKillshotTrack(List<? extends List<Player>> killshotTrack) {
        this.killshotTrack = killshotTrack;
        return this;
    }

    public UpdateBuilder setMatchFrenzy(Boolean matchFrenzy) {
        isMatchFrenzy = matchFrenzy;
        return this;
    }

    public UpdateBuilder setPlayers(List<? extends Player> players) {
        this.players = players.stream().map(Player::getName).collect(Collectors.toList());
        return this;
    }

    public UpdateBuilder setPlayersNames(List<String> players) {
        this.players = players;
        return this;
    }

    public UpdateBuilder setPlayerPosition(Player player, Square position) {
        if (playerPosition == null)
            playerPosition = new HashMap<>();
        playerPosition.put(player, position);
        return this;
    }

    public UpdateBuilder setActiveCubes(Player player, List<AmmoCube> cubes) {
        if (activeCubes == null)
            activeCubes = new HashMap<>();
        activeCubes.put(player, cubes);
        return this;
    }

    public UpdateBuilder setIsPlayerFrenzy(Player player, boolean frenzy) {
        if (isPlayerFrenzy == null)
            isPlayerFrenzy = new HashMap<>();
        isPlayerFrenzy.put(player, frenzy);
        return this;
    }

    public UpdateBuilder setSkullsOnBoard(Player player, int skulls) {
        if (skullsOnBoard == null)
            skullsOnBoard = new HashMap<>();
        skullsOnBoard.put(player, skulls);
        return this;
    }

    public UpdateBuilder setPlayerDamage(Player player, List<Player> damage) {
        if (playerDamage == null)
            playerDamage = new HashMap<>();
        playerDamage.put(player, damage);
        return this;
    }

    public UpdateBuilder setIsConnected(Player player, boolean connected) {
        if (isConnected == null)
            isConnected = new HashMap<>();
        isConnected.put(player, connected);
        return this;
    }

    public UpdateBuilder setLoadedWeapons(Player player,
                                          List<WeaponCard> loaded) {
        if (loadedWeapons == null)
            loadedWeapons = new HashMap<>();
        loadedWeapons.put(player, loaded);
        return this;
    }

    public UpdateBuilder setUnloadedWeapon(Player player,
                                           List<WeaponCard> unloaded) {
        if (unloadedWeapon == null)
            unloadedWeapon = new HashMap<>();
        unloadedWeapon.put(player, unloaded);
        return this;
    }

    public UpdateBuilder setPowerupsInHand(Player player,
                                           List<PowerupCard> powerup) {
        if (powerupsInHand == null)
            powerupsInHand = new HashMap<>();
        powerupsInHand.put(player, powerup);
        return this;
    }

    public UpdateBuilder setTimer(Integer timer) {
        this.timer = timer;
        return this;
    }

    public UpdateBuilder setWinners(List<? extends Player> winners) {
        this.winners = winners;
        return this;
    }

    public UpdateBuilder setPlayerMarks(Player player, List<Player> marks) {
        if (playerMarks == null)
            playerMarks = new HashMap<>();
        playerMarks.put(player, marks);
        return this;
    }

    public UpdateBuilder setCurrent(Player current) {
        this.current = current;
        return this;
    }

    /**
     * Builds the updates with the info provided.
     *
     * @return an array with the updates created
     */
    public Update[] build() {
        List<Update> updates = new ArrayList<>();
        singleAdd(updates, configurationId, Update.UpdateType.CONFIGURATION_ID,
                o -> Integer.toString(o));
        listAdd(updates, ammoCards, Update.UpdateType.AMMO_CARD_ARRAY,
                o -> {
                    try {
                        return o.getId();
                    } catch (NullPointerException e) {
                        return "notSet";
                    }
                });
        listAdd(updates, weaponsOnBoard, Update.UpdateType.WEAPON_CARD_ARRAY,
                o -> {
                    try {
                        return o.getId();
                    } catch (NullPointerException e) {
                        return "notSet";
                    }
                });
        singleAdd(updates, weaponDrawable, Update.UpdateType.IS_WEAPON_DECK_DRAWABLE,
                o -> Boolean.toString(o));
        listAdd(updates, killshotTrack, Update.UpdateType.KILLSHOT_TRACK,
                o -> o.stream().map(Player::getName).reduce((acc, e) -> acc + Update.SEPARATOR + e).orElse("ERROR"));
        singleAdd(updates, isMatchFrenzy, Update.UpdateType.IS_ACTION_TILE_FRENZY,
                o -> Boolean.toString(o));
        listAdd(updates, players, Update.UpdateType.TURN_POSITION,
                o -> o);
        mapAdd(updates, playerPosition, Update.UpdateType.SQUARE_POSITION,
                o -> {
                    try {
                        return Collections.singletonList(o.getID());
                    } catch (NullPointerException e) {
                        return Collections.singletonList("-1");
                    }
                });
        mapAdd(updates, activeCubes, Update.UpdateType.AMMO_CUBE_ARRAY,
                o -> Arrays.asList(Integer.toString(AmmoCube.countBlue(o)),
                        Integer.toString(AmmoCube.countRed(o)),
                        Integer.toString(AmmoCube.countYellow(o))));
        mapAdd(updates, isPlayerFrenzy, Update.UpdateType.IS_PLAYER_BOARD_FRENZY,
                o -> Collections.singletonList(Boolean.toString(o)));
        mapAdd(updates, skullsOnBoard, Update.UpdateType.SKULL_NUMBER,
                o -> Collections.singletonList(Integer.toString(o)));
        mapAdd(updates, playerDamage, Update.UpdateType.DAMAGE_ARRAY,
                o -> o.stream().map(Player::getName).collect(Collectors.toList()));
        mapAdd(updates, playerMarks, Update.UpdateType.MARKS_ARRAY,
                o -> o.stream().map(Player::getName).collect(Collectors.toList()));
        mapAdd(updates, loadedWeapons, Update.UpdateType.LOADED_WEAPONS,
                o -> o.stream().map(WeaponCard::getId).collect(Collectors.toList()));
        mapAdd(updates, unloadedWeapon, Update.UpdateType.UNLOADED_WEAPON,
                o -> o.stream().map(WeaponCard::getId).collect(Collectors.toList()));
        mapAdd(updates, powerupsInHand, Update.UpdateType.POWERUPS,
                o -> o.stream().map(PowerupCard::getId).collect(Collectors.toList()));
        mapAdd(updates, isConnected, Update.UpdateType.CONNECTED_PLAYERS,
                o -> Collections.singletonList(Boolean.toString(o)));
        singleAdd(updates, timer, Update.UpdateType.HALL_TIMER,
                o -> Integer.toString(o));
        listAdd(updates, winners, Update.UpdateType.GAME_OVER,
                Player::getName);
        singleAdd(updates, current, Update.UpdateType.CURRENT_PLAYER,
                Player::getName);
        return updates.toArray(new Update[]{});
    }

    private <T> void listAdd(List<? super Update> updates, List<T> field,
                             Update.UpdateType type,
                             Function<? super T, String> nameMapper) {
        if (field == null)
            return;
        updates.add(new Update(type, field.stream().map(nameMapper).collect(Collectors.toList())));
    }

    private <T> void mapAdd(List<? super Update> updates, Map<? extends Player, T> field,
                            Update.UpdateType type,
                            Function<? super T, ? extends List<String>> nameMapper) {
        if (field == null)
            return;
        field.forEach((player, t) -> updates.add(new Update(type, nameMapper.apply(t), player.getName())));
    }

    private <T> void singleAdd(List<? super Update> updates, T field,
                               Update.UpdateType type,
                               Function<T, String> nameMapper) {
        if (field == null)
            return;
        updates.add(new Update(type, nameMapper.apply(field)));
    }
}
