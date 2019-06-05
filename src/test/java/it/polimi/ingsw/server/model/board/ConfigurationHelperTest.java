package it.polimi.ingsw.server.model.board;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationHelperTest {


    @Test
    void boardCreator() {


        List<Room> m1 = ConfigurationHelper.boardCreator(Configurations.STANDARD1);

       /* for(Room r:m1){

                if(r.getSpawnSquare() != null) {
                    System.out.println("SpawnSquare " + r.getSpawnSquare().getID());
                    if (r.getSpawnSquare().getNorth() == null)
                        System.out.println(" : north -> " + r.getSpawnSquare().getNorth() + " northB -> " + r.getSpawnSquare().getNorthBorder());
                    else
                        System.out.println(" : north -> " + r.getSpawnSquare().getNorth().getID() + " northB -> " + r.getSpawnSquare().getNorthBorder());

                    if (r.getSpawnSquare().getSouth() == null)
                        System.out.println(" : south -> " + r.getSpawnSquare().getSouth() + " southB -> " + r.getSpawnSquare().getSouthBorder());
                    else
                        System.out.println(" : south -> " + r.getSpawnSquare().getSouth().getID() + " southB -> " + r.getSpawnSquare().getSouthBorder());

                    if (r.getSpawnSquare().getEast() == null)
                        System.out.println(" : east  -> " + r.getSpawnSquare().getEast() + " east B -> " + r.getSpawnSquare().getEastBorder());
                    else
                        System.out.println(" : east  -> " + r.getSpawnSquare().getEast().getID() + " east B -> " + r.getSpawnSquare().getEastBorder());

                    if (r.getSpawnSquare().getWest() == null)
                        System.out.println(" : west  -> " + r.getSpawnSquare().getWest() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
                    else
                        System.out.println(" : west  -> " + r.getSpawnSquare().getWest().getID() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
                }

            for(Square s: r.getSquares()) {
                System.out.println("Square " + s.getID());
                if(s.getNorth() == null) System.out.println(" : north -> " + s.getNorth() + " northB -> " + s.getNorthBorder());
                else System.out.println(" : north -> " + s.getNorth().getID() + " northB -> " + s.getNorthBorder());
                
                if(s.getSouth() == null) System.out.println(" : south -> " + s.getSouth() + " southB -> " + s.getSouthBorder());
                else System.out.println(" : south -> " + s.getSouth().getID() + " southB -> " + s.getSouthBorder());

                if(s.getEast() == null) System.out.println(" : east  -> " + s.getEast()+ " east B -> " + s.getEastBorder());
                else System.out.println(" : east  -> " + s.getEast().getID() + " east B -> " + s.getEastBorder());

                if(s.getWest() == null) System.out.println(" : west  -> " + s.getWest() + " west B -> " + s.getWestBorder() + "\n\n");
                else System.out.println(" : west  -> " + s.getWest().getID() + " west B -> " + s.getWestBorder() + "\n\n");


            }

        }*/

        assertEquals(m1.size(), 5);


       // System.out.println("-------------------------------------------------------------------------------------");

        Square squareA = new Square();
        Square squareB = new Square();
        List<Square> listA = new ArrayList<>();
        List<Square> listB = new ArrayList<>();
        listA.add(squareA);
        listB.add(squareB);
        Room roomA = new Room(listA);
        Room roomB = new Room(listB);
        Room[] mapA = {roomA};
        Room[] mapB = {roomB};
        Room[][] maps = {mapA, mapB};
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(maps, Room[][].class));
        List<Room> m2 = ConfigurationHelper.boardCreator(Configurations.STANDARD2);

        /*for(Room r: m2){

                if(r.getSpawnSquare() != null) {
                    System.out.println("SpawnSquare " + r.getSpawnSquare().getID());
                    if (r.getSpawnSquare().getNorth() == null)
                        System.out.println(" : north -> " + r.getSpawnSquare().getNorth() + " northB -> " + r.getSpawnSquare().getNorthBorder());
                    else
                        System.out.println(" : north -> " + r.getSpawnSquare().getNorth().getID() + " northB -> " + r.getSpawnSquare().getNorthBorder());

                    if (r.getSpawnSquare().getSouth() == null)
                        System.out.println(" : south -> " + r.getSpawnSquare().getSouth() + " southB -> " + r.getSpawnSquare().getSouthBorder());
                    else
                        System.out.println(" : south -> " + r.getSpawnSquare().getSouth().getID() + " southB -> " + r.getSpawnSquare().getSouthBorder());

                    if (r.getSpawnSquare().getEast() == null)
                        System.out.println(" : east  -> " + r.getSpawnSquare().getEast() + " east B -> " + r.getSpawnSquare().getEastBorder());
                    else
                        System.out.println(" : east  -> " + r.getSpawnSquare().getEast().getID() + " east B -> " + r.getSpawnSquare().getEastBorder());

                    if (r.getSpawnSquare().getWest() == null)
                        System.out.println(" : west  -> " + r.getSpawnSquare().getWest() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
                    else
                        System.out.println(" : west  -> " + r.getSpawnSquare().getWest().getID() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
                }

            for(Square s: r.getSquares()) {
                System.out.println("Square " + s.getID());
                if(s.getNorth() == null) System.out.println(" : north -> " + s.getNorth() + " northB -> " + s.getNorthBorder());
                else System.out.println(" : north -> " + s.getNorth().getID() + " northB -> " + s.getNorthBorder());

                if(s.getSouth() == null) System.out.println(" : south -> " + s.getSouth() + " southB -> " + s.getSouthBorder());
                else System.out.println(" : south -> " + s.getSouth().getID() + " southB -> " + s.getSouthBorder());

                if(s.getEast() == null) System.out.println(" : east  -> " + s.getEast()+ " east B -> " + s.getEastBorder());
                else System.out.println(" : east  -> " + s.getEast().getID() + " east B -> " + s.getEastBorder());

                if(s.getWest() == null) System.out.println(" : west  -> " + s.getWest() + " west B -> " + s.getWestBorder() + "\n\n");
                else System.out.println(" : west  -> " + s.getWest().getID() + " west B -> " + s.getWestBorder() + "\n\n");


            }

        }*/

        assertEquals(m2.size(), 5);


      // System.out.println("-------------------------------------------------------------------------------------");


        List<Room> m3 = ConfigurationHelper.boardCreator(Configurations.ADVISED34);

       /* for(Room r: m3){

            if(r.getSpawnSquare() != null) {
                System.out.println("SpawnSquare " + r.getSpawnSquare().getID());
                if (r.getSpawnSquare().getNorth() == null)
                    System.out.println(" : north -> " + r.getSpawnSquare().getNorth() + " northB -> " + r.getSpawnSquare().getNorthBorder());
                else
                    System.out.println(" : north -> " + r.getSpawnSquare().getNorth().getID() + " northB -> " + r.getSpawnSquare().getNorthBorder());

                if (r.getSpawnSquare().getSouth() == null)
                    System.out.println(" : south -> " + r.getSpawnSquare().getSouth() + " southB -> " + r.getSpawnSquare().getSouthBorder());
                else
                    System.out.println(" : south -> " + r.getSpawnSquare().getSouth().getID() + " southB -> " + r.getSpawnSquare().getSouthBorder());

                if (r.getSpawnSquare().getEast() == null)
                    System.out.println(" : east  -> " + r.getSpawnSquare().getEast() + " east B -> " + r.getSpawnSquare().getEastBorder());
                else
                    System.out.println(" : east  -> " + r.getSpawnSquare().getEast().getID() + " east B -> " + r.getSpawnSquare().getEastBorder());

                if (r.getSpawnSquare().getWest() == null)
                    System.out.println(" : west  -> " + r.getSpawnSquare().getWest() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
                else
                    System.out.println(" : west  -> " + r.getSpawnSquare().getWest().getID() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
            }

            for(Square s: r.getSquares()) {
                System.out.println("Square " + s.getID());
                if(s.getNorth() == null) System.out.println(" : north -> " + s.getNorth() + " northB -> " + s.getNorthBorder());
                else System.out.println(" : north -> " + s.getNorth().getID() + " northB -> " + s.getNorthBorder());

                if(s.getSouth() == null) System.out.println(" : south -> " + s.getSouth() + " southB -> " + s.getSouthBorder());
                else System.out.println(" : south -> " + s.getSouth().getID() + " southB -> " + s.getSouthBorder());

                if(s.getEast() == null) System.out.println(" : east  -> " + s.getEast()+ " east B -> " + s.getEastBorder());
                else System.out.println(" : east  -> " + s.getEast().getID() + " east B -> " + s.getEastBorder());

                if(s.getWest() == null) System.out.println(" : west  -> " + s.getWest() + " west B -> " + s.getWestBorder() + "\n\n");
                else System.out.println(" : west  -> " + s.getWest().getID() + " west B -> " + s.getWestBorder() + "\n\n");


            }

        }*/

        assertEquals(m3.size(), 5);



        //System.out.println("-------------------------------------------------------------------------------------");


        List<Room> m4 = ConfigurationHelper.boardCreator(Configurations.ADVISED45);

       /* for(Room r: m4){

            if(r.getSpawnSquare() != null) {
                System.out.println("SpawnSquare " + r.getSpawnSquare().getID());
                if (r.getSpawnSquare().getNorth() == null)
                    System.out.println(" : north -> " + r.getSpawnSquare().getNorth() + " northB -> " + r.getSpawnSquare().getNorthBorder());
                else
                    System.out.println(" : north -> " + r.getSpawnSquare().getNorth().getID() + " northB -> " + r.getSpawnSquare().getNorthBorder());

                if (r.getSpawnSquare().getSouth() == null)
                    System.out.println(" : south -> " + r.getSpawnSquare().getSouth() + " southB -> " + r.getSpawnSquare().getSouthBorder());
                else
                    System.out.println(" : south -> " + r.getSpawnSquare().getSouth().getID() + " southB -> " + r.getSpawnSquare().getSouthBorder());

                if (r.getSpawnSquare().getEast() == null)
                    System.out.println(" : east  -> " + r.getSpawnSquare().getEast() + " east B -> " + r.getSpawnSquare().getEastBorder());
                else
                    System.out.println(" : east  -> " + r.getSpawnSquare().getEast().getID() + " east B -> " + r.getSpawnSquare().getEastBorder());

                if (r.getSpawnSquare().getWest() == null)
                    System.out.println(" : west  -> " + r.getSpawnSquare().getWest() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
                else
                    System.out.println(" : west  -> " + r.getSpawnSquare().getWest().getID() + " west B -> " + r.getSpawnSquare().getWestBorder() + "\n\n");
            }

            for(Square s: r.getSquares()) {
                System.out.println("Square " + s.getID());
                if(s.getNorth() == null) System.out.println(" : north -> " + s.getNorth() + " northB -> " + s.getNorthBorder());
                else System.out.println(" : north -> " + s.getNorth().getID() + " northB -> " + s.getNorthBorder());

                if(s.getSouth() == null) System.out.println(" : south -> " + s.getSouth() + " southB -> " + s.getSouthBorder());
                else System.out.println(" : south -> " + s.getSouth().getID() + " southB -> " + s.getSouthBorder());

                if(s.getEast() == null) System.out.println(" : east  -> " + s.getEast()+ " east B -> " + s.getEastBorder());
                else System.out.println(" : east  -> " + s.getEast().getID() + " east B -> " + s.getEastBorder());

                if(s.getWest() == null) System.out.println(" : west  -> " + s.getWest() + " west B -> " + s.getWestBorder() + "\n\n");
                else System.out.println(" : west  -> " + s.getWest().getID() + " west B -> " + s.getWestBorder() + "\n\n");


            }

        }*/

//        assertEquals(m4.size(), 6);

    }
    
}