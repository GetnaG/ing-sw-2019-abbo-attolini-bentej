package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationHelper {
    public List<Room> loadRooms(){
    //Creating the squares
    Square sq1 = new Square(SquareColor.GREEN, null);
    Square sq2 = new Square(SquareColor.GREEN, null);
    Square sq3 = new Square(SquareColor.GREEN, null);
    SpawnSquare sq4 = new SpawnSquare(AmmoCube.YELLOW, getWeaponMarketWithCards());
    SpawnSquare sq5 = new SpawnSquare(AmmoCube.RED, getWeaponMarketWithCards());
    Square sq6 = new Square(SquareColor.PURPLE, null);
    Square sq7 = new Square(SquareColor.PURPLE, null);
    Square sq8 = new Square(SquareColor.YELLOW, null);
    Square sq9 = new Square(SquareColor.RED, null);
    Square sq10 = new Square(SquareColor.BLUE, null);
    SpawnSquare sq11 = new SpawnSquare(AmmoCube.BLUE, getWeaponMarketWithCards());

        /*Setting connections and doors
            9    <>   10   :   11
            :         <>       <>
            5     |   6    :   7    <>   8
            <>        <>       |         :
            1     :   2     :  3    <>  4
         */

    List<Square> neighbours = new ArrayList<>();
    List<Border> borderNeighbours = new ArrayList<>();
        neighbours.add(sq5);
        neighbours.add(sq2);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.CORRIDOR);

    setUpSquare(sq1, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq6);
        neighbours.add(sq3);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.CORRIDOR);

    setUpSquare(sq2, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq7);
        neighbours.add(sq4);
        borderNeighbours.add(Border.WALL);
        borderNeighbours.add(Border.DOOR);

    setUpSquare(sq3, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq8);
        borderNeighbours.add(Border.CORRIDOR);

    setUpSquare(sq4, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq9);
        neighbours.add(sq6);
        borderNeighbours.add(Border.CORRIDOR);
        borderNeighbours.add(Border.WALL);

    setUpSquare(sq5, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq10);
        neighbours.add(sq7);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.CORRIDOR);

    setUpSquare(sq6, neighbours, borderNeighbours);

        neighbours.removeAll(neighbours);
        borderNeighbours.removeAll(borderNeighbours);
        neighbours.add(sq11);
        neighbours.add(sq8);
        borderNeighbours.add(Border.DOOR);
        borderNeighbours.add(Border.DOOR);

    setUpSquare(sq7, neighbours, borderNeighbours);

    setNeighboor(sq9, sq10, Border.DOOR,2);

    setNeighboor(sq10, sq11, Border.DOOR,2);

    // Creating rooms

    List<Square> roomSquares = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();

    // Green room
        roomSquares.add(sq1);
        roomSquares.add(sq2);
        roomSquares.add(sq3);
        rooms.add(new

    Room(roomSquares));
    // Yellow Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq4);
        roomSquares.add(sq8);
        rooms.add(new

    Room(roomSquares, sq4));
    // Purple Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq6);
        roomSquares.add(sq7);
        rooms.add(new

    Room(roomSquares));
    //Red Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq5);
        roomSquares.add(sq9);
        rooms.add(new

    Room(roomSquares, sq5));
    // Blue Room
        roomSquares.removeAll(roomSquares);
        roomSquares.add(sq10);
        roomSquares.add(sq11);
        rooms.add(new

    Room(roomSquares, sq11));

        return rooms;
}


    private void setUpSquare(Square square, List<Square> neighbours, List<Border> borderNeighbours){
        for (Square n : neighbours){
            int orientation = neighbours.indexOf(n);
            setNeighboor(square, n, borderNeighbours.get(orientation),orientation);
        }
    }

    private void setNeighboor(Square s1, Square s2, Border borderType, int orientation){
        if (s1 == null || s2 == null)
            return;

        switch (orientation){
            case 1:
                // North
                s1.setNorthBorder(borderType);
                s2.setSouthBorder(borderType);
                s1.setNorth(s2);
                s2.setSouth(s1);
            case 2:
                // East
                s1.setEastBorder(borderType);
                s2.setWestBorder(borderType);
                s1.setEast(s2);
                s2.setWest(s1);
            case 3:
                // South
                s1.setSouthBorder(borderType);
                s2.setNorthBorder(borderType);
                s1.setSouth(s2);
                s2.setNorth(s1);
            case 4:
                s1.setWestBorder(borderType);
                s2.setEastBorder(borderType);
                s1.setWest(s2);
                s2.setEast(s1);

        }
    }

    private WeaponMarket getWeaponMarketWithCards(){
        List<WeaponCard> cards  = new ArrayList<>();
        /*try {
            cards.add(board.getWeaponCard());
            cards.add(board.getWeaponCard());
            cards.add(board.getWeaponCard());
        }catch (AgainstRulesException e) {
            // TODO Handle exception
        }*/

        return new WeaponMarket(cards);
    }

/*

    private Configurations c;
    private List<Square> map;
    private List<Room> mapWithRooms;

    public ConfigurationHelper(Configurations c) {
            this.c = c;
            this.map = new ArrayList<>();
            this.mapWithRooms = new ArrayList<>();
    }

    public List<Room> boardCreator(){

            int size = 12;

        for (int i = 0; i < size; i++) {
            map.add(new Square());
            map.get(i).setID(i);
        }
        
        List<SpawnSquare> spawnPoints = new ArrayList<>();
        
        spawnPoints.add(new SpawnSquare(AmmoCube.BLUE, null));
        spawnPoints.add(new SpawnSquare(AmmoCube.YELLOW, null));
        spawnPoints.add(new SpawnSquare(AmmoCube.RED, null));

        List<Square> temp = new ArrayList<>();

        switch (c){
                case STANDARD1:{
                    map.set(10, spawnPoints.get(0));
                    map.set(3, spawnPoints.get(1));
                    map.set(4, spawnPoints.get(2));

                    //white room

                    this.squareBuilder(0, SquareColor.WHITE, map.get(4), map.get(1), Border.DOOR, Border.CORRIDOR);
                    temp.add(map.get(0));
                    this.squareBuilder(1, SquareColor.WHITE, map.get(5), map.get(2), Border.DOOR, Border.CORRIDOR);
                    temp.add(map.get(1));
                    this.squareBuilder(2, SquareColor.WHITE, map.get(6), map.get(3), Border.WALL, Border.DOOR);
                    temp.add(map.get(2));

                    mapWithRooms.add(0, new Room(Arrays.asList(
                            map.get(0), map.get(1) , map.get(2)
                    )));
                    //temp.clear();

                    //yellow room

                    this.squareBuilder(3, null, map.get(7), null, Border.CORRIDOR, null);
                    temp.add(map.get(3));
                    this.squareBuilder(7, SquareColor.YELLOW, null, null, null, null);
                    temp.add(map.get(7));

                    mapWithRooms.add(1, new Room(temp, spawnPoints.get(1)));
                    //temp.clear();

                    //red room

                    this.squareBuilder(4, null, map.get(8), map.get(5), Border.CORRIDOR, Border.WALL);
                    temp.add(map.get(4));
                    this.squareBuilder(8, SquareColor.RED, null, map.get(9), null, Border.DOOR );
                    temp.add(map.get(8));

                    mapWithRooms.add(2, new Room(temp, spawnPoints.get(2)));
                    temp.clear();

                    //purple room

                    this.squareBuilder(5, SquareColor.PURPLE, map.get(9), map.get(6), Border.DOOR, Border.CORRIDOR);
                    temp.add(map.get(5));
                    this.squareBuilder(6, SquareColor.PURPLE, map.get(10), map.get(7), Border.DOOR, Border.DOOR);
                    temp.add(map.get(6));

                    mapWithRooms.add(3, new Room(temp));
                    temp.clear();

                    //blue room

                    this.squareBuilder(9, SquareColor.BLUE, null, map.get(10), null, Border.CORRIDOR);
                    temp.add(map.get(9));

                    temp.add(map.get(10));

                    mapWithRooms.add(4, new Room(temp, spawnPoints.get(2)));
                    temp.clear();
                    
                    break;
                }

                case STANDARD2: break;
                case ADVISED34: break;
                case ADVISED45: break;
                default:
            }

        for(Square s: map){
            if( s.getNorth() == null )
                s.setNorthBorder(Border.WALL);
            if( s.getSouth() == null )
                s.setSouthBorder(Border.WALL);
            if( s.getEast() == null )
                s.setEastBorder(Border.WALL);
            if( s.getWest() == null )
                s.setWestBorder(Border.WALL);
        }

            return mapWithRooms;


    }

    private void squareBuilder(int position, SquareColor c, Square n, Square e, Border nb, Border eb){
        if(c != null) map.get(position).setSquareColor(c);
        if(n != null) map.get(position).setNorth(n);
        if(e != null) map.get(position).setEast(e);
        if(nb != null) map.get(position).setNorthBorder(nb);
        if(eb != null) map.get(position).setEastBorder(eb);
    }
*/
}
