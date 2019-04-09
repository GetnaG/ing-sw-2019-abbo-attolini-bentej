package it.polimi.ingsw.server.model;
import java.util.*;

/**
 * 
 */
public class Room {

    /**
     *
     */
    private Set<AbstractSquare> squares;
    private Room room;
    private Color color;


    /**
     * Default constructor
     */
    public Room(/*list<AbstractSquare> s*/) {   //la lista s contiene tutti i quadrati della configurazione scelta per giocare
        /*
                for each AbstractSquare x in s:     //raggruppo quadrati con lo stesso colore -> voglio room rossa, room blu...
                    squares.color[x] = String
         */
    }


    /**
     * @return
     */
    public Color getColorRes() {
       return room.color;
    }

}