package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.cards.PowerupCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Loads powerup cards from a json file.
 * <p>
 * The file must contain an array of {@link PowerupCard} as follows:
 * <pre> {@code [
 *    {
 *     "usableAsAction": false,
 *     "usableOnDealingDamage": false,
 *     "usableOnReceivingDamage": true,
 *     "effectId": "tagback grenade",
 *     "cube": "BLUE",
 *     "resourceId": "AD_powerups_IT_022"
 *   }, ...
 * ]}</pre>
 * For info on the meaning and the values allowed, refer to
 * the fields of {@link PowerupCard}.
 * The {@code resourceId} is as requested by
 * {@link it.polimi.ingsw.server.model.cards.AbstractCard}.
 *
 * @author Abbo Giulio A.
 * @see PowerupCard
 */
public class PowerupLoader implements BasicLoader<PowerupCard> {
    /**
     * The loaded cards (could be empty).
     */
    private PowerupCard[] powerupCards;

    /**
     * This constructor loads the cards from a file.
     * The file must be located where specified by {@code file}.
     * This also checks that the specified effect exists.
     *
     * @param file the path, name and extension of the json file for powerup
     *             cards
     * @throws IllegalArgumentException if {@code file} is incorrect
     */
    PowerupLoader(String file) {
        /*Loading the cards from file*/
        try {
            powerupCards = new Gson().fromJson(new FileReader(file),
                    PowerupCard[].class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find: " + file, e);
        }

        /*Checking if all effects are present*/
        for (PowerupCard card : powerupCards)
            try {
                card.getEffect();
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
    public synchronized PowerupCard get(String id) {
        List<PowerupCard> list = getAll(id);
        if (list.isEmpty())
            throw new NoSuchElementException("Can not find powerup card: " + id);
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<PowerupCard> getAll() {
        return new ArrayList<>(Arrays.asList(powerupCards));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<PowerupCard> getAll(String id) {
        return getAll().stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}
