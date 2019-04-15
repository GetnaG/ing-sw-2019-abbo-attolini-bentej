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
    public SpawnSquare(WeaponMarket w) {  //spe non c'è più la sottoclasse turretsquare ma turret diventa un attributo di square??
        market = w;
        spawn = null;
    }


    public WeaponMarket getMarket() {
        return market;
    }

    public WeaponCard pickWeapon(){
        WeaponCard w/* = new WeaponCard()*/;
        w = market.pickWeaponFromList();
        super.replacer.replaceDiscardedWeapons(this, market.getCards()); //not sure on the second argument
        super.replacer.replaceAll();
        return w;
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

    public AmmoCube getColor() {
        Color c = super.getRoom().getColorRes();
        if (c == Color.BLUE)
            return AmmoCube.BLUE;
        if (c == Color.RED)
            return AmmoCube.RED;
        if (c == Color.YELLOW)
            return AmmoCube.YELLOW;
        return AmmoCube.ANY; //no io metterei che dà errore
    }

}