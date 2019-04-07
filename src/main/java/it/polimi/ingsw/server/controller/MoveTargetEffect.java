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

    public boolean isOnlyVisibleDestination() {
        return onlyVisibleDestination;
    }

    public void setOnlyVisibleDestination(boolean onlyVisibleDestination) {
        this.onlyVisibleDestination = onlyVisibleDestination;
    }

    public int getMaximumDistance() {
        return maximumDistance;
    }

    public void setMaximumDistance(int maximumDistance) {
        this.maximumDistance = maximumDistance;
    }

    public boolean isOnlySameDirection() {
        return onlySameDirection;
    }

    public void setOnlySameDirection(boolean onlySameDirection) {
        this.onlySameDirection = onlySameDirection;
    }

    public boolean isNotFromShooterSquare() {
        return notFromShooterSquare;
    }

    public void setNotFromShooterSquare(boolean notFromShooterSquare) {
        this.notFromShooterSquare = notFromShooterSquare;
    }

    public boolean isOnlyFromVisibleSquare() {
        return onlyFromVisibleSquare;
    }

    public void setOnlyFromVisibleSquare(boolean onlyFromVisibleSquare) {
        this.onlyFromVisibleSquare = onlyFromVisibleSquare;
    }

    public boolean isNotToShooterSquare() {
        return notToShooterSquare;
    }

    public void setNotToShooterSquare(boolean notToShooterSquare) {
        this.notToShooterSquare = notToShooterSquare;
    }

    public boolean isOnlyFromShooterSquare() {
        return onlyFromShooterSquare;
    }

    public void setOnlyFromShooterSquare(boolean onlyFromShooterSquare) {
        this.onlyFromShooterSquare = onlyFromShooterSquare;
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