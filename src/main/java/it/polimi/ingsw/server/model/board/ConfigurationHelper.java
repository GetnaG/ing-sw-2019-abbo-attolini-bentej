package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationHelper {

    private Configurations c;
    private List<Room> mapWithRooms;

    public ConfigurationHelper(Configurations c) {
        this.c = c;
        this.mapWithRooms = new ArrayList<>();
    }

    public List<Room> boardCreator() {

        SpawnSquare blueSS = new SpawnSquare(AmmoCube.BLUE, null);
        blueSS.setID(10);
        SpawnSquare yellowSS = new SpawnSquare(AmmoCube.YELLOW, null);
        yellowSS.setID(3);
        SpawnSquare redSS = new SpawnSquare(AmmoCube.RED, null);
        redSS.setID(4);

        switch (c) {
            case STANDARD1: {

                Square w1 = new Square(SquareColor.WHITE);
                w1.setID(0);
                Square w2 = new Square(SquareColor.WHITE);
                w2.setID(1);
                Square w3 = new Square(SquareColor.WHITE);
                w3.setID(2);
                Square p1 = new Square(SquareColor.PURPLE);
                p1.setID(5);
                Square p2 = new Square(SquareColor.PURPLE);
                p2.setID(6);
                Square y2 = new Square(SquareColor.YELLOW);
                y2.setID(7);
                Square r2 = new Square(SquareColor.RED);
                r2.setID(8);
                Square b2 = new Square(SquareColor.BLUE);
                b2.setID(9);

                //white room

                w1.squareBuilder(redSS, w2, Border.DOOR, Border.CORRIDOR);
                w2.squareBuilder(p1, w3, Border.DOOR, Border.CORRIDOR);
                w3.squareBuilder(p2, yellowSS, Border.WALL, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        w1, w2, w3
                )));

                //yellow room

                yellowSS.squareBuilder(y2, null, Border.CORRIDOR, null);
                //the second yellow square ha null parameters and is set as neighbour by p2

                mapWithRooms.add(new Room(Arrays.asList(
                        y2
                ), yellowSS));


                //red room

                redSS.squareBuilder(r2, p1, Border.CORRIDOR, Border.WALL);
                r2.squareBuilder(null, b2, null, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        r2
                ), redSS));

                //purple room

                p1.squareBuilder(b2, p2, Border.DOOR, Border.CORRIDOR);
                p2.squareBuilder(blueSS, y2, Border.DOOR, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        p1, p2
                )));

                //blue room

                b2.squareBuilder(null, blueSS, null, Border.CORRIDOR);
                //the blue SpawnSquare is set by b2 and p2

                mapWithRooms.add(new Room(Arrays.asList(
                        b2
                ), blueSS));

                break;
            }

            case STANDARD2: {
                Square w1 = new Square(SquareColor.WHITE);
                w1.setID(1);
                Square y2 = new Square(SquareColor.YELLOW);
                y2.setID(2);
                Square r2 = new Square(SquareColor.RED);
                r2.setID(5);
                Square y3 = new Square(SquareColor.YELLOW);
                y3.setID(6);
                Square y4 = new Square(SquareColor.YELLOW);
                y4.setID(7);
                Square b2 = new Square(SquareColor.BLUE);
                b2.setID(8);
                Square b3 = new Square(SquareColor.BLUE);
                b3.setID(9);
                Square g1 = new Square(SquareColor.GREEN);
                g1.setID(11);

                //white room

                w1.squareBuilder(r2, y2, Border.DOOR, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        w1
                )));

                //yellow room

                yellowSS.squareBuilder(y4, null, Border.CORRIDOR, null);
                y2.squareBuilder(y3, yellowSS, Border.CORRIDOR, Border.CORRIDOR);
                y3.squareBuilder(blueSS, y4, Border.DOOR, Border.CORRIDOR);
                y4.squareBuilder(g1, null, Border.DOOR, null);

                mapWithRooms.add(new Room(Arrays.asList(
                        y2, y3, y4
                ), yellowSS));

                //red room

                redSS.squareBuilder(b2, r2, Border.DOOR, Border.CORRIDOR);
                r2.squareBuilder(b3, y3, Border.WALL, Border.WALL);

                mapWithRooms.add(new Room(Arrays.asList(
                        r2
                ), redSS));

                //blue room

                blueSS.squareBuilder(null, g1, null, Border.DOOR);
                b2.squareBuilder(null, b3, null, Border.CORRIDOR);
                b3.squareBuilder(null, blueSS, null, Border.CORRIDOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        b2, b3
                ), blueSS));

                //green room

                //g1 has already been set by his neighbour blueSS and y4

                mapWithRooms.add(new Room(Arrays.asList(
                        g1
                )));

                break;
            }

            case ADVISED34: {

                Square w1 = new Square(SquareColor.WHITE);
                w1.setID(1);
                Square w2 = new Square(SquareColor.WHITE);
                w2.setID(2);
                Square y2 = new Square(SquareColor.YELLOW);
                y2.setID(7);
                Square r2 = new Square(SquareColor.RED);
                r2.setID(5);
                Square p1 = new Square(SquareColor.PURPLE);
                p1.setID(6);
                Square b2 = new Square(SquareColor.BLUE);
                b2.setID(8);
                Square b3 = new Square(SquareColor.BLUE);
                b3.setID(9);

                //white room

                w1.squareBuilder(r2, w2, Border.DOOR, Border.CORRIDOR);
                w2.squareBuilder(p1, yellowSS, Border.WALL, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        w1, w2
                )));

                //yellow room

                yellowSS.squareBuilder(y2, null, Border.CORRIDOR, null);
                //y2 is set by its neighbours

                mapWithRooms.add(new Room(Arrays.asList(
                        y2
                ), yellowSS));

                //red room

                redSS.squareBuilder(b2, r2, Border.DOOR, Border.CORRIDOR);
                r2.squareBuilder(b3, p1, Border.WALL, Border.CORRIDOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        r2
                ), redSS));

                //purple room

                p1.squareBuilder(blueSS, y2, Border.DOOR, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        p1
                )));

                //blue room

                b2.squareBuilder(null, b3, null, Border.CORRIDOR);
                b3.squareBuilder(null, blueSS, null, Border.CORRIDOR);
                //blueSS is set by its neighbours

                mapWithRooms.add(new Room(Arrays.asList(
                        b2, b3
                ), blueSS));

                break;
            }
            case ADVISED45: {

                Square w1 = new Square(SquareColor.WHITE);
                w1.setID(0);
                Square w2 = new Square(SquareColor.WHITE);
                w2.setID(1);
                Square y2 = new Square(SquareColor.YELLOW);
                y2.setID(2);
                Square y3 = new Square(SquareColor.YELLOW);
                y3.setID(6);
                Square y4 = new Square(SquareColor.YELLOW);
                y4.setID(7);
                Square r2 = new Square(SquareColor.RED);
                r2.setID(8);
                Square p1 = new Square(SquareColor.PURPLE);
                p1.setID(5);
                Square b2 = new Square(SquareColor.BLUE);
                b2.setID(9);
                Square g1 = new Square(SquareColor.GREEN);
                g1.setID(11);

                //white room

                w1.squareBuilder(redSS, w2, Border.DOOR, Border.CORRIDOR);
                w2.squareBuilder(p1, y2, Border.DOOR, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        w1, w2
                )));

                //yellow room

                y2.squareBuilder(y3, yellowSS, Border.CORRIDOR, Border.CORRIDOR);
                yellowSS.squareBuilder(y4, null, Border.CORRIDOR, null);
                y3.squareBuilder(blueSS, y4, Border.DOOR, Border.CORRIDOR);
                y4.squareBuilder(g1, null, Border.DOOR, null);

                mapWithRooms.add(new Room(Arrays.asList(
                        y2, y3, y4
                ), yellowSS));

                //red room

                redSS.squareBuilder(r2, p1, Border.CORRIDOR, Border.WALL);
                r2.squareBuilder(null, b2, null, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        r2
                ), redSS));

                //purple room

                p1.squareBuilder(b2, y3, Border.DOOR, Border.WALL);

                mapWithRooms.add(new Room(Arrays.asList(
                        p1
                )));

                //blue room

                b2.squareBuilder(null, blueSS, null, Border.CORRIDOR);
                blueSS.squareBuilder(null, g1, null, Border.DOOR);

                mapWithRooms.add(new Room(Arrays.asList(
                        b2
                ), blueSS));

                //green room

                //g1 is set by its neighbours

                mapWithRooms.add(new Room(Arrays.asList(
                        g1
                )));

                break;
            }

            default:
        }

        for (Room r : mapWithRooms) {
            for (Square s : r.getSquares()) {
                if (r.getSpawnSquare() != null) {
                    if (r.getSpawnSquare().getNorth() == null)
                        r.getSpawnSquare().setNorthBorder(Border.WALL);
                    if (r.getSpawnSquare().getSouth() == null)
                        r.getSpawnSquare().setSouthBorder(Border.WALL);
                    if (r.getSpawnSquare().getEast() == null)
                        r.getSpawnSquare().setEastBorder(Border.WALL);
                    if (r.getSpawnSquare().getWest() == null)
                        r.getSpawnSquare().setWestBorder(Border.WALL);
                }
                if (s.getNorth() == null)
                    s.setNorthBorder(Border.WALL);
                if (s.getSouth() == null)
                    s.setSouthBorder(Border.WALL);
                if (s.getEast() == null)
                    s.setEastBorder(Border.WALL);
                if (s.getWest() == null)
                    s.setWestBorder(Border.WALL);
            }
        }
        return mapWithRooms;
    }
}
