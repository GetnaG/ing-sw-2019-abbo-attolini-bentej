package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.controller.effects.Move;
import it.polimi.ingsw.server.controller.effects.cardeffects.CardEffect;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loads card effects from a json file.
 * <p>
 * The file must contain an array of {@link CardEffect} as follows:
 * <pre> {@code [
 *  {
 *     "id": "name",
 *     "damageAmount": 0,
 *     "marksAmount": 0,
 *     "secondaryDamage": 0,
 *     "secondaryMarks": 0,
 *     "targetsNumber": {"min": 0, "max": -1},
 *     "targetsDistance": {"min": 0, "max": -1},
 *     "historyPolicy": "ALL",
 *     "targetsPolicy": "ALL",
 *     "squaresPolicy": "NONE",
 *     "squaresDistance": 0,
 *     "quirks": []
 *   }, ...
 * ]}</pre>
 * All missing numeric fields, excluded {@code targetsNumber} and {@code
 * targetsDistance}, are considered as 0.
 * For info on the meaning and the values allowed, refer to
 * the fields of {@link CardEffect}.
 *
 * @author Abbo Giulio A.
 * @see CardEffect
 */
public class EffectLoader implements BasicLoader<EffectInterface> {
    /**
     * The effects not present in the json.
     */
    private List<EffectInterface> moveSelfEffects;
    /**
     * The loaded effects (could be empty).
     */
    private CardEffect[] cardEffects;

    /**
     * This constructor loads the effects from a file.
     * The file must be located where specified by {@code file}.
     * This also performs a check on the loaded elements.
     * Other effects are added.
     *
     * @param file the path, name and extension of the json file for effects
     * @throws IllegalArgumentException if {@code file} is incorrect
     */
    EffectLoader(String file) {
        /*Loading from file*/
        try {
            cardEffects = new Gson().fromJson(new FileReader(file),
                    CardEffect[].class);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find: " + file, e);
        }

        /*Checking the cardEffects*/
        for (CardEffect effect : cardEffects)
            if (!effect.checkIntegrity())
                throw new WrongFileInputException(file, effect.getName());

        /*Adding the other effects*/
        moveSelfEffects = new ArrayList<>();
        moveSelfEffects.add(new Action("cyberblade_shadowstep", new Move()));
        moveSelfEffects.add(new Action("rocketLauncher_rocketJump",
                Arrays.asList(new Move(), new Move())));
        moveSelfEffects.add(new Action("plasmaGun_phaseGlide",
                Arrays.asList(new Move(), new Move())));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This returns a copy of the effect.
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    @Override
    public synchronized EffectInterface get(String id) {
        List<EffectInterface> list = getAll(id);
        if (list.isEmpty())
            throw new NoSuchElementException("Can not find effect: " + id);
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This returns a copy of the effects.
     */
    @Override
    public synchronized List<EffectInterface> getAll() {
        return Stream.concat(Arrays.stream(cardEffects)
                        .map(CardEffect::new),
                moveSelfEffects.stream()
        ).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * This returns a copy of the effects.
     */
    @Override
    public synchronized List<EffectInterface> getAll(String id) {
        return Stream.concat(Arrays.stream(cardEffects)
                        .filter(c -> c.getName().equalsIgnoreCase(id))
                        .map(CardEffect::new),
                moveSelfEffects.stream()
        ).collect(Collectors.toList());
    }
}
