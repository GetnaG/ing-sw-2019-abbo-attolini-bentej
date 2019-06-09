package it.polimi.ingsw.server.persistency;

import com.google.gson.Gson;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.controller.effects.MoveSelfEffect;
import it.polimi.ingsw.server.controller.effects.TagbackEffect;
import it.polimi.ingsw.server.controller.effects.cardeffects.CardEffect;

import java.io.InputStream;
import java.io.InputStreamReader;
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
     * This effects do not have an internal state, thus can be used multiple
     * times.
     */
    private List<EffectInterface> otherEffects;
    /**
     * The loaded effects (could be empty).
     */
    private CardEffect[] cardEffects;

    /**
     * This constructor loads the effects from a stream.
     * This also performs a check on the loaded elements.
     * Other effects are added.
     *
     * @param inputStream the stream for the input file
     */
    EffectLoader(InputStream inputStream) {
        /*Loading from file*/
        cardEffects = new Gson().fromJson(new InputStreamReader(inputStream),
                CardEffect[].class);

        /*Checking the cardEffects*/
        for (CardEffect effect : cardEffects)
            if (!effect.checkIntegrity())
                throw new WrongFileInputException("Card effects",
                        effect.getName());

        /*Adding the other effects*/
        otherEffects = new ArrayList<>();
        otherEffects.add(new MoveSelfEffect("cyberblade_shadowstep", 1));
        otherEffects.add(new MoveSelfEffect("rocketLauncher_rocketJump", 2));
        otherEffects.add(new MoveSelfEffect("plasmaGun_phaseGlide", 2));
        otherEffects.add(new MoveSelfEffect("teleporter", -1));
        otherEffects.add(new TagbackEffect("tagbackGrenade"));
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
                otherEffects.stream()
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
                otherEffects.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(id))
        ).collect(Collectors.toList());
    }
}
