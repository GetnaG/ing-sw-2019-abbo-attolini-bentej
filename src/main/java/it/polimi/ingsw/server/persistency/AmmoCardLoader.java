package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.cards.AmmoCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Loads ammo cards from a json file.
 * The file must be located where specified by the {@link #FILE_NAME} constant.
 * <p>
 * The file must contain an array of {@link AmmoCard} as follows:
 * <pre> {@code [
 *  {
 *      "cubes": ["RED"],
 *      "powerup": false,
 *      "resourceId": "id"
 *  }, ...
 * ]}</pre>
 * Where {@code cubes} is followed by an array of
 * {@link it.polimi.ingsw.server.model.AmmoCube}s {@code "BLUE"},
 * {@code "RED"} or {@code "YELLOW"}; {@code powerup} indicates whether a
 * {@link it.polimi.ingsw.server.model.cards.PowerupCard} is
 * included; the {@code resourceId} is as requested by
 * {@link it.polimi.ingsw.server.model.cards.AbstractCard}.
 *
 * @author Abbo Giulio A.
 * @see AmmoCard
 * @see it.polimi.ingsw.server.model.AmmoCube
 * @see it.polimi.ingsw.server.model.cards.PowerupCard
 */
public class AmmoCardLoader implements BasicLoader<AmmoCard> {
    private static final String FILE_NAME = "./res/ammocards.json";

    AmmoCard[] ammoCards;

    AmmoCardLoader() {
        try {
            ammoCards = new Gson().fromJson(new FileReader(FILE_NAME),
                    AmmoCard[].class);
        } catch (FileNotFoundException e) {
            Logger.getLogger(AmmoCardLoader.class.getName())
                    .log(Level.WARNING, "Could not find: " + FILE_NAME +
                            "; no ammo card loaded!", e);
            ammoCards = new AmmoCard[]{};
        }
    }

    public static void main(String[] args) {
        new AmmoCardLoader();
    }

    @Override
    public AmmoCard get(String id) {
        List<AmmoCard> list = getAll();
        if (getAll().isEmpty())
            throw new NoSuchElementException("Can not find ammo card: " + id);
        return list.get(0);
    }

    @Override
    public List<AmmoCard> getAll() {
        return new ArrayList<>(Arrays.asList(ammoCards));
    }

    @Override
    public List<AmmoCard> getAll(String id) {
        return getAll().stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}
