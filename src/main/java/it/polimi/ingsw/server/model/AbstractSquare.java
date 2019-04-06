package it.polimi.ingsw.server.model;
import java.util.List;

/**
 *  defines the structure of a square
 */
public abstract class AbstractSquare {

    /**
     * Default constructor
     */
    public AbstractSquare() {
    }

    /**
     * 
     */
    private AbstractSquare north;

    /**
     * 
     */
    private AbstractSquare south;

    /**
     * 
     */
    private AbstractSquare east;

    /**
     * 
     */
    private AbstractSquare west;

    /**
     * 
     */
    public abstract void northType();

    /**
     * 
     */
    public abstract void southType();

    /**
     * 
     */
    public abstract void eastType();

    /**
     * 
     */
    public abstract void westType();


    /**
     * 
     */
    protected ReplaceListener replacer;

    /**
     * @param destination 
     * @return
     */
    public int distance(AbstractSquare destination) {
        // TODO implement here
        /*
            int dist
               destination.x
               destination.y
               distx = abs(destination.x - this.x)
               disty = abs(destination.y - this.y)
               dist = max(distx, disty)   -- when calculating distance to shoot
               dist = distx + disty   -- when calculating steps
               return dist


         */
        return 0;
    }

    /**
     * @return
     */
    public List<AbstractCard> getGrabbables() {
        // TODO implement here
        /*

         */
        return null;
    }

    /**
     * @param destination 
     * @return
     */
    public boolean checkVisible(AbstractSquare destination) {
        // TODO implement here
        /*
        check: dist = 1

         */
        return false;
    }

}