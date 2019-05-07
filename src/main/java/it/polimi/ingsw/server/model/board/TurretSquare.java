package it.polimi.ingsw.server.model.board;

/**
 *
 */
public class TurretSquare extends Square {

    private Turret turret;

    /**
     * Default constructor: turret mode non active <-> turret = null
     */
    public TurretSquare(Turret t) {
        super();
        turret = t;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Turret getTurret(){
        return this.turret;
    }
}