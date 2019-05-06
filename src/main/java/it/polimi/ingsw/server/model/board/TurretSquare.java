package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;

/**
 *
 */
public class TurretSquare extends Square {


    private AmmoCard ammoCard;
    private Turret turret;  //-> null by default , set active in turret mode

    /**
     * Default constructor
     */
    public TurretSquare(AmmoCard a, Turret t) {
        super();
        ammoCard = a;
        turret = t; //->turret mode non active <-> turret = null
    }


    public void setAmmoCard(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }

    //returns the reference and calls replacer
    public AmmoCard removeAmmoCard() {
        replacer.addTurretSquare(this);   // -- is implemented by the replacer
        AmmoCard tmp = ammoCard;
        ammoCard = null;
        return tmp;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public AmmoCard getAmmoCard(){
        return null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Turret getTurret(){
        return this.turret;
    }
}