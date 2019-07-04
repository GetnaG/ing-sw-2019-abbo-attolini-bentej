package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.cards.AmmoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines the structure of a square.
 */
public class Square {


    /**
     * The replace will take care of replacing the ammo card grabbed
     * or the weapon picked from the market
     */
    ReplaceListener replacer;
    /**
     * This parameter is set only if the implementation will
     * build as a normal square, otherwise will be left to null
     */
    private AmmoCard ammoCard;
    /**
     * Represents the color of the Square,
     * Chosen from @SquareColor
     */
    private SquareColor squareColor;
    /**
     * It's an assigned number,
     * based on the implementation of the board
     */
    private int ID;
    /**
     * Represents the northern neighbour
     * It is connected with it via northBoarder
     */
    private Square north;
    /**
     * Represents the ID of the northern neighbour
     */
    private int idNorth;
    /**
     * Represents the southern neighbour
     * It is connected with it via southBoarder
     */
    private Square south;
    /**
     * Represents the ID of the southern neighbour
     */
    private int idSouth;
    /**
     * Represents the eastern neighbour
     * It is connected with it via eastBoarder
     */
    private Square east;
    /**
     * Represents the ID of the eastern neighbour
     */
    private int idEast;
    /**
     * Represents the western neighbour
     * It is connected with it via westBoarder
     */
    private Square west;
    /**
     * Represents the ID of the western neighbour
     */
    private int idWest;
    /**
     * Represents the type of connection this Square has with its neighbour
     */
    private Border northBorder;
    /**
     * Represents the type of connection this Square has with its neighbour
     */
    private Border southBorder;
    /**
     * Represents the type of connection this Square has with its neighbour
     */
    private Border eastBorder;
    /**
     * Represents the type of connection this Square has with its neighbour
     */
    private Border westBorder;


    /**
     * Default constructor of a physical square
     */
    public Square(SquareColor squareColor) {
        this.squareColor = squareColor;
    }

    /**
     * Constructor of a physical square. By default all, a square is isolated. This means that its borders are all walls.
     * @param squareColor  color of square
     * @param card AmmoCard associated with this square (in case of a deathmatch mode).
     */
    public Square(SquareColor squareColor, AmmoCard card) {
        this.squareColor = squareColor;
        this.ammoCard = card;
        northBorder = Border.WALL;
        eastBorder = Border.WALL;
        southBorder = Border.WALL;
        westBorder = Border.WALL;
    }

    /**
     * Creates a square that is a copy of the provided one.
     * The provided square needs to be refreshed.
     *
     * @param copyOf the square to copy
     */
    public Square(Square copyOf) {
        squareColor = copyOf.squareColor;
        ID = copyOf.ID;
        idNorth = copyOf.idNorth;
        idSouth = copyOf.idSouth;
        idWest = copyOf.idWest;
        idEast = copyOf.idEast;
        northBorder = copyOf.northBorder;
        southBorder = copyOf.southBorder;
        eastBorder = copyOf.eastBorder;
        westBorder = copyOf.westBorder;
        ammoCard = null;
        north = null;
        south = null;
        west = null;
        east = null;
    }

    /**
     * Default constructor of an abstract square
     */
    public Square() {
    }

    public Square getNorth() {
        return north;
    }

    public void setNorth(Square north) {
        if (this.north != north) {
            this.north = north;
            if (this.north != null) {
                this.north.setSouth(this);
            }
        }

    }

    public Square getSouth() {
        return south;
    }

    public void setSouth(Square south) {
        if (this.south != south) {
            this.south = south;
            if (this.south != null) {
                this.south.setNorth(this);
            }
        }

    }

    public Square getEast() {
        return east;
    }

    public void setEast(Square east) {
        if (this.east != east) {
            this.east = east;
            if (this.east != null) {
                this.east.setWest(this);
            }
        }

    }

    public Square getWest() {
        return west;
    }

    public void setWest(Square west) {
        if (this.west != west) {
            this.west = west;
            if (this.west != null) {
                this.west.setEast(this);
            }
        }

    }

    public Border getNorthBorder() {
        return northBorder;
    }

    public void setNorthBorder(Border northBorder) {
        this.northBorder = northBorder;
        if (this.north != null && this.north.getSouthBorder() == null)  // there is a valid square with a non initialized border
            this.north.setSouthBorder(northBorder);

    }

    public Border getSouthBorder() {
        return southBorder;
    }

    public void setSouthBorder(Border southBorder) {
        this.southBorder = southBorder;
        if (this.south != null && this.south.getNorthBorder() == null)  // there is a valid square with a non initialized border
            this.south.setNorthBorder(southBorder);
    }

    public Border getEastBorder() {
        return eastBorder;
    }

    public void setEastBorder(Border eastBorder) {
        this.eastBorder = eastBorder;
        if (this.east != null && this.east.getWestBorder() == null)  // there is a valid square with a non initialized border
            this.east.setWestBorder(eastBorder);
    }

    public Border getWestBorder() {
        return westBorder;
    }

    public void setWestBorder(Border westBorder) {
        this.westBorder = westBorder;
        if (this.west != null && this.west.getEastBorder() == null)  // there is a valid square with a non initialized border
            this.west.setEastBorder(westBorder);
    }

    public void setReplacer(ReplaceListener replacer) {
        this.replacer = replacer;
    }

    public SquareColor getSquareColor() {
        return squareColor;
    }

    public void setSquareColor(SquareColor squareColor) {
        this.squareColor = squareColor;
    }

    public String getID() {
        return Integer.toString(ID);
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    /**
     * Gets the Turret in this Square or null if the game mode is not Turret Mode.
     *
     * @return Turret in this Square
     */
    public Turret getTurret() {
        return null;
    }

    public AmmoCard peekAmmoCard() {
        return ammoCard;
    }

    public AmmoCard getAmmoCard() {
        if (ammoCard == null)
            return null;
        replacer.addSquare(this);
        AmmoCard tmp = ammoCard;
        ammoCard = null;
        return tmp;

    }

    public void setAmmoCard(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }

    /**
     * @return the list is organized as follows: Cardinals[
     * this,
     * list of northern squares,
     * list of southern squares,
     * list of eastern squares,
     * list of western squares]
     * <p>
     * if the square does not confines with a square, then its position in the list is set to NULL
     */
    public List<Square> getCardinals() {
        List<Square> cardinals = new ArrayList<>();
        Square temp = this;

        cardinals.add(this);

        while (temp.north != null) {
            cardinals.add(temp.north);
            temp = temp.north;
        }

        temp = this;

        while (temp.south != null) {
            cardinals.add(temp.south);
            temp = temp.south;
        }

        temp = this;

        while (temp.east != null) {
            cardinals.add(temp.east);
            temp = temp.east;
        }

        temp = this;

        while (temp.west != null) {
            cardinals.add(temp.west);
            temp = temp.west;
        }

        return cardinals;
    }




    /**
     * @return returns a list of all the visible squares
     * the calling square is always visible
     */
    public List<Square> listOfVisibles(GameBoard gb) {
        List<Square> visibleSquares = new ArrayList<>();

        /*All the square in the same room are visible*/
        visibleSquares.addAll(gb.getRoom(this).getAllSquares());

        if (northBorder == Border.DOOR)
            visibleSquares.addAll(gb.getRoom(north).getAllSquares());
        if (eastBorder == Border.DOOR)
            visibleSquares.addAll(gb.getRoom(east).getAllSquares());
        if (southBorder == Border.DOOR)
            visibleSquares.addAll(gb.getRoom(south).getAllSquares());
        if (westBorder == Border.DOOR)
            visibleSquares.addAll(gb.getRoom(west).getAllSquares());

        return visibleSquares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square)) return false;
        Square square = (Square) o;
        return ID == square.ID &&
                idNorth == square.idNorth &&
                idSouth == square.idSouth &&
                idEast == square.idEast &&
                idWest == square.idWest &&
                squareColor == square.squareColor &&
                northBorder == square.northBorder &&
                southBorder == square.southBorder &&
                eastBorder == square.eastBorder &&
                westBorder == square.westBorder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, idNorth, idSouth, idEast, idWest, squareColor, northBorder, southBorder, eastBorder, westBorder);
    }

    /**
     * @param dest is a square.
     * @return true if this and dest lie on the same line (aka there is a straight line with no walls between them)
     */
    public boolean straight(Square dest) {

        if (this.equals(dest)) return true;

        Square temp = this;

        while (temp.north != null && temp.northBorder != Border.WALL) {
            if (temp.north.equals(dest))
                return true;

            temp = temp.north;
        }

        temp = this;

        while (temp.south != null && temp.southBorder != Border.WALL) {
            if (temp.south.equals(dest))
                return true;

            temp = temp.south;
        }

        temp = this;

        while (temp.east != null && temp.eastBorder != Border.WALL) {
            if (temp.east.equals(dest))
                return true;

            temp = temp.east;
        }

        temp = this;

        while (temp.west != null && temp.westBorder != Border.WALL) {
            if (temp.west.equals(dest))
                return true;

            temp = temp.west;
        }
        return false;

    }
/*
    public void squareBuilder(Square n, Square e, Border nb, Border eb){
        if(n != null) this.setNorth(n);
        if(e != null) this.setEast(e);
        if(nb != null) this.setNorthBorder(nb);
        if(eb != null) this.setEastBorder(eb);
    }*/

    public void refresh(List<Room> rooms) {//TODO
        if (rooms == null || rooms.isEmpty()) return;

        this.north = getSquare(rooms, idNorth);
        this.east = getSquare(rooms, idEast);
        this.south = getSquare(rooms, idSouth);
        this.west = getSquare(rooms, idWest);
    }

    public static Square getSquare(List<Room> rooms, int idSquare) {
        for (Room room : rooms)
            for (Square s : room.getAllSquares())
                if (Integer.parseInt(s.getID()) == idSquare)
                    return s;
        return null;
    }

    public int getIdNorth() {
        return idNorth;
    }

    public void setIdNorth(int idNorth) {
        this.idNorth = idNorth;
    }

    public int getIdSouth() {
        return idSouth;
    }

    public void setIdSouth(int idSouth) {
        this.idSouth = idSouth;
    }

    public int getIdEast() {
        return idEast;
    }

    public void setIdEast(int idEast) {
        this.idEast = idEast;
    }

    public int getIdWest() {
        return idWest;
    }

    public void setIdWest(int idWest) {
        this.idWest = idWest;
    }
}
