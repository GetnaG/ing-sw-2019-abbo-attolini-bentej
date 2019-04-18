package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.cards.AmmoCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Loads ammo cards from a json file.
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
    /**
     * The loaded cards (could be empty).
     */
    private AmmoCard[] ammoCards;

    /**
     * This constructor loads the cards from a file.
     * The file must be located where specified by {@code file}.
     *
     * @param file the path, name and extension of the json file for ammo cards
     * @throws IllegalArgumentException if {@code file} is incorrect
     */
    AmmoCardLoader(String file) {
        try {
            ammoCards = new Gson().fromJson(new FileReader(file),
                    AmmoCard[].class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find: " + file, e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    @Override
    public synchronized AmmoCard get(String id) {
        List<AmmoCard> list = getAll(id);
        if (list.isEmpty())
            throw new NoSuchElementException("Can not find ammo card: " + id);
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<AmmoCard> getAll() {
        return new ArrayList<>(Arrays.asList(ammoCards));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<AmmoCard> getAll(String id) {
        return getAll().stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}
