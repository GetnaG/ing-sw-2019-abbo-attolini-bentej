package it.polimi.ingsw.server.model.board;
import java.util.*;

/**
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

        //set spawnsqaure as leader of the list of the room squares, check that a room has exactly 1 spawnsquare
        if(squares.get(0)  != this.spawnSquare)
            ;  //------------------------------------------------------------------ exception????????????????


    }

    public Room( List<Square> squares ) {
        this.squares = squares;
        for(Square i: squares)
            i.setRoom(this);
        spawnSquare = null;
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
        return spawnSquare != null;
    }

    public SpawnSquare getSpawnSquare() {
        return spawnSquare;
    }


    /**
     * @param obj is a room
     * @return true if two squares belong to the same room
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Room &&
                (
                        ((Room) obj).getSquares().equals(squares)
                );
    }
}