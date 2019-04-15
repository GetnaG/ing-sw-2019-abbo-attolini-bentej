package it.polimi.ingsw.server.model.board;
import java.util.*;

/**
 * 
 */
public class Room {

    private Color color;
    private List<Square> squares;


    public Room(Color c, List<Square> s ) {
            for(Square sq: squares)
            {
                sq.getRoom().color=c;
            }
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public Color getColorRes() {
       return this.color;
    }

    public List<Square> getSquares(){
        return squares;
    }
}