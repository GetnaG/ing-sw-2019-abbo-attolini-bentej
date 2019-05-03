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
        this.spawnSquare = spawnSquare;
    }

    public Room( List<Square> squares ) {
        this.squares = squares;
        spawnSquare = null;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
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