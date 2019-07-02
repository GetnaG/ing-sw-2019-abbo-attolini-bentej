package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 
 */
public class SpawnSquare extends Square {

    private WeaponMarket market;
    private Spawn spawn;


    public SpawnSquare(AmmoCube color, WeaponMarket w) {
        super(constructorHelper(color));
        market = w;
        spawn = null;
    }
    private static SquareColor constructorHelper(AmmoCube c){
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

    public void setMarket(WeaponMarket market) {
        this.market = market;
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
    public void pickWeapon(WeaponCard weapon){
        if(market.isValidWeapon(weapon)) {
            market.pickWeaponFromList(weapon);
            replacer.addSpawnSquare(this);
        }
        else
            System.out.println("Warning: You are trying to remove a non-existing weapon!");
    }

    /**
     * @return return the color of the room selected to spawn
     */
    public AmmoCube getSpawnColor() {
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
    public void refresh(List<Room> rooms) {
        super.refresh(rooms);
        market = new WeaponMarket(new ArrayList<>(Arrays.asList(null, null, null)));
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