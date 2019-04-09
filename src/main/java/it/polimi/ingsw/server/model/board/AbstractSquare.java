package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AbstractCard;

/**
 *  defines the structure of a square
 */
public abstract class AbstractSquare {


    private Room room;  //-- group squares by color

    private AbstractSquare north;
    private AbstractSquare south;
    private AbstractSquare east;
    private AbstractSquare west;

    // -- Border : { Wall, Door, Corridor} <- Set when creating the map
    private Border northBorder;
    private Border southBorder;
    private Border eastBorder;
    private Border westBorder;

    protected ReplaceListener replacer;


    /**
     * Default constructor
     */
    public AbstractSquare() {
        room=null;
        north=null;
        south=null;
        east=null;
        west=null;
        northBorder=null;
        southBorder=null;
        eastBorder=null;
        westBorder=null;
    }

    /**
     * implemented by TurretSquare and SpawnSquare
     * @return in TurretSquare is AmmoCard, in SpawnSquare is List<WeaponCard>
     */
    public AbstractCard getGrabbables() {
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
                if(this.northBorder==Border.DOOR){
                    if(this.north.room==destination.room)
                        return true;

                }

                if(this.southBorder==Border.DOOR){
                      if(this.south.room==destination.room)
                          return true;
                }

                 if(this.eastBorder==Border.DOOR){
                     if(this.east.room==destination.room)
                         return true;
                 }

                 if(this.westBorder==Border.DOOR){
                     if(this.west.room==destination.room)
                          return true;
                 }
        }

        return false;
    }


    public Room getRoom() {
        return room;
    }


}

//gameboard ha lista di room, ogni room ha una lista di squares, raggruppati dal colore