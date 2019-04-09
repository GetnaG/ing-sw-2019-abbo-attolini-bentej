package it.polimi.ingsw.server.controller.effects;

/**
 * 
 */
public class RoomEffect extends AbstractEffect {

    /**
     * Default constructor
     */
    public RoomEffect() {
    }

    /**
     * 
     */
    private boolean allowShooterRoom;

    /**
     * 
     */
    protected int distanceFromPrevious;

    public boolean isAllowShooterRoom() {
        return allowShooterRoom;
    }

    public void setAllowShooterRoom(boolean allowShooterRoom) {
        this.allowShooterRoom = allowShooterRoom;
    }

    public int getDistanceFromPrevious() {
        return distanceFromPrevious;
    }

    public void setDistanceFromPrevious(int distanceFromPrevious) {
        this.distanceFromPrevious = distanceFromPrevious;
    }
}