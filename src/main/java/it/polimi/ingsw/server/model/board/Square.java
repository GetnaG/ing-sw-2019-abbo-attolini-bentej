package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AbstractCard;
import it.polimi.ingsw.server.model.cards.AmmoCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the structure of a square.
 */
public class Square {


    protected ReplaceListener replacer;
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


    /**
     * Default constructor of a physical square
     */
    public Square(Color color) {
        room = null;
        north = null;
        south = null;
        east = null;
        west = null;
        northBorder = null;
        southBorder = null;
        eastBorder = null;
        westBorder = null;
        replacer = null;
        this.color = color;
    }

    /**
     * Default constructor of an abstract square
     */
    public Square() {
        this(null);
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
     * @return the list is organized as follows: Cardinals[
     * this,
     * list of northern squares,
     * list of southern squares,
     * list of eastern squares,
     * list of western squares]
     *
     * if the square does not confines with a square, then its position in the list is set to NULL
     */
    public List<Square> getCardinals() {
        List<Square> cardinals = null;
        int i = 0;
        Square temp = this;

        cardinals.add(i, this);

        while (!(temp.north == null )) {
            cardinals.add(i, temp.north);
            i++;
        }
        while (!(temp.south == null )) {
            cardinals.add(i, temp.south);
            i++;
        }
        while (!(temp.east == null )) {
            cardinals.add(i, temp.east);
            i++;
        }
        while (!(temp.west == null )) {
            cardinals.add(i, temp.west);
            i++;
        }

        return cardinals;
    }

    /**
     * implemented by TurretSquare and SpawnSquare
     *
     * @return in TurretSquare is AmmoCard, in SpawnSquare is List<WeaponCard>
     */
    public AbstractCard getGrabbables() {
        return null;
    } // chiedere a fahed

    /**
     * @param destination is the square that will be checked
     * @return 1 if the destination square is visible from the calling square, 0 otherwise
     */
    public boolean checkVisible(Square destination) {

        if (this.room == destination.room)
            return true;

        else {
            if (this.northBorder != Border.WALL) {
                if (this.north.room == destination.room)
                    return true;

            }

            if (this.southBorder != Border.WALL) {
                if (this.south.room == destination.room)
                    return true;
            }

            if (this.eastBorder != Border.WALL) {
                if (this.east.room == destination.room)
                    return true;
            }

            if (this.westBorder != Border.WALL) {
                if (this.west.room == destination.room)
                    return true;
            }
        }

        return false;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setGrabbable(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }

    /**
     * @return returns a list of all the visible squares
     * 1) the calling square is always visible
     * 2) for the first square in every room, check if it is visible from this square
     * 3) if it is, add the entire room to the list of visible squares
     */
    public List<Square> listOfVisibles(GameBoard gb) {
        List<Square> visibleSquares = new ArrayList<>();
        int i = 0;
        int j = 0;
        List<Room> config = gb.getConfiguration();
        Square temp;

        visibleSquares.set(j, this);
        for (Room r : config) {
            temp = r.getSquares().get(i);
            if (this.checkVisible(temp)) {
                visibleSquares.addAll(j, r.getSquares());
                j = j + r.getSquares().size();
            }
        }
        return visibleSquares;
    }

    /**
     * @param obj is a square
     * @return true if the squares are the same instances
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Square))
            return false;

        if (!(this.north == null && ((Square) obj).north == null)) {
            if (!(this.north != null && ((Square) obj).north != null) || ((Square) obj).north != north  )  {
                return false;
            }
        }

        if (!(this.south == null && ((Square) obj).south == null)) {
            if (!(this.south != null && ((Square) obj).south != null)  ||  ((Square) obj).south != south) {
                return false;
            }
        }

        if (!(this.east == null && ((Square) obj).east == null)) {
            if (!(this.east != null && ((Square) obj).east != null)  ||  ((Square) obj).east != east) {
                return false;
            }
        }

        if (!(this.west == null && ((Square) obj).west == null)) {
            if (!(this.west != null && ((Square) obj).west != null)  ||  ((Square) obj).west != west) {
                return false;
            }
        }

        return true;
    }


    /**
     * @param dest is a square.
     * @return true if this and dest lie on the same line (aka there is a straight line with no walls between them)
     */
    public boolean straight(Square dest) {

        if (this.equals(dest)) return true;

        Square temp = this;

        while (!(temp.north == null ) && !temp.northBorder.equals(Border.WALL)) {
            if (temp.north.equals(dest))
                return true;

            temp = temp.north;
        }

        while (!(temp.south == null ) && !temp.southBorder.equals(Border.WALL)) {
            if (temp.south.equals(dest))
                return true;

            temp = temp.south;
        }

        while (!(temp.east == null ) && !temp.eastBorder.equals(Border.WALL)) {
            if (temp.east.equals(dest))
                return true;

            temp = temp.east;
        }

        while (!(temp.west == null ) && !temp.westBorder.equals(Border.WALL)) {
            if (temp.west.equals(dest))
                return true;

            temp = temp.west;
        }
        return false;

    }
}
