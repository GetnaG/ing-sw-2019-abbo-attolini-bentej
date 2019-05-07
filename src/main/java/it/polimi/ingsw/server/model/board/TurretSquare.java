package it.polimi.ingsw.server.model.board;

/**
 *
 */
public class TurretSquare extends Square {

    private Turret turret;

    /**
     * Default constructor: turret mode non active --> turret = null
     */
    public TurretSquare(Turret t) {
        super();
        turret = t;
    }

    /**
     * Gets the Turret in this Square or null if the game mode is not Turret Mode.
     *{@inheritDoc}
     * @return Turret in this Square
     */
    @Override
    public Turret getTurret(){
        return this.turret;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TurretSquare))
            return false;

        if(!(((Square) obj).equals(this)) || this.turret != ((TurretSquare) obj).turret)  ///////////super.eq???
            return  false;

        return true;
    }
}