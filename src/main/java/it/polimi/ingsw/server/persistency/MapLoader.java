package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.board.Room;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
     * {@inheritDoc}
     * <p>
     * Returns the configuration with a matching {@code id}, refreshed.
     */
    @Override
    public List<Room> get(String id) {
        List<Room> rooms = Arrays.stream(maps[Integer.parseInt(id)])
                .map(Room::new).collect(Collectors.toList());
        Room.refresh(rooms);
        return rooms;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns all the object loaded, already refreshed.
     */
    @Override
    public List<List<Room>> getAll() {
        List<List<Room>> mapsList = Arrays.stream(maps)
                .map(a -> Arrays.stream(a).map(Room::new).collect(Collectors.toList()))
                .collect(Collectors.toList());
        for (List<Room> rooms : mapsList) {
            Room.refresh(rooms);
        }
        return mapsList;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The list will contain only one element because the id is unique.
     * The squares will be already refreshed.
     */
    @Override
    public List<List<Room>> getAll(String id) {
        return Collections.singletonList(get(id));
    }
}
