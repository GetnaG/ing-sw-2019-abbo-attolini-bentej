package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Effects should not be reused
 * <p>
 * * Objects of this class are instantiated by a
 * * {@link it.polimi.ingsw.server.persistency.PowerupLoader}.
 * <p>
 * ; {@code
 * squaresPolicy} can be: <ul>
 * <li>{@code VISIBLE}, which allows only visible squares
 * <li>{@code TO_SUBJECT}, which moves the target to the player running
 * the effect
 * <li>{@code TO_PREVIOUS}, which moves the target to the previous target
 * <li>{@code ALL}, which allows all the squares
 * <li>{@code VISIBLE_NOT_SELF} which allows all the squares but the player's one
 * <li>{@code SINGLE_DIRECTION}, which allows all the squares in the
 * cardinal directions from the player's square, including his own; this
 * can be used with zero to two targets
 * <li>{@code NONE}, if the squares are not relevant to the effect
 * </ul>{@code squaresDistance} is ; {@code quirks} can contain: <ul>
 *
 * <li>{@code DIFFERENT_SQUARES}, if the targets must be all on different
 * squares
 * <li>{@code IGNORE_WALLS}, if the walls must be ignored when counting
 * the moves
 * <li>{@code MOVE_TO_TARGET}, if the player running the effect must be
 * moved to the target
 * <li>{@code MAX_TWO_HITS}, which removes from the available targets
 * those targeted at least two times
 * <li>{@code ROOM}, which can be used with secondary damage and marks to
 * hit all the players in a room instead of a square
 * </ul>
 */
public class CardEffect implements EffectInterface {
    /**
     * The name of this effect.
     */
    private String id;
    /**
     * The damage given to the selected targets. Negative values are not
     * allowed.
     */
    private int damageAmount;
    /**
     * The marks given to the selected targets. Negative values are not
     * allowed.
     */
    private int marksAmount;
    /**
     * The damage given to the other targets in the square or the nearest one.
     * If only one target can be chosen, this damage is given to all the
     * targets in his same square (or room), him included, otherwise it is
     * given to the target nearest to the player who is running the effect.
     * Negative values are not allowed.
     */
    private int secondaryDamage;
    /**
     * The marks given to the other targets in the square or the nearest one.
     * If only one target can be chosen, this marks are given to all the
     * targets in his same square (or room), him included, otherwise they are
     * given to the target nearest to the player who is running the effect.
     * Negative values are not allowed.
     */
    private int secondaryMarks;
    /**
     * The number of targets that can be chosen.
     * If the minimum is -1, then all the possible targets must be selected,
     * if the maximum is -1 then there is no upper limit, and the player can
     * choose the minimum allowed or more.
     * Other negative values are not allowed.
     */
    private Range targetsNumber;
    /**
     * The range of allowed distances of the targets from the player.
     * A maximum distance of -1 means that there is no maximum distance.
     * Other negative values are not allowed.
     */
    private Range targetsDistance;
    /**
     * Filters the targets based on previous targets in this effects chain.
     *
     * @see HistoryPolicy
     */
    private CardEffect.HistoryPolicy historyPolicy;
    /**
     * Filters the targets based on their visibility.
     *
     * @see TargetsPolicy
     */
    private CardEffect.TargetsPolicy targetsPolicy;
    /**
     * Filters the squares.
     *
     * @see SquaresPolicy
     */
    private CardEffect.SquaresPolicy squaresPolicy;
    /**
     * The maximum distance between a target and a square.
     */
    private int squaresDistance;
    /**
     * Particular behaviours of this effect.
     *
     * @see QuirkPolicy
     */
    private CardEffect.QuirkPolicy[] quirks;
    /**
     * The next effect in the chain.
     * This field is not part of the effect and depends on the card.
     */
    private EffectInterface decorated;
    /**
     * The list of available targets.
     * This field is not part of the effect.
     */
    private List<Damageable> targets;
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
        targetsNumber = new Range(copyOf.targetsNumber.min,
                copyOf.targetsNumber.max);
        targetsDistance = new Range(copyOf.targetsDistance.min,
                copyOf.targetsDistance.max);
        historyPolicy = copyOf.historyPolicy;
        targetsPolicy = copyOf.targetsPolicy;
        squaresPolicy = copyOf.squaresPolicy;
        squaresDistance = copyOf.squaresDistance;
        quirks = copyOf.quirks.clone();
        decorated = null;
        targets = null;
        destinations = null;
        subject = null;
        alreadyTargeted = null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This will interact with the player to choose the targets.
     */
    @Override
    public void runEffect(Player subjectPlayer, GameBoard board,
                          List<Damageable> alreadyTargeted) {
        subject = subjectPlayer;
        this.alreadyTargeted = alreadyTargeted;
        filterTargets();
        try {//FIXME: what to do if there are no targets?
            selectTargets();
        } catch (AgainstRulesException e) {
            e.printStackTrace();
        }
        apply();
        alreadyTargeted.addAll(targets);
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
        decorated = last;
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
        switch (targetsPolicy) {
            case VISIBLE:
                targets = getVisibleTargetsBy(subject);
                break;
            case NOT_VISIBLE:
                targets = getNotVisibleTargets(subject);
                break;
            case VISIBLE_BY_PREVIOUS:
                targets = getVisibleTargetsBy(
                        alreadyTargeted.get(alreadyTargeted.size() - 1));
                break;
            case ALL:
            default:
                targets = getAllTargets();
                break;
        }
        targets = targets.stream().distinct()
                .filter(d -> !d.equals(subject))
                .filter(d -> checkTargetsDistanceAndDirection(subject, d))
                .filter(d -> !policiesContain(QuirkPolicy.MAX_TWO_HITS) ||
                        Collections.frequency(alreadyTargeted, d) < 2)
                .filter(d -> {
                    switch (historyPolicy) {
                        case NOT_TARGETED:
                            return !alreadyTargeted.contains(d);
                        case ONLY_TARGETED:
                            return alreadyTargeted.contains(d);
                        case ALL:
                        default:
                            return true;
                    }
                }).collect(Collectors.toList());
        switch (squaresPolicy) {
            case VISIBLE:
                destinations = getVisibleFrom(subject.getPosition());
                break;
            case VISIBLE_NOT_SELF:
                destinations = getVisibleFrom(subject.getPosition());
                destinations.remove(subject.getPosition());
                break;
            case TO_PREVIOUS:
                destinations = new ArrayList<>();
                destinations.add(alreadyTargeted
                        .get(alreadyTargeted.size() - 1).getPosition());
                break;
            case SINGLE_DIRECTION:
                destinations = getCardinalFrom(subject.getPosition());
                break;
            case ALL:
                destinations = getAllDestinations();
                break;
            case TO_SUBJECT:
            case NONE:
            default:
                destinations = null;
                break;
        }
        if (policiesContain(QuirkPolicy.ROOM)) {
            List<Square> others = new ArrayList<>();
            for (Square s : destinations)
                others.addAll(getSquaresSameRoom(s));
            destinations.addAll(others);
            destinations.stream().distinct()
                    .filter(s -> !sameRoom(s, subject.getPosition()));
        }
        if (destinations != null) {
            destinations = destinations.stream()
                    .filter(s1 -> {
                        for (Damageable d1 : targets)
                            if (checkSquaresDistanceAndDirection(s1, d1.getPosition()))
                                return true;
                        return false;
                    }).collect(Collectors.toList());
            targets = targets.stream()
                    .filter(d -> {
                        for (Square s : destinations)
                            if (checkSquaresDistanceAndDirection(s, d.getPosition()))
                                return true;
                        return false;
                    }).collect(Collectors.toList());
        }
    }

    private void selectTargets() throws AgainstRulesException {
        if (targetsNumber.min != -1 && targets.size() > targetsNumber.min) {
            chooseFrom(targets, targetsNumber);
        } else if (targets.size() < targetsNumber.min)
            throw new AgainstRulesException("Not enough targets!");
        if (destinations != null) {
            destinations = destinations.stream()
                    .filter(s -> {
                        for (Damageable d : targets)
                            if ((checkSquaresDistanceAndDirection(s,
                                    d.getPosition())))
                                return true;
                        return false;
                    }).collect(Collectors.toList());
            if (destinations.size() < 1)
                throw new AgainstRulesException("Not enough destinations!");
            if (destinations.size() > 1)
                chooseFrom(destinations);
        }
    }

    private void apply() {
        for (Damageable d : targets) {
            addDamage(d, damageAmount);
            addMarks(d, marksAmount);
            switch (squaresPolicy) {
                case TO_SUBJECT:
                    d.setPosition(subject.getPosition());
                    break;
                case ALL:
                case VISIBLE:
                case VISIBLE_NOT_SELF:
                case TO_PREVIOUS:
                case SINGLE_DIRECTION:
                case NONE:
                    if (!destinations.isEmpty())
                        d.setPosition(destinations.get(0));
                    break;
                default:
                    break;
            }
        }

        if (targetsNumber.min == 1 && targetsNumber.max == 1) {
            if (policiesContain(QuirkPolicy.ROOM)) {
                for (Square s :
                        getSquaresSameRoom(targets.get(0).getPosition())) {
                    for (Damageable d : getDamageableIn(s)) {
                        addDamage(d, secondaryDamage);
                        addMarks(d, secondaryMarks);
                    }
                }
            } else
                for (Damageable sameSquare :
                        getDamageableIn(targets.get(0).getPosition())) {
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
        if (policiesContain(QuirkPolicy.MOVE_TO_TARGET))
            subject.setPosition(getFurthestTarget().getPosition());
    }

    private boolean checkTargetsDistanceAndDirection(Damageable a, Damageable b) {
        int dist = distance(a.getPosition(), b.getPosition(),
                policiesContain(QuirkPolicy.IGNORE_WALLS));
        boolean acceptableDistance = dist >= targetsDistance.min &&
                (targetsDistance.max == -1 || dist <= targetsDistance.max);
        if (squaresPolicy == SquaresPolicy.SINGLE_DIRECTION)
            return acceptableDistance &&
                    sameDirection(a.getPosition(), b.getPosition());
        return acceptableDistance;
    }

    private boolean checkSquaresDistanceAndDirection(Square a, Square b) {
        int dist = distance(a, b, policiesContain(QuirkPolicy.IGNORE_WALLS));
        boolean acceptableDistance = dist <= squaresDistance;
        if (squaresPolicy == SquaresPolicy.SINGLE_DIRECTION)
            return acceptableDistance && sameDirection(a, b);
        return acceptableDistance;
    }

    private boolean policiesContain(QuirkPolicy policy) {
        for (QuirkPolicy p : quirks)
            if (p.equals(policy))
                return true;
        return false;
    }

    private Damageable getNearestTarget() {
        return targets.stream().min(Comparator.comparingInt(a ->
                distance(a.getPosition(), subject.getPosition(),
                        policiesContain(QuirkPolicy.IGNORE_WALLS))))
                .orElse(null);
    }

    private Damageable getFurthestTarget() {
        return targets.stream().max(Comparator.comparingInt(a ->
                distance(a.getPosition(), subject.getPosition(),
                        policiesContain(QuirkPolicy.IGNORE_WALLS))))
                .orElse(null);
    }

    private List<Damageable> getAllTargets() {
        List<Damageable> targets = new ArrayList<>();
        for (Square s : getAllDestinations())
            targets.addAll(getDamageableIn(s));
        return targets;
    }

    private List<Damageable> getVisibleTargetsBy(Damageable player) {
        List<Damageable> targets = new ArrayList<>();
        for (Square s : getVisibleFrom(player.getPosition()))
            targets.addAll(getDamageableIn(s));
        return targets;
    }

    private List<Damageable> getNotVisibleTargets(Damageable player) {
        List<Damageable> targets = getAllTargets();
        targets.removeAll(getVisibleTargetsBy(player));
        return targets;
    }

    private List<Square> getSquaresSameRoom(Square s) {
        return getAllDestinations().stream()
                .filter(x -> sameRoom(x, s)).collect(Collectors.toList());
    }

    private void chooseFrom(List<Damageable> l, CardEffect.Range amount) {
        List<List<Damageable>> choices = new ArrayList<>();
        if (amount.max == -1)
            amount.max = l.size();
        combinations(l, amount.min, amount.max, choices);
        choices = choices.stream().filter(c -> {
            if (policiesContain(QuirkPolicy.DIFFERENT_SQUARES) &&
                    c.stream().map(Damageable::getPosition)
                            .distinct().count() != c.size())
                return false;
            if (squaresPolicy == SquaresPolicy.SINGLE_DIRECTION &&
                    c.size() == 2 &&
                    !sameDirection(c.get(0).getPosition(),
                            c.get(1).getPosition()))
                return false;
            return true;
        }).collect(Collectors.toList());
        targets = subject.getToClient().chooseTarget(choices);
    }

    private void chooseFrom(List<Square> l) {
        Square chosen = subject.getToClient().chooseDestination(destinations);
        destinations.clear();
        destinations.add(chosen);
    }

    private void combinations(List<Damageable> elements, int min, int max,
                              List<List<Damageable>> collector) {
        if (elements.size() >= min && elements.size() <= max)
            if (!collector.contains(elements))
                collector.add(elements);
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

    private boolean sameDirection(Square a, Square b) {
        return true;
    }

    private List<Square> getCardinalFrom(Square position) {
        return new ArrayList<>();//FIXME with same square
    }

    private int distance(Square a, Square b, boolean ignoreWalls) {
        return 0;
    }

    private List<Damageable> getDamageableIn(Square s) {
        return new ArrayList<>();
    }

    private List<Square> getAllDestinations() {
        return new ArrayList<>();
    }

    private List<Square> getVisibleFrom(Square from) {
        return new ArrayList<>();
    }

    private boolean sameRoom(Square a, Square subjectPosition) {
        return false;
    }

    private enum QuirkPolicy {
        DIFFERENT_SQUARES, IGNORE_WALLS, MOVE_TO_TARGET, MAX_TWO_HITS, ROOM
    }

    private enum HistoryPolicy {
        ONLY_TARGETED, NOT_TARGETED, ALL
    }

    private enum TargetsPolicy {
        VISIBLE, NOT_VISIBLE, VISIBLE_BY_PREVIOUS, ALL
    }

    private enum SquaresPolicy {
        VISIBLE, TO_SUBJECT, TO_PREVIOUS, ALL, VISIBLE_NOT_SELF,
        SINGLE_DIRECTION, NONE//single direction max 0,2 target
    }

    private class Range {
        private int min;
        private int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}