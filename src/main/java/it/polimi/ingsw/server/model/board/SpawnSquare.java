package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;

/**
 * 
 */
public class SpawnSquare extends Square {

    private WeaponMarket market;
    private Spawn spawn;


    /* Default constructor of spawn squares that can*/

    public SpawnSquare(AmmoCube color, WeaponMarket w) { ///-------????
        super(constructorHelper(color));
        market = w;
        spawn = null;
    }
    private static Color constructorHelper(AmmoCube c){
        switch (c) {
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case RED:
                return Color.RED;
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
        market.pickWeaponFromList(weapon);
        super.replacer.addSpawnSquare(this);
    }

    /**
     * @return return the color of the room selected to spawn
     */
    public AmmoCube getSpawnColor() {
        Color c = super.getColor();
        if (c == Color.BLUE)
            return AmmoCube.BLUE;
        if (c == Color.RED)
            return AmmoCube.RED;
        if (c == Color.YELLOW)
            return AmmoCube.YELLOW;
        return AmmoCube.ANY;
    }

}