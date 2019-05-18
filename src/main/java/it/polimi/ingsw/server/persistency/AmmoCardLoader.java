package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.cards.AmmoCard;

import java.io.InputStream;
import java.io.InputStreamReader;
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
 * For info on the meaning and the values allowed, refer to
 * the fields of {@link AmmoCard}.
 * The {@code resourceId} is as requested by
 * {@link it.polimi.ingsw.server.model.cards.AbstractCard}.
 *
 * @author Abbo Giulio A.
 * @see AmmoCard
 */
public class AmmoCardLoader implements BasicLoader<AmmoCard> {
    /**
     * The loaded cards (could be empty).
     */
    private AmmoCard[] ammoCards;

    /**
     * This constructor loads the cards from a stream.
     *
     * @param inputStream the stream for the input file
     */
    AmmoCardLoader(InputStream inputStream) {
        ammoCards = new Gson().fromJson(new InputStreamReader(inputStream),
                AmmoCard[].class);
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
