package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.persistency.FromFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a map using the configuration given
 */
public class ConfigurationHelper {


    private static List<List<Room>> maps;


    public static List<Room> boardCreator(Configurations c) {
        maps = FromFile.maps().getAll();
        List<Room> board = maps.get(c.getId());
        return board;
    }

    public static void setReplacer(ReplaceListener replacer) {

    }
}
