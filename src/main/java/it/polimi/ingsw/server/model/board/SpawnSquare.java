package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class defines a Spawn Square, where it is possible to spawn
 * at the beginning of the game and after every death.
 * <p>
 * Unlike normal Squares, this Square contains a Market where it
 * is possible to buy weapons, instead of grabbing ammo cards.
 */
public class SpawnSquare extends Square {

    /**
     * Represents the market of the Spawn Square
     * Its size limit is 3, and it is updated each time
     * a weapon is picked up by a Player
     */
    private WeaponMarket market;
    /**
     * Used in Domination mode
     */
    private Spawn spawn;


    public SpawnSquare(AmmoCube color, WeaponMarket w) {
        super(constructorHelper(color));
        market = w;
        spawn = null;
    }

    /**
     * Creates a spawn square that is a copy of the provided one.
     * The provided square needs to be refreshed.
     *
     * @param copyOf the square to copy
     */
    public SpawnSquare(SpawnSquare copyOf) {
        super(copyOf);
        market = null;
        spawn = null;
    }

    private static SquareColor constructorHelper(AmmoCube c) {
        switch (c) {
            case BLUE:
                return SquareColor.BLUE;
            case YELLOW:
                return SquareColor.YELLOW;
            case RED:
                return SquareColor.RED;
            case ANY:
                throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }


    public WeaponMarket getMarket() {
        return market;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public void setSpawn(Spawn spawn) {
        this.spawn = spawn;
    }


    /**
     * @param weapon is  the weapon chosen by the player
     */
    public void pickWeapon(WeaponCard weapon) {
        if (market.isValidWeapon(weapon)) {
            market.pickWeaponFromList(weapon);
            replacer.addSpawnSquare(this);
        } else
            throw new IllegalArgumentException("You are trying to remove a non-existing weapon!");
    }

    /**
     * @return return the color of the room selected to spawn
     */
    AmmoCube getSpawnColor() {
        SquareColor c = getSquareColor();
        if (c == SquareColor.BLUE)
            return AmmoCube.BLUE;
        if (c == SquareColor.RED)
            return AmmoCube.RED;
        if (c == SquareColor.YELLOW)
            return AmmoCube.YELLOW;
        return AmmoCube.ANY;
    }

    /**
     * After maps are loaded from json, a refresh is needed.
     */
    void refresh(List<? extends Room> rooms) {
        if (market == null)
            market = new WeaponMarket(new ArrayList<>(Arrays.asList(null, null, null)));
        setNorth(Square.getSquare(rooms, getIdNorth()));
        setEast(Square.getSquare(rooms, getIdEast()));
        setSouth(Square.getSquare(rooms, getIdSouth()));
        setWest(Square.getSquare(rooms, getIdWest()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpawnSquare)) return false;
        if (!super.equals(o)) return false;
        SpawnSquare that = (SpawnSquare) o;
        return Objects.equals(market, that.market) &&
                Objects.equals(spawn, that.spawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, spawn);
    }
}