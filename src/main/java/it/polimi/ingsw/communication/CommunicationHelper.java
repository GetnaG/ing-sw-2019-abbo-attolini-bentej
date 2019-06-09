package it.polimi.ingsw.communication;

import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for translating objects into list of choices (strings).
 * The strings are the resource keys that will be used by the client.
 *
 * @author Abbo Giulio A.
 */
public class CommunicationHelper {
    /**
     * Handles the interaction using {@linkplain EffectInterface}.
     * Each choice could be a chain of effects; this displays the names
     * of the elements in the chaim.
     *
     * @param options the options to choose from
     * @return the options as lists of sequences of keys
     */
    public List<List<String>> askEffect(List<? extends EffectInterface> options) {

        /*Creating a list of names for each chain of effects*/
        List<List<String>> names = new ArrayList<>();
        for (EffectInterface effect : options) {
            List<String> chain = new ArrayList<>();
            effect.forEach(el -> chain.add(el.getName()));
            names.add(chain);
        }
        return names;
    }

    /**
     * Handles the interaction using {@linkplain PowerupCard}.
     *
     * @param options the options to choose from
     * @return the options as lists of sequences of keys
     */
    public List<List<String>> askPowerup(List<? extends PowerupCard> options) {
        return options.stream()
                .map(PowerupCard::getId).map(Arrays::asList)
                .collect(Collectors.toList());
    }

    /**
     * Handles the interaction using {@linkplain Square}.
     *
     * @param options the options to choose from
     * @return the options as lists of sequences of keys
     */
    public List<List<String>> askSquare(List<? extends Square> options) {
        return options.stream()
                .map(Square::getID).map(Arrays::asList)
                .collect(Collectors.toList());
    }

    /**
     * Handles the interaction using {@linkplain WeaponCard}.
     *
     * @param options the options to choose from
     * @return the options as lists of sequences of keys
     */
    public List<List<String>> askWeapon(List<? extends WeaponCard> options) {
        return options.stream()
                .map(WeaponCard::getId).map(Arrays::asList)
                .collect(Collectors.toList());
    }

    /**
     * Handles the interaction using {@linkplain Action}.
     * This displays only the name of the action, not the names of the single
     * effects in the chain.
     *
     * @param options the options to choose from
     * @return the options as lists of sequences of keys
     */
    public List<List<String>> askAction(List<? extends Action> options) {
        return options.stream()
                .map(Action::getName).map(Arrays::asList)
                .collect(Collectors.toList());
    }

    /**
     * Handles the interaction using lists of {@linkplain Damageable}.
     *
     * @param options the options to choose from
     * @return the options as lists of sequences of keys
     */
    public List<List<String>> askDamageableList(List<?
            extends List<Damageable>> options) {
        return options.stream()
                .map(o -> o.stream()
                        .map(Damageable::getName)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
