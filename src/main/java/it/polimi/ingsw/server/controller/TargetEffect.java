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

}