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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //true se due sqaure sono nella stessa room, guarda abstract card anche per square
    }
}