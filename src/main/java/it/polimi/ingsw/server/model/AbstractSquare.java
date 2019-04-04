
import java.util.*;

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
    private void northType;

    /**
     * 
     */
    private void southType;

    /**
     * 
     */
    private void eastType;

    /**
     * 
     */
    private void westType;


    /**
     * 
     */
    private ReplaceListener replacer;

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