package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.persistency.FromFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This represents a weapon card, with a cost and sequences of effects.
 * Objects of this class are instantiated by a
 * {@link it.polimi.ingsw.server.persistency.WeaponLoader}.
 * <p>
 * The field {@linkplain #effectIdSequences} represents all the
 * possible sequences of
 * {@link it.polimi.ingsw.server.controller.effects.EffectInterface} that
 * this card allows, this must be written explicitly if
 * {@linkplain #onlySpecifiedOrder} is true, otherwise all the permutation of
 * the elements are allowed. In this last case, only the first elements of
 * the arrays are considered, the first of which must be the base method.
 * For example, given the effects BASE, A, B, then the possible sequences
 * will be<ul>
 * <li>BASE</li>
 * <li>BASE, A</li>
 * <li>BASE, B</li>
 * <li>A, BASE</li>
 * <li>B, BASE</li>
 * <li>and then all the permutation of BASE, A, B</li></ul>
 *
 * @author giubots
 * @see it.polimi.ingsw.server.persistency.WeaponLoader
 * @see AmmoCube
 * @see it.polimi.ingsw.server.controller.effects.EffectInterface
 */
public class WeaponCard extends AbstractCard {
    /**
     * The {@linkplain AmmoCube}s that must be payed to reload this weapon.
     */
    private AmmoCube[] cost;

    /**
     * All the possible sequences of effects for this weapon.
     */
    private String[][] effectIdSequences;

    /**
     * Whether the effects can be used only in the specified order.
     * <p>
     * If true, then the possible sequences are as set in
     * {@linkplain #effectIdSequences}; if false then only the starting
     * elements are taken into account and the sequences consist in all the
     * possible combinations.
     */
    private boolean onlySpecifiedOrder;

    /**
     * Constructor used for testing, effects are initialized to [["Test"]].
     *
     * @param id                 the id to locate the resources for this object
     * @param cost               the cubes that must be payed to reload this
     *                           weapon
     * @param effectSequences    all the possible sequences of effects for this
     * @param onlySpecifiedOrder if true the effects can be used only in the
     *                           specified order; else only the first
     *                           elements of {@code effectSequences} are
     *                           considered, and can be used in every
     *                           combination possible.
     */
    public WeaponCard(String id, List<AmmoCube> cost,
                      String[][] effectSequences, boolean onlySpecifiedOrder) {
        super(id);
        this.cost = cost.toArray(new AmmoCube[0]);
        this.onlySpecifiedOrder = onlySpecifiedOrder;
        effectIdSequences = effectSequences;
    }

    /**
     * Returns a list of all the possible sequences of effects that this card
     * allows.
     * <p>
     * The returned list will contain all the possible starting points for
     * each of the effect sequences, and these elements contain a link to the
     * next element of the sequence.
     * For example, if the possible sequences are ABC, BBB, ABA, then the lis
     * will contain A, B, A (this one with different links from the first A).
     *
     * @return a list of all the possible sequences of effect allowed
     */
    public List<Action> getPossibleSequences() {
        return Arrays.stream(effectIdSequences).map(effectIdSequence ->
                new Action("", Arrays.stream(effectIdSequence).map(s ->
                        FromFile.effects().get(s)).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    /**
     * If the order is not fixed, this calculates all the possible permutations.
     */
    public void runPermutation() {
        if (!onlySpecifiedOrder) {
            List<List<String>> permutationsList = new ArrayList<>();

            /*Just the basic effect*/
            permutationsList.add(Collections.singletonList(effectIdSequences[0][0]));

            /*Basic and first optional*/
            if (effectIdSequences.length >= 2)
                permutations(Arrays.asList(effectIdSequences[0][0], effectIdSequences[1][0]),
                        new ArrayList<>(), permutationsList);

            /*Basic and second optional*/
            if (effectIdSequences.length >= 3)
                permutations(Arrays.asList(effectIdSequences[0][0], effectIdSequences[2][0]),
                        new ArrayList<>(), permutationsList);

            /*All the effects*/
            List<String> elements = new ArrayList<>();
            for (String[] s : effectIdSequences)
                elements.add(s[0]);
            permutations(elements, new ArrayList<>(), permutationsList);

            /*Storing the permutations and ensuring that this method does not run again*/
            effectIdSequences = permutationsList.stream().map(e -> e.toArray(new String[0])).toArray(String[][]::new);
            onlySpecifiedOrder = true;
        }
    }

    /**
     * Returns the cubes that must be payed to reload this weapon.
     *
     * @return the cubes that must be payed to reload this weapon
     */
    public List<AmmoCube> getCost() {
        return new ArrayList<>(Arrays.asList(cost));
    }

    /**
     * Returns all the different permutations of the provided elements.
     *
     * @param elements  the elements of the sequence
     * @param temp      where the sequence is temporarily stored
     * @param collector where all the permutations will be stored
     */
    private void permutations(List<String> elements, List<String> temp, List<? super List<String>> collector) {
        if (elements.isEmpty()) {
            collector.add(temp);
        } else {
            for (String element : elements) {
                List<String> myTemp = new ArrayList<>(temp);
                myTemp.add(element);
                List<String> myElements = new ArrayList<>(elements);
                myElements.remove(element);
                permutations(myElements, myTemp, collector);
            }
        }
    }
}