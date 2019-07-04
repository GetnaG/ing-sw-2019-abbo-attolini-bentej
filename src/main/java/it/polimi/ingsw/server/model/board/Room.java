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

    public Room(Room copyOf) {
        squares = copyOf.squares.stream().map(Square::new).collect(Collectors.toList());
        if (copyOf.spawnSquare != null)
            spawnSquare = new SpawnSquare(copyOf.spawnSquare);
        else
            spawnSquare = null;
    }

    /**
     * @param squares is the list of the normal (ammo) squares
     * @param spawnSquare is considered a single element
     */
    public Room( List<Square> squares, SpawnSquare spawnSquare ) {

        this.squares = squares;

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
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public List<Square> getAllSquares(){
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
     * @param obj is a Room.
     * @return true if two rooms have the same list of squares and the same spawnSquare.
     */
    @Override
    public boolean equals(Object obj) {
        if(this.getSpawnSquare() != null)
            return (obj instanceof Room) && (((Room) obj).squares.equals(squares) && ((Room) obj).getSpawnSquare().equals(spawnSquare));
        else
            return (obj instanceof Room) && (((Room) obj).squares.equals(squares));
    }

    public static void refresh(List<Room> rooms) {
        List<Square> allSquares = rooms.stream().map(Room::getAllSquares).flatMap(List::stream).collect(Collectors.toList());
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