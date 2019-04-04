package it.polimi.ingsw.server.model;
import java.util.List;

/**
 * 
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
        return 0;
    }

    /**
     * @return
     */
    public List<AbstractCard> getGrabbables() {
        // TODO implement here
        return null;
    }

    /**
     * @param destination 
     * @return
     */
    public boolean checkVisible(AbstractSquare destination) {
        // TODO implement here
        return false;
    }

}