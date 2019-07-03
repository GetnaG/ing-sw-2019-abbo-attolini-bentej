package it.polimi.ingsw.server.model.board;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  A room consists in a list of Squares
 *  It can either contain a SpawnSquare (if and only if its color is BLUE, YELLOW or RED), or not (in which case
 *  its color is chosen among the colors listed in the enum SquareColor)
 *
 *  If the room contains a SpawnSquare, it is always set as the first element of the list of Squares that make up
 *  the room
 *
 */
public class Room {

    private List<Square> squares;
    private SpawnSquare spawnSquare;

    /**
     * @param squares is the list of the normal (ammo) squares
     * @param spawnSquare is considered a single element
     */
    public Room( List<Square> squares, SpawnSquare spawnSquare ) {

        this.squares = squares;
        /*for(Square i: squares)
            i.setRoom(this);*/

        this.spawnSquare = spawnSquare;

        if(squares.isEmpty())
            throw new IllegalArgumentException();
        else
            for(Square x: squares)
                if(squares.get(0).getSquareColor() != x.getSquareColor())
                    throw new IllegalArgumentException();
    }

    public Room( List<Square> squares ) {
        this.squares = squares;
        /*for(Square i: squares)
            i.setRoom(this);*/
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
        /*for(Square i: squares)
            i.setRoom(this);*/
    }

    public List<Square> getSquares(){
        List<Square> returned = new ArrayList<>(squares);
        if (spawnSquare != null)
            returned.add(spawnSquare);
        return returned;
    }

    public boolean hasSpawnSquare() {
        return (getSpawnSquare() != null);
    }

    public SpawnSquare getSpawnSquare() {
        return spawnSquare;
    }


    /**
     * @param obj is a room
     * @return true if two rooms have the same list of squares and the same spawnSquare
     */
    @Override
    public boolean equals(Object obj) {
        if(this.getSpawnSquare() != null)
            return (obj instanceof Room) && (((Room) obj).squares.equals(squares) && ((Room) obj).getSpawnSquare().equals(spawnSquare));
        else
            return (obj instanceof Room) && (((Room) obj).squares.equals(squares));
    }

    public static void refresh(List<Room> rooms) {
        List<Square> allSquares = rooms.stream().map(Room::getSquares).flatMap(List::stream).collect(Collectors.toList());
        for (Square square : allSquares) {
            square.setNorth(Square.getSquare(rooms, square.getIdNorth()));
            square.setEast(Square.getSquare(rooms, square.getIdEast()));
            square.setSouth(Square.getSquare(rooms, square.getIdSouth()));
            square.setWest(Square.getSquare(rooms, square.getIdWest()));
        }
        for (Room r : rooms) {
            if (r.hasSpawnSquare())
                r.getSpawnSquare().refresh(rooms);
        }
    }
 }