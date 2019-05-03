package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.controller.effects.EffectIterator;
import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the instructions on how to run the card effects.
 * Objects of this class are instantiated by an
 * {@link it.polimi.ingsw.server.persistency.EffectLoader}.
 * <p>
 * A card effect should not be reused, care must be taken to duplicate an
 * effect every time it is needed and not to use the "original", this can be
 * done with the provided constructor. The effect loader handles this by
 * default.
 * <p>
 * With the word <i>target</i> it is meant every {@link Damageable} on which
 * the effect can be applied.
 *
 * @author Abbo Giulio A.
 * @see it.polimi.ingsw.server.persistency.EffectLoader
 * @see Player
 * @see Square
 */
public class CardEffect implements EffectInterface {
    /**
     * The name of this effect.
     */
    private String id;
    /**
     * The damage given to the selected availableTargets. Negative values are not
     * allowed.
     */
    private int damageAmount;
    /**
     * The marks given to the selected availableTargets. Negative values are not
     * allowed.
     */
    private int marksAmount;
    /**
     * The damage given to the other availableTargets in the square or the nearest one.
     * If only one target can be chosen, this damage is given to all the
     * availableTargets in his same square (or room), him included, otherwise it is
     * given to the target nearest to the player who is running the effect.
     * Negative values are not allowed.
     */
    private int secondaryDamage;
    /**
     * The marks given to the other availableTargets in the square or the nearest one.
     * If only one target can be chosen, this marks are given to all the
     * availableTargets in his same square (or room), him included, otherwise they are
     * given to the target nearest to the player who is running the effect.
     * Negative values are not allowed.
     */
    private int secondaryMarks;
    /**
     * The number of availableTargets that can be chosen.
     * If the minimum is -1, then all the possible availableTargets must be selected,
     * if the maximum is -1 then there is no upper limit, and the player can
     * choose the minimum allowed or more.
     * Other negative values are not allowed.
     */
    private Range targetsNumber;
    /**
     * The range of allowed distances of the availableTargets from the player.
     * A maximum distance of -1 means that there is no maximum distance.
     * Other negative values are not allowed.
     */
    private Range targetsDistance;
    /**
     * Filters the availableTargets based on previous availableTargets in this effects chain.
     *
     * @see HistoryPolicy
     */
    private HistoryPolicy historyPolicy;
    /**
     * Filters the availableTargets based on their visibility.
     *
     * @see TargetsPolicy
     */
    private TargetsPolicy targetsPolicy;
    /**
     * Filters the squares.
     *
     * @see SquaresPolicy
     */
    private SquaresPolicy squaresPolicy;
    /**
     * The maximum distance between a target and a square.
     */
    private int squaresDistance;
    /**
     * Particular behaviours of this effect.
     *
     * @see QuirkPolicy
     */
    private QuirkPolicy[] quirks;

    /**
     * The next effect in the chain.
     * This field is not part of the effect and depends on the card.
     */
    private EffectInterface decorated;
    /**
     * The list of available availableTargets.
     * This field is not part of the effect.
     */
    private List<Damageable> availableTargets;
    private List<Damageable> allTargets;
    /**
     * The list of available squares.
     * This field is not part of the effect.
     */

    private List<Square> destinations;
    /**
     * The player running this effect.
     * This field is not part of the effect.
     */
    private Player subject;
    /**
     * The players that have already been targeted in this chain of effects.
     * This field is not part of the effect.
     */
    private List<Damageable> alreadyTargeted;
    /**
     * The players that have already been damaged in this chain of effects.
     * This field is not part of the effect.
     */
    private List<Damageable> alreadyDamaged;
    private GameBoard board;

    /**
     * Creates a copy of the given card effect.
     *
     * @param copyOf the card effect to be copied.
     */
    public CardEffect(CardEffect copyOf) {
        id = copyOf.id;
        damageAmount = copyOf.damageAmount;
        marksAmount = copyOf.marksAmount;
        secondaryDamage = copyOf.secondaryDamage;
        secondaryMarks = copyOf.secondaryMarks;
        targetsNumber = new Range(copyOf.targetsNumber.getMin(),
                copyOf.targetsNumber.getMax());
        targetsDistance = new Range(copyOf.targetsDistance.getMin(),
                copyOf.targetsDistance.getMax());
        historyPolicy = copyOf.historyPolicy;
        targetsPolicy = copyOf.targetsPolicy;
        squaresPolicy = copyOf.squaresPolicy;
        squaresDistance = copyOf.squaresDistance;
        quirks = copyOf.quirks.clone();
        decorated = null;
        availableTargets = null;
        destinations = null;
        subject = null;
        alreadyTargeted = null;
        alreadyDamaged = null;
        board = null;
        allTargets = null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This will interact with the player to choose the availableTargets.
     */
    @Override
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board,
                          List<Damageable> allTargeted, List<Damageable> damageTargeted) {
        subject = subjectPlayer;
        alreadyTargeted = allTargeted;
        alreadyDamaged = damageTargeted;
        this.board = board;
        this.allTargets = allTargets;
        filterTargets();
        try {//FIXME: what to do if there are no availableTargets?
            selectTargets();
        } catch (AgainstRulesException e) {
            e.printStackTrace();
        }
        apply();
        allTargeted.addAll(availableTargets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EffectInterface getDecorated() {
        return decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToChain(EffectInterface last) {
        if (decorated == null)
            decorated = last;
        else
            decorated.addToChain(last);
    }

    /**
     * Returns an iterator over the elements of this chain of effects.
     *
     * @return an iterator over the elements of this chain of effects
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return new EffectIterator(this);
    }

    private void filterTargets() {
        availableTargets = targetsPolicy.getDamageable(subject, allTargets,
                alreadyTargeted.get(alreadyTargeted.size() - 1), board);

        availableTargets = availableTargets.stream().distinct()
                .filter(d -> !d.equals(subject))
                .filter(d -> checkTargetsDistanceAndDirection(subject, d))
                .filter(d -> !QuirkPolicy.MAX_TWO_HITS.isIn(quirks) ||
                        Collections.frequency(alreadyTargeted, d) < 2)
                .filter(d -> historyPolicy.filterTarget(d, alreadyTargeted, alreadyDamaged)).collect(Collectors.toList());
        destinations = squaresPolicy.getDestinations(subject, board,
                alreadyTargeted.get(alreadyTargeted.size() - 1));
        if (QuirkPolicy.ROOM.isIn(quirks) && destinations != null) {
            List<Square> others = new ArrayList<>();
            for (Square s : destinations)
                others.addAll(getSquaresSameRoom(s));
            destinations.addAll(others);
            destinations = destinations.stream().distinct()
                    .filter(s -> !(s.getRoom().equals(subject.getPosition().getRoom())))
                    .collect(Collectors.toList());
        }
        if (destinations != null) {
            destinations = destinations.stream()
                    .filter(s1 -> {
                        for (Damageable d1 : availableTargets)
                            if (checkSquaresDistanceAndDirection(s1, d1.getPosition()))
                                return true;
                        return false;
                    }).collect(Collectors.toList());
            availableTargets = availableTargets.stream()
                    .filter(d -> {
                        for (Square s : destinations)
                            if (checkSquaresDistanceAndDirection(s, d.getPosition()))
                                return true;
                        return false;
                    }).collect(Collectors.toList());
        }
    }

    private void selectTargets() throws AgainstRulesException {
        if (targetsNumber.hasMinimum() && availableTargets.size() > targetsNumber.getMin()) {
            chooseFrom(availableTargets, targetsNumber);
        } else if (availableTargets.size() < targetsNumber.getMin())
            throw new AgainstRulesException("Not enough availableTargets!");
        if (destinations != null) {
            destinations = destinations.stream()
                    .filter(s -> {
                        for (Damageable d : availableTargets)
                            if ((checkSquaresDistanceAndDirection(s,
                                    d.getPosition())))
                                return true;
                        return false;
                    }).collect(Collectors.toList());
            if (destinations.isEmpty())
                throw new AgainstRulesException("Not enough destinations!");
            if (destinations.size() > 1)
                chooseFrom(destinations);
        }
    }

    private void apply() {
        for (Damageable d : availableTargets) {
            addDamage(d, damageAmount);
            addMarks(d, marksAmount);
            squaresPolicy.apply(subject, d, destinations);
        }

        if (targetsNumber.isSingleValue()) {
            if (QuirkPolicy.ROOM.isIn(quirks)) {
                for (Square s :
                        getSquaresSameRoom(availableTargets.get(0).getPosition())) {
                    for (Damageable d : board.getPlayerInSquare(s, allTargets)) {
                        addDamage(d, secondaryDamage);
                        addMarks(d, secondaryMarks);
                    }
                }
            } else
                for (Damageable sameSquare :
                        board.getPlayerInSquare(availableTargets.get(0).getPosition(), allTargets)) {
                    if (!sameSquare.equals(subject)) {
                        addDamage(sameSquare, secondaryDamage);
                        addMarks(sameSquare, secondaryMarks);
                    }
                }
        } else {
            Damageable dam = getNearestTarget();
            addDamage(dam, damageAmount);
            addMarks(dam, marksAmount);
        }
        if (QuirkPolicy.MOVE_TO_TARGET.isIn(quirks))
            subject.setPosition(getFurthestTarget().getPosition());
    }

    private boolean checkTargetsDistanceAndDirection(Damageable a, Damageable b) {
        int dist = board.minimumDistance(a.getPosition(), b.getPosition(),
                QuirkPolicy.IGNORE_WALLS.isIn(quirks));
        boolean acceptableDistance = dist >= targetsDistance.getMin() &&
                (!targetsDistance.hasMaximum() || dist <= targetsDistance.getMax());
        if (QuirkPolicy.SINGLE_DIRECTION.isIn(quirks))
            return acceptableDistance &&
                    a.getPosition().straight(b.getPosition());
        return acceptableDistance;
    }

    private boolean checkSquaresDistanceAndDirection(Square a, Square b) {
        int dist = board.minimumDistance(a, b,
                QuirkPolicy.IGNORE_WALLS.isIn(quirks));
        boolean acceptableDistance = dist <= squaresDistance;
        if (QuirkPolicy.SINGLE_DIRECTION.isIn(quirks))
            return acceptableDistance && a.straight(b);
        return acceptableDistance;
    }

    private Damageable getNearestTarget() {
        return availableTargets.stream().min(Comparator.comparingInt(a ->
                board.minimumDistance(a.getPosition(), subject.getPosition(),
                        QuirkPolicy.IGNORE_WALLS.isIn(quirks))))
                .orElse(null);
    }

    private Damageable getFurthestTarget() {
        return availableTargets.stream().max(Comparator.comparingInt(a ->
                board.minimumDistance(a.getPosition(), subject.getPosition(),
                        QuirkPolicy.IGNORE_WALLS.isIn(quirks))))
                .orElse(null);
    }


    private List<Square> getSquaresSameRoom(Square s) {
        return board.getAllSquares().stream()
                .filter(x -> x.getRoom().equals(s.getRoom()))
                .collect(Collectors.toList());
    }

    private void chooseFrom(List<Damageable> l, Range amount) {
        List<List<Damageable>> choices = new ArrayList<>();
        int max = amount.getMax();
        if (max == -1)
            max = l.size();
        combinations(l, amount.getMin(), max, choices);
        choices = choices.stream().filter(c -> {
            if (QuirkPolicy.DIFFERENT_SQUARES.isIn(quirks) &&
                    c.stream().map(Damageable::getPosition)
                            .distinct().count() != c.size())
                return false;
            return !QuirkPolicy.SINGLE_DIRECTION.isIn(quirks) ||
                    c.size() != 2 ||
                    c.get(0).getPosition().straight(c.get(1).getPosition());
        }).collect(Collectors.toList());
        l.clear();
        l.addAll(subject.getToClient().chooseTarget(choices));
    }

    private void chooseFrom(List<Square> l) {
        Square chosen = subject.getToClient().chooseDestination(l);
        l.clear();
        l.add(chosen);
    }

    private void combinations(List<Damageable> elements, int min, int max,
                              List<? super List<Damageable>> collector) {
        if (elements.size() >= min && elements.size() <= max && !collector.contains(elements))
            collector.add(elements);//TODO check if duplicates are added
        if (elements.size() > min)
            for (Damageable d : elements) {
                List<Damageable> sublist = new ArrayList<>(elements);
                sublist.remove(d);
                combinations(sublist, min, max, collector);
            }
    }

    private void addDamage(Damageable damaged, int amount) {
        List<Player> damage = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            damage.add(subject);
        }
        damaged.giveDamage(damage);
    }

    private void addMarks(Damageable damaged, int amount) {
        List<Player> marks = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            marks.add(subject);
        }
        damaged.giveMark(marks);
    }

}