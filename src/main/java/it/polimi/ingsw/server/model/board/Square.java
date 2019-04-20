package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AbstractCard;
import it.polimi.ingsw.server.model.cards.AmmoCard;

/**
 *  Defines the structure of a square.
 */
public class Square {


    private Room room;
    private AmmoCard ammoCard;
    private Color color;

    private Square north;
    private Square south;
    private Square east;
    private Square west;

    private Border northBorder;
    private Border southBorder;
    private Border eastBorder;
    private Border westBorder;


    protected ReplaceListener replacer;

    public void setRoom(Room room) {
        this.room = room;
    }

    public Square getNorth() {
        return north;
    }

    public void setNorth(Square north) {
        this.north = north;
    }

    public Square getSouth() {
        return south;
    }

    public void setSouth(Square south) {
        this.south = south;
    }

    public Square getEast() {
        return east;
    }

    public void setEast(Square east) {
        this.east = east;
    }

    public Square getWest() {
        return west;
    }

    public void setWest(Square west) {
        this.west = west;
    }

    public Border getNorthBorder() {
        return northBorder;
    }

    public void setNorthBorder(Border northBorder) {
        this.northBorder = northBorder;
    }

    public Border getSouthBorder() {
        return southBorder;
    }

    public void setSouthBorder(Border southBorder) {
        this.southBorder = southBorder;
    }

    public Border getEastBorder() {
        return eastBorder;
    }

    public void setEastBorder(Border eastBorder) {
        this.eastBorder = eastBorder;
    }

    public Border getWestBorder() {
        return westBorder;
    }

    public void setWestBorder(Border westBorder) {
        this.westBorder = westBorder;
    }

    public ReplaceListener getReplacer() {
        return replacer;
    }

    public void setReplacer(ReplaceListener replacer) {
        this.replacer = replacer;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    /**
     * Default constructor
     */
    public Square(Color color) {
        room=null;
        north=null;
        south=null;
        east=null;
        west=null;
        northBorder=null;
        southBorder=null;
        eastBorder=null;
        westBorder=null;
        replacer = null;
        this.color = color;
    }

    /**
     * Default constructor
     */
    public Square() {
        room=null;
        north=null;
        south=null;
        east=null;
        west=null;
        northBorder=null;
        southBorder=null;
        eastBorder=null;
        westBorder=null;
        replacer = null;
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
    public boolean checkVisible(Square destination) {

        if( this.room == destination.room )
            return true;

        else{
                if(this.northBorder!=Border.WALL){
                    if(this.north.room==destination.room)
                        return true;

                }

                if(this.southBorder!=Border.WALL){
                      if(this.south.room==destination.room)
                          return true;
                }

                 if(this.eastBorder!=Border.WALL){
                     if(this.east.room==destination.room)
                         return true;
                 }

                 if(this.westBorder!=Border.WALL){
                     if(this.west.room==destination.room)
                          return true;
                 }
        }

        return false;
    }


    public Room getRoom() {
        return room;
    }

    public void setGrabbable (AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }


}
