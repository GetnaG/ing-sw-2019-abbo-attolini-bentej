package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.AbstractEffect;

/**
 * 
 */
public class MoveTargetEffect extends AbstractEffect {

    /**
     * Default constructor
     */
    public MoveTargetEffect() {
    }

    /**
     * 
     */
    private boolean onlyVisibleDestination;

    /**
     * 
     */
    private int maximumDistance;

    /**
     * 
     */
    private boolean onlyFromShooterSquare;

    /**
     * 
     */
    private boolean onlySameDirection;

    /**
     * 
     */
    private boolean notFromShooterSquare;

    /**
     * 
     */
    private boolean onlyFromVisibleSquare;

    /**
     * 
     */
    private boolean notToShooterSquare;

}