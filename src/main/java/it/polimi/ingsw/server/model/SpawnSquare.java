package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.controller.Spawn;

/**
 * 
 */
public class SpawnSquare extends AbstractSquare {



    private WeaponMarket market;
    private Spawn spawn;

    /**
     * Default constructor
     */
    public SpawnSquare() {
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

}