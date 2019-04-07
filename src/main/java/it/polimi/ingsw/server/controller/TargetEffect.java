package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.AbstractEffect;

/**
 * 
 */
public class TargetEffect extends AbstractEffect {

    /**
     * Default constructor
     */
    public TargetEffect() {
    }

    /**
     * 
     */
    private boolean onlyVisibleTarget;

    /**
     * 
     */
    private boolean onlyInvisibleTarget;

    /**
     * 
     */
    private int minimumDistance;

    /**
     * 
     */
    private boolean onlyCardinalDirection;

    public boolean isOnlyVisibleTarget() {
        return onlyVisibleTarget;
    }

    public void setOnlyVisibleTarget(boolean onlyVisibleTarget) {
        this.onlyVisibleTarget = onlyVisibleTarget;
    }

    public boolean isOnlyInvisibleTarget() {
        return onlyInvisibleTarget;
    }

    public void setOnlyInvisibleTarget(boolean onlyInvisibleTarget) {
        this.onlyInvisibleTarget = onlyInvisibleTarget;
    }

    public int getMinimumDistance() {
        return minimumDistance;
    }

    public void setMinimumDistance(int minimumDistance) {
        this.minimumDistance = minimumDistance;
    }

    public boolean isOnlyCardinalDirection() {
        return onlyCardinalDirection;
    }

    public void setOnlyCardinalDirection(boolean onlyCardinalDirection) {
        this.onlyCardinalDirection = onlyCardinalDirection;
    }
}