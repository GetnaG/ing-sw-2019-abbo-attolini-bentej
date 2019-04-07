package it.polimi.ingsw.server.controller;
/**
 * 
 */
public class MultipleSquareEffect extends SquareEffect {

    /**
     * Default constructor
     */
    public MultipleSquareEffect() {
    }

    /**
     * 
     */
    private int chainDamageQty;

    /**
     * 
     */
    private boolean onlySameDirection;

    /**
     * 
     */
    private int maxConsecutiveSquares;

    /**
     * 
     */
    private int maxDifferentRoom;

    public int getChainDamageQty() {
        return chainDamageQty;
    }

    public void setChainDamageQty(int chainDamageQty) {
        this.chainDamageQty = chainDamageQty;
    }

    public boolean isOnlySameDirection() {
        return onlySameDirection;
    }

    public void setOnlySameDirection(boolean onlySameDirection) {
        this.onlySameDirection = onlySameDirection;
    }

    public int getMaxConsecutiveSquares() {
        return maxConsecutiveSquares;
    }

    public void setMaxConsecutiveSquares(int maxConsecutiveSquares) {
        this.maxConsecutiveSquares = maxConsecutiveSquares;
    }

    public int getMaxDifferentRoom() {
        return maxDifferentRoom;
    }

    public void setMaxDifferentRoom(int maxDifferentRoom) {
        this.maxDifferentRoom = maxDifferentRoom;
    }
}