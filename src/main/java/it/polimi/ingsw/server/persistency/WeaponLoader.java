package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Loads weapon cards from a json file.
 * <p>
 * The file must contain an array of {@link WeaponCard} as follows:
 * <pre> {@code [
 *   {
 *     "cost": ["YELLOW"],
 *     "effectIdSequences": [
 *       ["sledgehammer"],
 *       ["pulverize"]
 *     ],
 *     "onlySpecifiedOrder": true,
 *     "resourceId": "AD_weapons_IT_022"
 *   }, ...
 * ]}</pre>
 * For info on the meaning and the values allowed, refer to
 * the fields of {@link WeaponCard}.
 * The {@code resourceId} is as requested by
 * {@link it.polimi.ingsw.server.model.cards.AbstractCard}.
 *
 * @author Abbo Giulio A.
 * @see WeaponCard
 */
public class WeaponLoader implements BasicLoader<WeaponCard> {
    /**
     * The loaded cards (could be empty).
     */
    private WeaponCard[] weaponCards;

    /**
     * This constructor loads the cards from a file.
     * The file must be located where specified by {@code file}.
     *
     * @param file the path, name and extension of the json file for powerup
     *             cards
     * @throws IllegalArgumentException if {@code file} is incorrect
     */
    WeaponLoader(String file) {
        try {
            weaponCards = new Gson().fromJson(new FileReader(file),
                    WeaponCard[].class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find: " + file, e);
        }

        /*Checking if all effects are present*/
        for (WeaponCard card : weaponCards)
            try {
                card.getPossibleSequences();
            } catch (NoSuchElementException e) {
                throw new WrongFileInputException(file, card.getId(), e);
            }
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    @Override
    public synchronized WeaponCard get(String id) {
        List<WeaponCard> list = getAll(id);
        if (list.isEmpty())
            throw new NoSuchElementException("Can not find weapon card: " + id);
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<WeaponCard> getAll() {
        return new ArrayList<>(Arrays.asList(weaponCards));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<WeaponCard> getAll(String id) {
        return getAll().stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}
