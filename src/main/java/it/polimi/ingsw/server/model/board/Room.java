package it.polimi.ingsw.server.model.board;
import java.util.*;

/**
 *  A room consists in a list of Squares
 *  It can either contain a SpawnSquare (if and only if its color is BLUE, YELLOW or RED), or not (in which case
 *  its color is chosen among the colors listed in the enum Color)
 *
 *  If the room contains a SpawnSquare, it is always set as the first element of the list of Squares that make up
 *  the room
 *
 */
public class Room {

    private List<Square> squares;
    private SpawnSquare spawnSquare;


    public Room( List<Square> squares, SpawnSquare spawnSquare ) {

        this.squares = squares;
        for(Square i: squares)
            i.setRoom(this);

        this.spawnSquare = spawnSquare;

        if(squares.get(0)  != this.spawnSquare)
            swap0(squares, spawnSquare);

        if(squares.isEmpty())
            throw new IllegalArgumentException();
        else
            for(Square x: squares)
                if(squares.get(0).getColor() != x.getColor())
                    throw new IllegalArgumentException();


    }

    public Room( List<Square> squares ) {
        this.squares = squares;
        for(Square i: squares)
            i.setRoom(this);
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
        for(Square i: squares)
            i.setRoom(this);
    }

    public List<Square> getSquares(){
        return squares;
    }

    public boolean hasSpawnSquare() {
        if(getSpawnSquare() != null)
            return true;
        return false;
    }

    public SpawnSquare getSpawnSquare() {
        return spawnSquare;
    }


    /**
     * @param obj is a room
     * @return true if two rooms have the same list of squares
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Room) && (((Room) obj).getSquares().equals(squares));
    }
    
    

    private List<Square> swap0(List<Square> s, Square b){
        int i=0;
        Square tmp;

        while(!s.get(i).equals(b))
            i++;
        
        tmp = s.get(0);
        s.set(0, b);
        s.set(i, tmp);

        return s;
    }


}