package it.polimi.ingsw.server.model.board;
import java.util.*;

/**
 * 
 */
public class Room {

    private Color color;
    private List<AbstractSquare> squares;


    public Room(Color c, List<AbstractSquare> s ) {
            color=c;

    }

    public void setSquares(List<AbstractSquare> squares) {
        this.squares = squares;
    }

    public Color getColorRes() {
       return this.color;
    }

}