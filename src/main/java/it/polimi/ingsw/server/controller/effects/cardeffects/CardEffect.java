package it.polimi.ingsw.server.controller.effects.cardeffects;

import it.polimi.ingsw.communication.ToClientException;
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
     * The damage given to the selected targets. Negative values are not
     * allowed.
     */
    private int damageAmount;
    /**
     * The marks given to the selected targets. Negative values are not allowed.
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
    private HistoryPolicy historyPolicy;
    /**
     * Filters the targets based on their visibility.
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
     * This field is not part of the effect.
     */
    private EffectInterface decorated;
    /**
     * The list of available targets.
     * This field is not part of the effect.
     */
    private Set<Damageable> availableTargets;
    /**
     * All the targets on the board.
     * This field is not part of the effect.
     */
    private Set<Damageable> allTargets;
    /**
     * The list of available squares.
     * This field is not part of the effect.
     */
    private Set<Square> destinations;
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
    /**
     * The board of the game.
     * This field is not part of the effect.
     */
    private GameBoard board;

    /**
     * Creates a copy of the given card effect.
     *
     * @param toClone the card effect to be copied.
     */
    public CardEffect(CardEffect toClone) {
        id = toClone.id;
        damageAmount = toClone.damageAmount;
        marksAmount = toClone.marksAmount;
        secondaryDamage = toClone.secondaryDamage;
        secondaryMarks = toClone.secondaryMarks;
        targetsNumber = new Range(toClone.targetsNumber.getMin(),
                toClone.targetsNumber.getMax());
        targetsDistance = new Range(toClone.targetsDistance.getMin(),
                toClone.targetsDistance.getMax());
        historyPolicy = toClone.historyPolicy;
        targetsPolicy = toClone.targetsPolicy;
        squaresPolicy = toClone.squaresPolicy;
        squaresDistance = toClone.squaresDistance;
        quirks = toClone.quirks.clone();
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
     * This will filter the available targets, then will interact with the
     * {@code subjectPlayer} to choose the affected targets, then will apply
     * the effect.
     * <p>
     * All the damaged targets will be added in the {@code damageTargeted}
     * list; all the affected targets (damaged targets included) will be
     * added to the {@code allTargeted} list.
     */
    @Override
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets,
                          GameBoard board, List<Damageable> allTargeted,
                          List<Damageable> damageTargeted) {
        subject = subjectPlayer;
        alreadyTargeted = allTargeted;
        alreadyDamaged = damageTargeted;
        this.board = board;
        this.allTargets = new HashSet<>(allTargets);
        availableTargets = null;
        destinations = null;

        filterTargets();
        try {//FIXME: what to do if there are no availableTargets?
            selectTargets();
        } catch (AgainstRulesException e) {
            e.printStackTrace();
        } catch (ToClientException e) {
            return;
        }
        apply();
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

    /**
     * Filters the targets and the destinations based on the card settings.
     * {@code availableTargets} will contain all the available targets;
     * {@code destinations} will contain all the available destinations, or
     * will be null if the destinations are not relevant.
     */
    private void filterTargets() {

        /*Retrieving and filtering the targets*/
        availableTargets = targetsPolicy.getValidTargets(subject, allTargets,
                alreadyTargeted, board).stream()
                .filter(this::filterTarget)
                .collect(Collectors.toSet());

        /*Retrieving the filtered destinations*/
        destinations = getValidDestinations();

        /*If the destination is relevant*/
        if (destinations != null) {

            /*Keeping only the destinations with at least one valid target*/
            destinations = destinations.stream()
                    .filter(s -> {
                        for (Damageable d : availableTargets)
                            if (checkSquaresDistanceAndDirection(s, d.getPosition()))
                                return true;
                        return false;
                    }).collect(Collectors.toSet());

            /*Keeping only the targets with at least one valid destination*/
            availableTargets = availableTargets.stream()
                    .filter(d -> {
                        for (Square s : destinations)
                            if (checkSquaresDistanceAndDirection(s, d.getPosition()))
                                return true;
                        return false;
                    }).collect(Collectors.toSet());
        }
    }

    /**
     * Interacting with the subject to choose the targets and destinations for
     * this effect.
     * Targets are selected based on {@linkplain #targetsNumber}; only a
     * single destination can be selected if it is relevant.
     *
     * @throws AgainstRulesException if there are not enough targets or
     *                               destinations
     * @throws ToClientException     if the subject gets suspended
     */
    private void selectTargets() throws AgainstRulesException, ToClientException {
        /*If the subject must choose fewer targets than available*/
        if (targetsNumber.hasMinimum() &&
                availableTargets.size() > targetsNumber.getMin()) {
            chooseFrom(availableTargets, targetsNumber);

            /*If there are not enough targets*/
        } else if (availableTargets.size() < targetsNumber.getMin())
            throw new AgainstRulesException("Not enough targets!");

        /*If the destination is relevant*/
        if (destinations != null) {

            /*Destinations are filtered based on the selected targets*/
            destinations = destinations.stream()
                    .filter(s -> {
                        for (Damageable d : availableTargets)
                            if ((checkSquaresDistanceAndDirection(s,
                                    d.getPosition())))
                                return true;
                        return false;
                    }).collect(Collectors.toSet());

            /*The destination is relevant but there is not one*/
            if (destinations.isEmpty())
                throw new AgainstRulesException("Not enough destinations!");

            /*The subject must choose a single destination*/
            if (destinations.size() > 1)
                chooseFrom(new ArrayList<>(destinations));
        }
    }

    /**
     * Gives marks and damage, moves targets.
     * Gives {@linkplain #damageAmount} and {@linkplain #marksAmount} to each
     * of the selected targets; if {@linkplain #targetsNumber} allows only a
     * single target the {@linkplain #secondaryDamage} and
     * {@linkplain #secondaryMarks} are given to all the other damageable in
     * the same square or room, otherwise they are given to the target
     * nearest to the subject.
     */
    private void apply() {
        /*Giving damage and marks to selected targets and moving them*/
        for (Damageable d : availableTargets) {
            addDamage(d, damageAmount);
            addMarks(d, marksAmount);
            squaresPolicy.apply(subject, d, destinations);
        }

        /*Giving secondary damage and targets to other damageable*/
        if (targetsNumber.isSingleValue()) {
            applySecondarySingle();
        } else {
            if (!availableTargets.isEmpty()) {
                Damageable dam = getNearestTarget();
                addDamage(dam, secondaryDamage);
                addMarks(dam, secondaryMarks);
            }
        }

        /*Moving the subject if necessary*/
        if (QuirkPolicy.MOVE_TO_TARGET.isIn(quirks) == !availableTargets.isEmpty())
            subject.setPosition(getFurthestTarget().getPosition());
    }

    /**
     * Applying effects on secondary targets.
     * The {@linkplain #secondaryDamage} and {@linkplain #secondaryMarks} are
     * given to all the other damageable in the same square or room.
     */
    private void applySecondarySingle() {
        if (QuirkPolicy.ROOM.isIn(quirks))

            /*Targeting those in the same room of the target*/
            for (Square s : getSquaresSameRoom(availableTargets.iterator().next().getPosition()))
                for (Damageable d : board.getPlayerInSquare(s, allTargets)) {
                    addDamage(d, secondaryDamage);
                    addMarks(d, secondaryMarks);
                }
        else

            /*Targeting those in the same square of the target*/
            for (Damageable inSameSquare : board.getPlayerInSquare(availableTargets.iterator().next().getPosition(), allTargets))
                if (!inSameSquare.equals(subject)) {
                    addDamage(inSameSquare, secondaryDamage);
                    addMarks(inSameSquare, secondaryMarks);
                }
    }

    /**
     * Returns true if a target is acceptable.
     * Filtering is done based on {@linkplain #targetsPolicy},
     * {@linkplain #targetsDistance}, {@linkplain #quirks} and
     * {@linkplain #historyPolicy}.
     *
     * @param target the element to be examined
     * @return true if the element must be kept
     */
    private boolean filterTarget(Damageable target) {

        /*Memorizing the distance between the target and the subject*/
        int dist = board.minimumDistance(subject.getPosition(), target.getPosition(),
                QuirkPolicy.IGNORE_WALLS.isIn(quirks));

        /*Evaluating if the distance is acceptable*/
        boolean acceptableDistance = dist >= targetsDistance.getMin() &&
                (!targetsDistance.hasMaximum() || dist <= targetsDistance.getMax());

        /*If necessary, evaluating if the target is in a straight line from
        the subject*/
        if (QuirkPolicy.SINGLE_DIRECTION.isIn(quirks))
            acceptableDistance = acceptableDistance &&
                    subject.getPosition().straight(target.getPosition());

        /*Evaluating whether the player has been targeted twice*/
        boolean acceptableTarget = !QuirkPolicy.MAX_TWO_HITS.isIn(quirks) ||
                Collections.frequency(alreadyTargeted, target) < 2;

        /*Evaluating the target against the previous targets*/
        return acceptableDistance && acceptableTarget &&
                historyPolicy.filterTarget(target, alreadyTargeted, alreadyDamaged);
    }

    /**
     * Returns true if two squares have an acceptable
     * {@linkplain #squaresDistance}.
     * This also takes into consideration the {@linkplain #quirks};
     *
     * @param a the first square to be evaluated
     * @param b the second square to be evaluated
     * @return true if this couple is acceptable
     */
    private boolean checkSquaresDistanceAndDirection(Square a, Square b) {

        /*Evaluating if the distance is acceptable*/
        boolean acceptableDistance = board.minimumDistance(a, b,
                QuirkPolicy.IGNORE_WALLS.isIn(quirks)) <= squaresDistance;

        /*Taking into consideration whether the squares must be in a single
        direction*/
        return QuirkPolicy.SINGLE_DIRECTION.isIn(quirks) ?
                acceptableDistance && a.straight(b) : acceptableDistance;
    }

    /**
     * Returns the nearest of the {@linkplain #availableTargets}.
     *
     * @return the nearest target or a random one if it could not be found
     */
    private Damageable getNearestTarget() {
        if (!availableTargets.isEmpty())
            return availableTargets.stream().min(Comparator.comparingInt(a ->
                    board.minimumDistance(a.getPosition(), subject.getPosition(),
                            QuirkPolicy.IGNORE_WALLS.isIn(quirks))))
                    .orElse(availableTargets.iterator().next());
        throw new IllegalArgumentException("Can not find nearest target: no " +
                "targets.");
    }

    /**
     * Returns the furthest of the {@linkplain #availableTargets}.
     *
     * @return the furthest target or a random one if it could not be found
     */
    private Damageable getFurthestTarget() {
        if (!availableTargets.isEmpty())
            return availableTargets.stream().max(Comparator.comparingInt(a ->
                    board.minimumDistance(a.getPosition(), subject.getPosition(),
                            QuirkPolicy.IGNORE_WALLS.isIn(quirks))))
                    .orElse(availableTargets.iterator().next());
        throw new IllegalArgumentException("Can not find furthest target: no " +
                "targets.");
    }

    /**
     * Returns the valid destinations.
     * This takes into account {@linkplain #squaresPolicy}, if
     * {@linkplain QuirkPolicy#ROOM} is selected, then the squares in the
     * subject's room are not included.
     *
     * @return the valid destinations
     */
    private Set<Square> getValidDestinations() {

        /*Retrieving the valid destinations*/
        Set<Square> valid = squaresPolicy.getValidDestinations(subject, board,
                alreadyTargeted);

        /*If ROOM is selected*/
        if (QuirkPolicy.ROOM.isIn(quirks) && valid != null) {

            /*Memorizing all the squares in the same room of the destinations*/
            Set<Square> others = new HashSet<>();
            for (Square s : valid)
                others.addAll(getSquaresSameRoom(s));
            valid.addAll(others);

            /*Filtering out the squares in the subject's room*/
            valid = valid.stream().distinct()
                    .filter(s -> !(s.getRoom().equals(subject.getPosition().getRoom())))
                    .collect(Collectors.toSet());
        }
        return valid;
    }

    /**
     * Returns all the squares in the same room of a given one.
     *
     * @param s the square in the room to be evaluated
     * @return all the squares in the same room of {@code s}
     */
    private List<Square> getSquaresSameRoom(Square s) {
        return board.getAllSquares().stream()
                .filter(x -> x.getRoom().equals(s.getRoom()))
                .collect(Collectors.toList());
    }

    /**
     * Handles the interaction with the subject for targets.
     * Prepares all the combinations of possible targets and makes the
     * subject choose.
     *
     * @param available the valid targets
     * @param amount    the range of target to be chosen, {@code [0, n... -1]}
     * @throws AgainstRulesException if there are not enough targets
     * @throws ToClientException     if the subject gets suspended
     */
    private void chooseFrom(Set<Damageable> available, Range amount)
            throws AgainstRulesException, ToClientException {

        /*Preparing a list of all the possible sequences*/
        List<List<Damageable>> choices = new ArrayList<>();
        combinations(new ArrayList<>(available), amount.getMin(),
                amount.hasMaximum() ? amount.getMax() : available.size(),
                choices);

        /*Filtering out the choices that are not valid*/
        choices = choices.stream().filter(c -> {
            if (QuirkPolicy.DIFFERENT_SQUARES.isIn(quirks) &&
                    c.stream().map(Damageable::getPosition)
                            .distinct().count() != c.size())
                return false;
            return !QuirkPolicy.SINGLE_DIRECTION.isIn(quirks) ||
                    c.size() != 2 ||
                    c.get(0).getPosition().straight(c.get(1).getPosition());
        }).collect(Collectors.toList());

        /*Throwing an exception if there are not enough targets*/
        if (amount.getMin() > 0 && choices.isEmpty())
            throw new AgainstRulesException("Not enough targets!");

        /*Asking the subject for the desired sequence*/
        available.clear();
        available.addAll(new HashSet<>(subject.getToClient().chooseTarget(choices)));
    }

    /**
     * Handles the interaction with the subject for destinations.
     *
     * @param available the available destinations
     * @throws ToClientException if the subject gets suspended
     */
    private void chooseFrom(List<Square> available) throws ToClientException {
        Square chosen;
        chosen = subject.getToClient().chooseDestination(available);
        available.clear();
        available.add(chosen);
    }

    /**
     * Recursive method for creating all the possible combinations.
     *
     * @param elements  the elements of the combinations
     * @param min       the minimum number of elements allowed
     * @param max       the maximum number of elements allowed
     * @param collector where all the possible combinations will be stored
     */
    private void combinations(List<Damageable> elements, int min, int max,
                              List<List<Damageable>> collector) {

        /*Adding this element only if it has the right length and is not
        already present*/
        if (elements.size() >= min && elements.size() <= max && !collector.contains(elements)) {
            collector.add(elements);//TODO check if duplicates are added
        }

        /*Removing and calling combination for each element*/
        if (elements.size() > min)
            for (Damageable d : elements) {
                List<Damageable> sublist = new ArrayList<>(elements);
                sublist.remove(d);
                combinations(sublist, min, max, collector);
            }
    }

    /**
     * Adds the specified damage to the provided target and updates
     * {@linkplain #alreadyDamaged} and {@linkplain #alreadyTargeted}.
     *
     * @param damaged the target
     * @param amount  the number of damage to be added
     */
    private void addDamage(Damageable damaged, int amount) {
        List<Player> damage = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            damage.add(subject);
        }
        damaged.giveDamage(damage);
        if (!alreadyDamaged.contains(damaged))
            alreadyDamaged.add(damaged);
        if (!alreadyTargeted.contains(damaged))
            alreadyTargeted.add(damaged);
    }

    /**
     * Adds the specified marks to the provided target and updates
     * {@linkplain #alreadyTargeted}.
     *
     * @param damaged the target
     * @param amount  the number of marks to be added
     */
    private void addMarks(Damageable damaged, int amount) {
        List<Player> marks = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            marks.add(subject);
        }
        damaged.giveMark(marks);
        if (!alreadyTargeted.contains(damaged))
            alreadyTargeted.add(damaged);
    }

    /**
     * Checks if this is a valid effect.
     *
     * @return true if this is a valid effect
     */
    public boolean checkIntegrity() {
        return id != null && !"".equals(id) &&
                damageAmount >= 0 &&
                marksAmount >= 0 &&
                secondaryDamage >= 0 &&
                secondaryMarks >= 0 &&
                targetsNumber.getMin() >= -1 && targetsNumber.getMax() >= -1 &&
                (!targetsNumber.hasMaximum() || !targetsNumber.hasMinimum()
                        || targetsNumber.getMax() >= targetsNumber.getMin()) &&
                targetsDistance.getMin() >= 0 && targetsDistance.getMax() >= -1 &&
                (!targetsDistance.hasMaximum() || !targetsDistance.hasMinimum()
                        || targetsDistance.getMax() >= targetsDistance.getMin()) &&
                historyPolicy != null &&
                targetsPolicy != null &&
                squaresPolicy != null &&
                squaresDistance >= 0 &&
                quirks != null &&
                (!QuirkPolicy.SINGLE_DIRECTION.isIn(quirks)
                        || targetsNumber.getMax() <= 2);
    }
}