package it.polimi.ingsw.server.model;
import java.util.List;

/**
 *  defines the structure of a square
 */
public abstract class AbstractSquare {

    private String color;
    private Room room;

    private AbstractSquare north;
    private AbstractSquare south;
    private AbstractSquare east;
    private AbstractSquare west;

    // -- Border : { Wall, Door, Corridor} <- Set when creating the map
    private String northBorder;
    private String southBorder;
    private String eastBorder;
    private String westBorder;

    protected ReplaceListener replacer;


    /**
     * Default constructor
     */
    public AbstractSquare() {
        color="";
        room=null;
        north=null;
        south=null;
        east=null;
        west=null;
        northBorder="";
        southBorder="";
        eastBorder="";
        westBorder="";
    }

    /**
     * implemented by TurretSquare and SpawnSquare
     * @return in TurretSquare is AmmoCard, in SpawnSquare is List<WeaponCard>
     */
    public List<AbstractCard> getGrabbables() {
        return null;
    }

    /**
     * @param destination 
     * @return 1 if the destination square is visible from the calling square, 0 otherwise
     */
    public boolean checkVisible(AbstractSquare destination) {

        if( this.room == destination.room )
            return true;
        else{
                if(!this.northBorder.equals("wall")){

                }


        }
               /* AbstractSquare adjacent, support

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