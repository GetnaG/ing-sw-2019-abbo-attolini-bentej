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
        switch (targetsPolicy) {
            case VISIBLE:
                availableTargets = getVisibleTargetsBy(subject);
                break;
            case NOT_VISIBLE:
                availableTargets = getNotVisibleTargets(subject);
                break;
            case VISIBLE_BY_PREVIOUS:
                availableTargets = getVisibleTargetsBy(
                        alreadyTargeted.get(alreadyTargeted.size() - 1));
                break;
            case ALL:
            default:
                availableTargets = allTargets;
                break;
        }
        availableTargets = availableTargets.stream().distinct()
                .filter(d -> !d.equals(subject))
                .filter(d -> checkTargetsDistanceAndDirection(subject, d))
                .filter(d -> !policiesContain(QuirkPolicy.MAX_TWO_HITS) ||
                        Collections.frequency(alreadyTargeted, d) < 2)
                .filter(d -> historyPolicy.filterTarget(d, alreadyTargeted, alreadyDamaged)).collect(Collectors.toList());
        switch (squaresPolicy) {
            case VISIBLE:
                destinations = subject.getPosition().listOfVisibles(board);
                break;
            case VISIBLE_NOT_SELF:
                destinations = subject.getPosition().listOfVisibles(board);
                destinations.remove(subject.getPosition());
                break;
            case TO_PREVIOUS:
                destinations = new ArrayList<>();
                destinations.add(alreadyTargeted
                        .get(alreadyTargeted.size() - 1).getPosition());
                break;
            case SUBJECT_CARDINALS:
                destinations = subject.getPosition().getCardinals();
                break;
            case ALL:
                destinations = new ArrayList<>(board.getAllSquares());
                break;
            case TO_SUBJECT:
            case NONE:
            default:
                destinations = null;
                break;
        }
        if (policiesContain(QuirkPolicy.ROOM) && destinations != null) {
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
            if (destinations.size() < 1)
                throw new AgainstRulesException("Not enough destinations!");
            if (destinations.size() > 1)
                chooseFrom(destinations);
        }
    }

    private void apply() {
        for (Damageable d : availableTargets) {
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
                case SUBJECT_CARDINALS:
                case NONE:
                    if (!destinations.isEmpty())
                        d.setPosition(destinations.get(0));
                    break;
                default:
                    break;
            }
        }

        if (targetsNumber.isSingleValue()) {
            if (policiesContain(QuirkPolicy.ROOM)) {
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
        if (policiesContain(QuirkPolicy.MOVE_TO_TARGET))
            subject.setPosition(getFurthestTarget().getPosition());
    }

    private boolean checkTargetsDistanceAndDirection(Damageable a, Damageable b) {
        int dist = board.minimumDistance(a.getPosition(), b.getPosition(),
                policiesContain(QuirkPolicy.IGNORE_WALLS));
        boolean acceptableDistance = dist >= targetsDistance.getMin() &&
                (!targetsDistance.hasMaximum() || dist <= targetsDistance.getMax());
        if (policiesContain(QuirkPolicy.SINGLE_DIRECTION))
            return acceptableDistance &&
                    a.getPosition().straight(b.getPosition());
        return acceptableDistance;
    }

    private boolean checkSquaresDistanceAndDirection(Square a, Square b) {
        int dist = board.minimumDistance(a, b,
                policiesContain(QuirkPolicy.IGNORE_WALLS));
        boolean acceptableDistance = dist <= squaresDistance;
        if (policiesContain(QuirkPolicy.SINGLE_DIRECTION))
            return acceptableDistance && a.straight(b);
        return acceptableDistance;
    }

    private boolean policiesContain(QuirkPolicy policy) {
        for (QuirkPolicy p : quirks)
            if (p == policy)
                return true;
        return false;
    }

    private Damageable getNearestTarget() {
        return availableTargets.stream().min(Comparator.comparingInt(a ->
                board.minimumDistance(a.getPosition(), subject.getPosition(),
                        policiesContain(QuirkPolicy.IGNORE_WALLS))))
                .orElse(null);
    }

    private Damageable getFurthestTarget() {
        return availableTargets.stream().max(Comparator.comparingInt(a ->
                board.minimumDistance(a.getPosition(), subject.getPosition(),
                        policiesContain(QuirkPolicy.IGNORE_WALLS))))
                .orElse(null);
    }

    private List<Damageable> getVisibleTargetsBy(Damageable player) {
        List<Damageable> targets = new ArrayList<>();
        for (Square s : player.getPosition().listOfVisibles(board))
            targets.addAll(board.getPlayerInSquare(s, allTargets));
        return targets;
    }

    private List<Damageable> getNotVisibleTargets(Damageable player) {
        List<Damageable> targets = allTargets;
        targets.removeAll(getVisibleTargetsBy(player));
        return targets;
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
        combinations(l, amount.getMin(), amount.getMax(), choices);
        choices = choices.stream().filter(c -> {
            if (policiesContain(QuirkPolicy.DIFFERENT_SQUARES) &&
                    c.stream().map(Damageable::getPosition)
                            .distinct().count() != c.size())
                return false;
            if (policiesContain(QuirkPolicy.SINGLE_DIRECTION) &&
                    c.size() == 2 &&
                    !c.get(0).getPosition().straight(c.get(1).getPosition()))
                return false;
            return true;
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

    /**
     * Quirks specify a particular behaviour of the effect.
     */
    private enum QuirkPolicy {
        /**
         * The availableTargets must be all on different squares.
         */
        DIFFERENT_SQUARES,
        /**
         * The wall must be ignored when counting moves.
         */
        IGNORE_WALLS,
        /**
         * The subject is moved to the furthest target affected.
         */
        MOVE_TO_TARGET,
        /**
         * Targets which have already been targeted at least two times in
         * this chain are not valid availableTargets.
         */
        MAX_TWO_HITS,
        /**
         * Can be used with secondary damage and marks to hit all the players
         * in a room instead of a square.
         */
        ROOM,
        /**
         * Restricts movement to a single cardinal direction.
         * This can be used with zero to two availableTargets.
         */
        SINGLE_DIRECTION
    }

    /**
     * Filters the availableTargets based on their visibility.
     */
    private enum TargetsPolicy {
        /**
         * Only availableTargets visible by the subject.
         */
        VISIBLE,
        /**
         * Only availableTargets not visible by the subject.
         */
        NOT_VISIBLE,
        /**
         * Only availableTargets visible by the last targeted in the chain.
         */
        VISIBLE_BY_PREVIOUS,
        /**
         * All the availableTargets are allowed.
         */
        ALL
    }

    /**
     * Filters the squares and defines the movement.
     */
    private enum SquaresPolicy {
        /**
         * Only squares visible by the subject.
         */
        VISIBLE,
        /**
         * Moves the availableTargets to the subject.
         */
        TO_SUBJECT,
        /**
         * Moves the availableTargets to the last target in the chain.
         */
        TO_PREVIOUS,
        /**
         * All squares are allowed.
         */
        ALL,
        /**
         * Only squares visible by the subject except the subject's.
         */
        VISIBLE_NOT_SELF,
        /**
         * All the squares in the cardinal directions from the player's
         * square are allowed, including his own.
         */
        SUBJECT_CARDINALS,
        /**
         * The squares are not relevant in this effect.
         */
        NONE
    }

}