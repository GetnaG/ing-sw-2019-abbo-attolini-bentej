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
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

/**
 * Descrizione.
 * <p>
 * Dettagli.
 *
 * @author Abbo Giulio A.
 */
public class UpdateBuilder {
    private Integer configurationId;
    private List<AmmoCard> ammoCards;
    private List<WeaponCard> weaponsOnBoard;
    private Boolean weaponDrawable;
    private List<? extends List<Player>> killshotTrack;
    private Boolean isMatchFrenzy;
    private List<String> players;
    private Map<Player, Square> playerPosition;
    private Map<Player, List<AmmoCube>> activeCubes;
    private Map<Player, Boolean> isPlayerFrenzy;
    private Map<Player, Integer> skullsOnBoard;
    private Map<Player, List<Player>> playerDamage;
    private Map<Player, List<Player>> playerMarks;
    private Map<Player, Boolean> isConnected;
    private Map<Player, List<WeaponCard>> loadedWeapons;
    private Map<Player, List<WeaponCard>> unloadedWeapon;
    private Map<Player, List<PowerupCard>> powerupsInHand;
    private Integer timer;
    private List<? extends Player> winners;
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

        weaponsOnBoard.addAll(board.findSpawn(AmmoCube.BLUE).getMarket().getCards());
        weaponsOnBoard.addAll(board.findSpawn(AmmoCube.RED).getMarket().getCards());
        weaponsOnBoard.addAll(board.findSpawn(AmmoCube.YELLOW).getMarket().getCards());
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

    public Update[] build() {
        List<Update> updates = new ArrayList<>();
        singleAdd(updates, configurationId, Update.UpdateType.CONFIGURATION_ID,
                o -> Integer.toString(o));
        listAdd(updates, ammoCards, Update.UpdateType.AMMO_CARD_ARRAY,
                o -> {
                    try {return o.getId();}
                    catch (NullPointerException e) {return /*"notSet"*/ "market";}
                });
        listAdd(updates, weaponsOnBoard, Update.UpdateType.WEAPON_CARD_ARRAY,
                o -> {
                    try {return o.getId();}
                    catch (NullPointerException e) {return "notSet";}
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
                    try {return Collections.singletonList(o.getID());}
                    catch (NullPointerException e) {return Collections.singletonList("-1");}
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

    private <T> void singleAdd(List<Update> updates, T field,
                               Update.UpdateType type,
                               Function<T, String> nameMapper) {
        if (field == null)
            return;
        updates.add(new Update(type, nameMapper.apply(field)));
    }
}
