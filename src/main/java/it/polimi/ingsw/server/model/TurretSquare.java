package it.polimi.ingsw.server.model;

/**
 * 
 */
public class TurretSquare extends AbstractSquare {


    private AmmoCard ammoCard;
    private Turret turret;  //-> null by default , set active in turret mode

    /**
     * Default constructor
     */
    public TurretSquare(AmmoCard a, Turret t) {
        ammoCard=a;
        turret=t; //->turret mode non active <-> turret = null
    }


    public void setAmmoCard(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }


    //getGrabbables -> replace listener
    @Override
    public AbstractCard getGrabbables() {
        replacer.addTurretSquare(this);
        return ammoCard;
    }




}