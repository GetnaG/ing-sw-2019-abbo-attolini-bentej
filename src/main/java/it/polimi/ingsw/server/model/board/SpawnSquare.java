package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;

/**
 * 
 */
public class SpawnSquare extends Square {

    private WeaponMarket market;
    private Spawn spawn;

    /**
     * Default constructor
     */
    public SpawnSquare(Color c, WeaponMarket w) {
        super(c);
        market = w;
        spawn = null;
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


    public WeaponCard pickWeapon(WeaponCard weapon){
        WeaponCard w = market.pickWeaponFromList(weapon);
        super.replacer.replaceDiscardedWeapons(this, market.getCards()); //not sure on the second argument
        return w;
    }

    public AmmoCube getSpawnColor() {
        Color c = super.getColor();
        if (c == Color.BLUE)
            return AmmoCube.BLUE;
        if (c == Color.RED)
            return AmmoCube.RED;
        if (c == Color.YELLOW)
            return AmmoCube.YELLOW;
        return AmmoCube.ANY; // this option should not even exist
    }

}