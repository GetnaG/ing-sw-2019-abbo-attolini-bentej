package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.board.Room;
import it.polimi.ingsw.server.model.cards.PowerupCard;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Loads all maps from a json file.
 * <p>
 * The file must contain an array of arrays of {@link Room }. Each array of rooms
 * represents a configuration.
 *
 * @author Fahed B. Tej
 * @see Room
 */
public class MapLoader implements BasicLoader<List<Room>> {
    /**
     * The loaded configurations (could be empty).
     */
    private Room[][] maps;

    /**
     * This constructor loads the maps from a file.
     *
     * @param inputStream the stream for the input file
     * @throws IllegalArgumentException if {@code file} is incorrect
     */
    MapLoader(InputStream inputStream) {
        /*Loading the cards from file*/
        maps = new Gson().fromJson(new InputStreamReader(inputStream),
                Room[][].class);
    }

    /**
     * Returns the first object with a matching {@code id} ignoring case.
     *
     * @param id the identifier of the requested object
     * @return the first object found with the requested {@code id}
     * @throws NoSuchElementException if no match is found
     */
    @Override
    public List<Room> get(String id) {
        int index = Integer.parseInt(id);
        return Arrays.asList(maps[index]);
    }

    /**
     * Returns all the object loaded, already refreshed.
     *
     * @return all the object loaded
     */
    @Override
    public List<List<Room>> getAll() {
        List<Room[]> tmp = Arrays.asList(maps);
        List<List<Room>> maps = tmp.stream().map(array -> Arrays.asList(array)).collect(Collectors.toList());
        for (List<Room> rooms : maps) {
            rooms.get(0).refresh(rooms);
        }
        return maps;
    }

    /**
     * Returns all the objects with a matching {@code id} ignoring case.
     *
     * @param id the identifier of the requested objects
     * @return all the objects found with the requested {@code id}
     */
    @Override
    public List<List<Room>> getAll(String id) {
        List<List<Room>> ret = new ArrayList<>();
        ret.add(get(id));
        return ret;
    }
}
