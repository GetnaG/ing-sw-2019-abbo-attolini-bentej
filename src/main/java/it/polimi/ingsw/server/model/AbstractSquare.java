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

    /*
    private String color;
    private int x ( 0 <= x <= 3)
    private int y ( 0 <= y <= 2)
     */

    /**
     * 
     */
    //private AbstractSquare north;
    //  -> private Border northBorder;   -- Border : { Wall, Door, Corridor} <- Set when creating the map

    /**
     * 
     */
   // private AbstractSquare south;

    /**
     * 
     */
   // private AbstractSquare east;

    /**
     * 
     */
    //private AbstractSquare west;

    /**
     * 
     */
    public abstract void northType();
    /*
        public abstract Border northType(AbstractSquare){
            return this.northBorder;
     */

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


    /*
    *
    * public String getColor(AbstractSquare sq){
    *   return sq.color;
    *   }
    *
     */

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
               dist = distx + disty
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
        if( this.color = destination.color )
            return 1;
        else{
                AbstractSquare adjacent

                if(this.northType != wall)
                 adjacent.x = this.x;   adjacent.y = this.y + 1;
                    adjacent.color = getColor(adjacent);
                   if( adjacent.color = destination.color ) return 1;

                 if(this.southType != wall)
                 adjacent.x = this.x    adjacent.y = this.y - 1
                   adjacent.color = getColor(adjacent);
                   if( adjacent.color = destination.color ) return 1

                 if(this.eastType != wall)
                 adjacent.y = this.y    adjacent.x = this.x + 1
                    adjacent.color = getColor(adjacent);
                   if( adjacent.color = destination.color ) return 1

                 if(this.westType != wall)
                 adjacent.y = this.y    adjacent.x = this.x - 1
                    adjacent.color = getColor(adjacent);
                   if( adjacent.color = destination.color ) return 1


                   //non devo fare subito il return ma devo fare l'or tra tutti isVisible e restituire quello
                 }
          default: return 0;

         */
        return false;
    }

}