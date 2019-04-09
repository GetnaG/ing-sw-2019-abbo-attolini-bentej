package it.polimi.ingsw.server.controller.effects;

/**
 * 
 */
public class TargetSquareEffect extends TargetEffect {

    /**
     * Default constructor
     */
    public TargetSquareEffect() {
    }

    /**
     * 
     */
    private int squareDamageQty;

    /**
     * 
     */
    private int squareMarksQty;

    public int getSquareDamageQty() {
        return squareDamageQty;
    }

    public void setSquareDamageQty(int squareDamageQty) {
        this.squareDamageQty = squareDamageQty;
    }

    public int getSquareMarksQty() {
        return squareMarksQty;
    }

    public void setSquareMarksQty(int squareMarksQty) {
        this.squareMarksQty = squareMarksQty;
    }
}