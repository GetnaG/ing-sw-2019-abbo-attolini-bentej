package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.server.model.AgainstRulesException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class CardEffect implements EffectInterface {
    private String id;
    private int damageAmount;
    private int marksAmount;
    private int secondaryDamage;//if 1 target, everyone in square, else to nearest
    private int secondaryMarks;
    private CardEffect.Range targetsNumber;//-1,n don't choose; n,-1 no upper
    private CardEffect.Range targetsDistance;
    private CardEffect.HistoryPolicy historyPolicy;
    private CardEffect.TargetsPolicy targetsPolicy;
    private CardEffect.SquaresPolicy squaresPolicy;
    private int squaresDistance;
    private CardEffect.QuirkPolicy[] quirks;
    private EffectInterface decorated;
    private List<Damageable> targets;
    private List<Square> destinations;
    private Player subject;
    private List<Damageable> alreadyTargeted;

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

    /**
     * {@inheritDoc}
     * This will interact with the player to choose the targets.
     */
    @Override
    public void runEffect(Player subjectPlayer, GameBoard board,
                          List<Damageable> alreadyTargeted) {
        subject = subjectPlayer;
        this.alreadyTargeted = alreadyTargeted;
        filterTargets();
        try {//FIXME
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
     * Returns an iterator over the elements of this chain of effects.
     *
     * @return an iterator over the elements of this chain of effects
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        return new EffectIterator(this);
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