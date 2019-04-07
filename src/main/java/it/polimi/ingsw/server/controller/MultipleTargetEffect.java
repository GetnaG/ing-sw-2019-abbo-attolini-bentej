package it.polimi.ingsw.server.controller;
/**
 * 
 */
public class MultipleTargetEffect extends TargetEffect {

    /**
     * Default constructor
     */
    public MultipleTargetEffect() {
    }

    /**
     * 
     */
    private boolean sameDirection;

    public boolean isSameDirection() {
        return sameDirection;
    }

    public void setSameDirection(boolean sameDirection) {
        this.sameDirection = sameDirection;
    }
}