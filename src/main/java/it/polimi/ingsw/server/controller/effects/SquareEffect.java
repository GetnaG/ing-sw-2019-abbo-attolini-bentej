package it.polimi.ingsw.server.controller.effects;

/**
 * 
 */
public class SquareEffect extends AbstractEffect {

    /**
     * Default constructor
     */
    public SquareEffect() {
    }

    /**
     * 
     */
    private boolean onlyShooterSquare;

    /**
     * 
     */
    private int distance;

    /**
     * 
     */
    private int restrictTargetNumber;

    /**
     * 
     */
    private boolean onlyVisibleSquare;

    public boolean isOnlyShooterSquare() {
        return onlyShooterSquare;
    }

    public void setOnlyShooterSquare(boolean onlyShooterSquare) {
        this.onlyShooterSquare = onlyShooterSquare;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRestrictTargetNumber() {
        return restrictTargetNumber;
    }

    public void setRestrictTargetNumber(int restrictTargetNumber) {
        this.restrictTargetNumber = restrictTargetNumber;
    }

    public boolean isOnlyVisibleSquare() {
        return onlyVisibleSquare;
    }

    public void setOnlyVisibleSquare(boolean onlyVisibleSquare) {
        this.onlyVisibleSquare = onlyVisibleSquare;
    }
}